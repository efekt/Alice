package it.efekt.alice.core;

public class GuildConfigOld {
    private String guildId;
    private String cmdPrefix = "<";

    public GuildConfigOld(String guildId){
        this.guildId = guildId;
    }

    public String getCmdPrefix(){
        return this.cmdPrefix;
    }

    public void setCmdPrefix(String cmdPrefix){
        this.cmdPrefix = cmdPrefix;
    }
}
