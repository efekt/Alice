package it.efekt.alice.commands.core;

public enum CommandCategory {
    BLANK("uncategorized"),
    BOT_ADMIN("admin"),
    FUN("Fun"),
    NSFW("NSFW");

    String name;

    CommandCategory(String name){
        this.name = name;
    }
}
