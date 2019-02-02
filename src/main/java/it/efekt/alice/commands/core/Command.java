package it.efekt.alice.commands.core;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Command extends ListenerAdapter {
    protected String alias;
    private String desc = "";
    private String[] args;
    private String usageInfo = "";
    private List<Permission> permissions = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(Command.class);
    private boolean isNsfw = false;
    private boolean isAdminCommand = false;
    private CommandCategory category;

    public Command(String alias){
        this.alias = alias;
    }

    public abstract void onCommand(MessageReceivedEvent e);

    private void execute(MessageReceivedEvent e){
        this.onCommand(e);
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

    protected void setDescription(String desc){
        this.desc = desc;
    }

    public void setAlias(){
        this.alias = alias;
    }

    public boolean isAdminCommand(){
        return this.isAdminCommand;
    }

    public void setIsAdminCommand(boolean isAdminCommand){
        this.isAdminCommand = isAdminCommand;
    }

    public boolean isNsfw(){
        return this.isNsfw;
    }

    public void setNsfw(boolean isNsfw){
        this.isNsfw = isNsfw;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getAlias(){
        return this.alias;
    }

    public String getUsageInfo(){
        return this.usageInfo;
    }

    protected void setUsageInfo(String usageInfo){
        this.usageInfo = usageInfo;
    }

    protected String[] getArgs(){
        return this.args;
    }

    protected void addPermission(Permission permission){
        this.permissions.add(permission);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        String[] allArgs = e.getMessage().getContentDisplay().split("\\s+");

        if (allArgs[0].startsWith(getGuildPrefix(e.getGuild()))){
            String cmdAlias = allArgs[0].replaceFirst(Pattern.quote(getGuildPrefix(e.getGuild())), "");
            String[] args = Arrays.copyOfRange(allArgs, 1, allArgs.length);
            if (this.alias.equalsIgnoreCase(cmdAlias)){
                this.logger.debug("User: " + e.getAuthor().getName() + " id:" + e.getAuthor().getId() + " is requesting cmd: " + cmdAlias + " with msg: " + e.getMessage().getContentDisplay());
                // Prevent bots from using commands
                if (e.getAuthor().isBot()){
                    return;
                }
                // Only for debug purposes, change it later //todo implement better admin commands
                if (isAdminCommand() && !e.getAuthor().getId().equalsIgnoreCase("128146616094818304")){
                    return;
                }

                if (canUseCmd(e.getMember())){
                    if (isNsfw() && !e.getTextChannel().isNSFW()){
                        e.getChannel().sendMessage(new EmbedBuilder()
                                .setThumbnail("https://i.imgur.com/L3o8Xq0.jpg")
                                .setTitle("Ta komenda jest NSFW!")
                                .setColor(AliceBootstrap.EMBED_COLOR)
                                .setDescription("Dozwolona tylko na kanałach z włączoną obsługą treści NSFW")
                                .build()).queue();
                        return; // nsfw content on not-nsfw channel
                    }

                    this.args = args;
                    this.execute(e);
                    this.logger.debug("User: " + e.getAuthor().getName() + " id:" + e.getAuthor().getId() + " executed cmd: " + cmdAlias + " with msg: " + e.getMessage().getContentDisplay());
                }
            }
        }
    }
}
