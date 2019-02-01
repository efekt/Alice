package it.efekt.alice.commands.database;

import net.dv8tion.jda.core.entities.Guild;



public class Database {


    public Database(){

    }


    public String getGuildPrefix(Guild guild){
        String prefix = "!";

        return prefix;
    }

    public void setGuildPrefix(Guild guild, String prefix){

    }

}
