package it.efekt.alice.core;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
    private String alias;
    private String desc;

    public Command(String alias){
        this.alias = alias;
    }

    public abstract void onCommand(MessageReceivedEvent e, String[] args);

    public void execute(MessageReceivedEvent e, String[] args){
        this.onCommand(e, args);
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getAlias(){
        return this.alias;
    }

    public void setAlias(){
        this.alias = alias;
    }
}
