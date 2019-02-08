package it.efekt.alice.commands.core;

public enum CommandCategory {
    BLANK("uncategorized"),
    BOT_ADMIN("admin"),
    UTILS("Utils"),
    FUN("Fun"),
    NSFW("NSFW");

    String name;

    CommandCategory(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
