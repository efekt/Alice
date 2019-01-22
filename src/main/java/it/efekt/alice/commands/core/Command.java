package it.efekt.alice.commands.core;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;

public abstract class Command extends ListenerAdapter {
    protected String alias;
    private String desc = "";
    private String[] args;
    private String usageInfo = "";

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


    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        String[] allArgs = e.getMessage().getContentDisplay().split("\\s+");
        if (allArgs[0].startsWith( getGuildPrefix(e.getGuild()))){
            String cmdAlias = allArgs[0].replaceFirst(getGuildPrefix(e.getGuild()), "");
            String[] args = Arrays.copyOfRange(allArgs, 1, allArgs.length);
            if (this.alias.equalsIgnoreCase(cmdAlias)){
                //todo permissions system?
                this.args = args;
                this.execute(e);
            }
        }
    }
}
