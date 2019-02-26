package it.efekt.alice.lang;

import com.google.gson.stream.JsonWriter;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public enum Message {
    CMD_HELP_TITLE("Alice - Centrum Pomocy {1}"),
    CMD_UNKNOWN_COMMAND("Nieznana komenda"),
    CMD_COMMAND_NOT_FOUND("cmd-command-not-found" ),
    CMD_HELP_CATEGORY("Kategoria"),
    CMD_HELP_REQUIRED_PERM("Wymagane uprawnienia"),
    CMD_HELP_USAGE_INFO("<komenda> - wyświetla pomoc podanej komendy"),
    CMD_HELP_DESCRIPTION("Wyświetla ekran pomocy"),





    CMD_PING_DESCRIPTION("Wyświetla ekran pomocy");



    private String defaultValue;

    Message(String defaultText){
        this.defaultValue = defaultText;
    }

    public String getKey(){
        return this.name().replaceAll("_", "-").toLowerCase();
    }

    public String getDefaultValue(){
        return this.defaultValue;
    }

    public String get(Language language, String... vars){
        return language.getLanguageString(getKey()).var(vars);
    }

    public String get(MessageReceivedEvent e, String... vars){
       return getLanguage(e).getLanguageString(getKey()).var(vars);
    }

    public Language getLanguage(MessageReceivedEvent e){
        String locale = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getLocale();
        return AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(locale));
    }

    public static Message getDefaultMessage(String key){
        return Arrays.stream(Message.values()).filter(msg -> msg.getKey().equals(key)).findFirst().get();
    }

    public static void main(String[] args){
        // Generate json file with default values from this enum
        System.out.println("Generating all messages to json file");
        try {
            JsonWriter jsonWriter = new JsonWriter(new FileWriter("./en_US.json"));
            jsonWriter.beginObject();
            jsonWriter.setIndent("  ");
            for (Message message : Message.values()){
                jsonWriter.name(message.getKey()).value(message.getDefaultValue());
            }
            jsonWriter.endObject();
            jsonWriter.close();
            System.out.println("Saved all messages!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
