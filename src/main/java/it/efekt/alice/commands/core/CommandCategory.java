package it.efekt.alice.commands.core;

public enum CommandCategory {
    BLANK("uncategorized"),
    BOT_ADMIN("admin"),
    DISCORD_ADMIN_UTILS("Admin utils"),
    UTILS("Utils"),
    FUN("Fun"),
    NSFW("NSFW"),
    GAMES("Games"),
    VOICE("Voice");

    String name;

    CommandCategory(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
