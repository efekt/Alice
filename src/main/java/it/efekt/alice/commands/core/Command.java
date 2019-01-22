package it.efekt.alice.commands.core;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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

    public Command(String alias){
        this.alias = alias;
    }

    public abstract void onCommand(MessageReceivedEvent e);

    public void execute(MessageReceivedEvent e){
        this.onCommand(e);
    }

    public String getGuildPrefix(Guild guild){
        return AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getCmdPrefix();
    }

    public boolean canUseCmd(Member member){
        return member.hasPermission(this.permissions);
    }

    protected void setDescription(String desc){
        this.desc = desc;
    }

    public void setAlias(){
        this.alias = alias;
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

    public void setUsageInfo(String usageInfo){
        this.usageInfo = usageInfo;
    }

    public String[] getArgs(){
        return this.args;
    }

    public void addPermission(Permission permission){
        this.permissions.add(permission);
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        String[] allArgs = e.getMessage().getContentDisplay().split("\\s+");

        if (allArgs[0].startsWith(getGuildPrefix(e.getGuild()))){
            String cmdAlias = allArgs[0].replaceFirst(Pattern.quote(getGuildPrefix(e.getGuild())), "");
            String[] args = Arrays.copyOfRange(allArgs, 1, allArgs.length);
            if (this.alias.equalsIgnoreCase(cmdAlias)){
                //todo permissions system?
                if (canUseCmd(e.getMember())){
                    this.args = args;
                    this.execute(e);
                }
            }
        }
    }
}
