package it.efekt.alice.commands.core;

import it.efekt.alice.commands.util.VoteCmd;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.LangCode;
import it.efekt.alice.lang.Language;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public abstract class Command extends ListenerAdapter {
    private String BOT_AUTHOR_ID = "128146616094818304";
    protected String alias;
    private String[] args;
    private Message desc = Message.BLANK;
    // Short line next to command alias
    private Message shortUsageInfo = Message.BLANK;
    // Full explanation of all arguments in commands
    private Message fullUsageInfo = Message.BLANK;
    private List<Permission> permissions = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(Command.class);
    private boolean isNsfw = false;
    private boolean isAdminCommand = false;
    private boolean isPrivateChannelCmd = false;
    private boolean isVoteRequired = false;
    private HashMap<String, Long> usersTimeVoted = new HashMap<>();
    private CommandCategory category = CommandCategory.BLANK;
    public Command(String alias){
        this.alias = alias;
    }

    public abstract boolean onCommand(MessageReceivedEvent e);

    private void execute(MessageReceivedEvent e) {
        Runnable runnable = () -> {
            // If command is returning false, means that something is wrong
            if (!isPrivateChannelCmd) {
                AliceBootstrap.analytics.reportCmdUsage(getAlias(), Arrays.asList(getArgs()).toString(), e.getGuild(), e.getAuthor());
            }
                try {
                    if (!this.onCommand(e)) {
                        e.getChannel().sendMessage(Message.CMD_CHECK_IF_IS_CORRECT.get(e, "Type `<help` `" + getAlias() + "` to see the command's help")).queue();
                        return;
                    }
                } catch (InsufficientPermissionException exc){
                    e.getChannel().sendMessage(Message.PERMISSION_NEEDED.get(e, exc.getPermission().name())).queue();
                }

        };
        new Thread(runnable).start();
    }

    protected String getGuildPrefix(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getPrefix();
    }

    public boolean canUseCmd(Member member){
        return member.hasPermission(this.permissions);
    }

    public void setCategory(CommandCategory category){
        this.category = category;
    }

    public CommandCategory getCommandCategory(){
        return this.category;
    }

    protected void setDescription(Message desc){
        this.desc = desc;
    }

    public void setAlias(){
        this.alias = alias;
    }

    private boolean isAdminCommand(){
        return this.isAdminCommand;
    }

    protected void setIsAdminCommand(boolean isAdminCommand){
        this.isAdminCommand = isAdminCommand;
    }

    private boolean isVoteRequired(){
        return this.isVoteRequired;
    }

    public void setIsVoteRequired(boolean isVoteRequired){
        this.isVoteRequired = isVoteRequired;
    }

    public boolean isNsfw(){
        return this.isNsfw;
    }

    protected void setNsfw(boolean isNsfw){
        this.isNsfw = isNsfw;
    }

    public Message getDesc(){
        return this.desc;
    }

    public String getAlias(){
        return this.alias;
    }

    public Message getShortUsageInfo(){
        return this.shortUsageInfo;
    }

    public List<Permission> getPermissions(){
        return this.permissions;
    }

    public Message getFullUsageInfo() {
        return this.fullUsageInfo;
    }

    protected void setFullUsageInfo(Message fullUsageInfo) {
        this.fullUsageInfo = fullUsageInfo;
    }

    protected void setShortUsageInfo(Message shortUsageInfo){
        this.shortUsageInfo = shortUsageInfo;
    }

    protected String[] getArgs(){
        return this.args;
    }

    protected void addPermission(Permission permission){
        this.permissions.add(permission);
    }

    protected void setPrivateChannelCmd(boolean isPrivateChannelCmd){
        this.isPrivateChannelCmd = isPrivateChannelCmd;
    }

    protected GuildConfig getGuildConfig(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild);
    }

    protected Language lang(MessageReceivedEvent e){
        String locale = getGuildConfig(e.getGuild()).getLocale();
        return AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(locale));
    }

    private boolean hasVoted(String userId){


        if (this.usersTimeVoted.containsKey(userId)) {
            if (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - this.usersTimeVoted.get(userId)) <= 24) {
                logger.info("user found on the temporary list, true");
                return true;
            } else {
                this.usersTimeVoted.remove(userId);
                logger.info("the time is up, user deleted from the temp list, false");

                return false;
            }
        }
        AtomicBoolean voted = new AtomicBoolean();
        try {
            DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                    .token(AliceBootstrap.alice.getConfig().getDiscordBotListApiToken())
                    .botId(AliceBootstrap.alice.getShardManager().getShards().get(0).getSelfUser().getId())
                    .build();
            api.hasVoted(userId).whenComplete((hasVoted, exc) -> {
                if (hasVoted) {
                    logger.info("got info from botlist, " + hasVoted.toString());

                    this.usersTimeVoted.put(userId, System.currentTimeMillis());
                    voted.set(true);
                }
            }).toCompletableFuture().get();


            logger.info("returning final hasVoted value, " + voted.get());
            return voted.get();
        } catch (Exception exc){
            logger.warn("There was a problem while connecting to DiscordBotListAPI, checking for votes has been omitted");
            return true;
        }
    }

    protected boolean isMentioningSelf(String[] args){
        //removing "!" from mention (it occurs when mentioned user has it's nickname changed)
        return args[0].replace("!", "").equalsIgnoreCase(AliceBootstrap.alice.getShardManager().getShards().get(0).getSelfUser().getAsMention()) && args.length>=2;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        long commandStartTime = System.currentTimeMillis();
        try {
            String[] allArgs = e.getMessage().getContentRaw().split("\\s+");
            // getting alias and cmd args accordingly to prefix (mention vs standard prefix)
            if (isMentioningSelf(allArgs) || (e.isFromGuild() && allArgs[0].startsWith(getGuildPrefix(e.getGuild())))) {
                String cmdAlias;
                String[] args;
                if (!isMentioningSelf(allArgs)) {
                    cmdAlias = allArgs[0].replaceFirst(Pattern.quote(getGuildPrefix(e.getGuild())), "");
                    args = Arrays.copyOfRange(allArgs, 1, allArgs.length);
                } else {
                    cmdAlias = allArgs[1];
                    args = Arrays.copyOfRange(allArgs, 2, allArgs.length);
                }

                if (this.alias.equalsIgnoreCase(cmdAlias)) {
                    if (e.isFromType(ChannelType.PRIVATE)) {
                        if (!isPrivateChannelCmd) {
                            return;
                        }
                    } else {
                        if (isPrivateChannelCmd) {
                            e.getTextChannel().sendMessage("This command can be used on private channel only.").complete();
                            return;
                        }
                    }

                    if (!isPrivateChannelCmd) {
                        this.logger.debug("User: " + e.getAuthor().getName() + " id:" + e.getAuthor().getId() + " is requesting cmd: " + cmdAlias + " with msg: " + e.getMessage().getContentDisplay());
                    }
                    // Prevent bots from using commands
                    if (e.getAuthor().isBot()) {
                        return;
                    }

                    if (!isPrivateChannelCmd && getGuildConfig(e.getGuild()).isCmdDisabled(cmdAlias)) {
                        return;
                    }

                    // Only for debug purposes, change it later //todo implement better admin commands
                    if (isAdminCommand() && !e.getAuthor().getId().equalsIgnoreCase(BOT_AUTHOR_ID)) {
                        return;
                    }
                    // checking for author is important to filter private message commands, that are for admin only
                    if (e.getAuthor().getId().equalsIgnoreCase(BOT_AUTHOR_ID) || canUseCmd(e.getMember())) {
                        if (isNsfw() && !e.getTextChannel().isNSFW()) {
                            e.getChannel().sendMessage(new EmbedBuilder()
                                    .setThumbnail("https://i.imgur.com/L3o8Xq0.jpg")
                                    .setTitle(Message.CMD_THIS_IS_NSFW_CMD.get(e))
                                    .setColor(AliceBootstrap.EMBED_COLOR)
                                    .setDescription(Message.CMD_NSFW_ALLOWED_ONLY.get(e))
                                    .build()).queue();
                            return; // nsfw content on not-nsfw channel
                        }

                        // check if user-vote is required in order to execute this command
                        if (this.isVoteRequired && !hasVoted(e.getAuthor().getId())){
                            e.getChannel().sendMessage(new EmbedBuilder()
                                    .setTitle(Message.VOTE_REQUIRED_TITLE.get(e))
                                    .setColor(AliceBootstrap.EMBED_COLOR)
                                    .setDescription(Message.VOTE_REQUIRED_INFO.get(e) + VoteCmd.VOTE_URL)
                                    .build()).queue();
                        }
                        this.args = args;
                        e.getChannel().sendTyping().queue();
                        this.execute(e);
                        this.logger.info("User: " + e.getAuthor().getName() + " id:" + e.getAuthor().getId() + " executed cmd: " + cmdAlias + " with msg: " + e.getMessage().getContentDisplay() +" took: " + (System.currentTimeMillis() - commandStartTime) + "ms");
                    }
                }
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
