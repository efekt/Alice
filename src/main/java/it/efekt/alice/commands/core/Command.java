package it.efekt.alice.commands.core;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.lang.LangCode;
import it.efekt.alice.lang.Language;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.discordbots.api.client.DiscordBotListAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Command {
    private Logger logger = LoggerFactory.getLogger(Command.class);

    protected String alias;
    protected String[] args;
    protected AMessage desc = AMessage.BLANK;
    // Short line next to command alias
    protected AMessage shortUsageInfo = AMessage.BLANK;
    // Full explanation of all arguments in commands
    protected AMessage fullUsageInfo = AMessage.BLANK;
    protected List<Permission> permissions = new ArrayList<>();
    protected boolean isNsfw = false;
    protected boolean isAdminCommand = false;
    protected boolean isPrivateChannelCmd = false;
    protected boolean isVoteRequired = false;
    protected HashMap<String, Long> usersTimeVoted = new HashMap<>();
    protected CommandCategory category = CommandCategory.BLANK;

    public Command(String alias){
        this.alias = alias;
    }


    public abstract boolean onCommand(MessageReceivedEvent e);

    protected void execute(MessageReceivedEvent e) {
        Runnable runnable = () -> {
            // If command is returning false, means that something is wrong
            if (!isPrivateChannelCmd) {
                AliceBootstrap.analytics.reportCmdUsage(getAlias(), Arrays.asList(getArgs()).toString(), e.getGuild(), e.getAuthor());
            }
            try {
                if (!this.onCommand(e)) {
                    e.getChannel().sendMessage(AMessage.CMD_CHECK_IF_IS_CORRECT.get(e, "Type `<help` `" + getAlias() + "` to see the command's help")).queue();
                    return;
                }
            } catch (InsufficientPermissionException exc){
                e.getChannel().sendMessage(AMessage.PERMISSION_NEEDED.get(e, exc.getPermission().name())).queue();
            }

        };
        new Thread(runnable).start();
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

    protected void setDescription(AMessage desc){
        this.desc = desc;
    }

    public void setAlias(){
        this.alias = alias;
    }

    protected boolean isAdminCommand(){
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

    public AMessage getDesc(){
        return this.desc;
    }

    public String getAlias(){
        return this.alias;
    }

    public AMessage getShortUsageInfo(){
        return this.shortUsageInfo;
    }

    public List<Permission> getPermissions(){
        return this.permissions;
    }

    public AMessage getFullUsageInfo() {
        return this.fullUsageInfo;
    }

    protected void setFullUsageInfo(AMessage fullUsageInfo) {
        this.fullUsageInfo = fullUsageInfo;
    }

    protected void setShortUsageInfo(AMessage shortUsageInfo){
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

    protected String getGuildPrefix(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getPrefix();
    }

    protected Language lang(MessageReceivedEvent e){
        String locale = getGuildConfig(e.getGuild()).getLocale();
        return AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(locale));
    }


    protected boolean hasVoted(String userId){


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


}
