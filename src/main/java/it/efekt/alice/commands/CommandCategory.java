package it.efekt.alice.commands;

public enum CommandCategory {
    BLANK("uncategorized"),
    FUN("Fun"),
    NSFW("NSFW");

    String name;

    CommandCategory(String name){
        this.name = name;
    }
}
