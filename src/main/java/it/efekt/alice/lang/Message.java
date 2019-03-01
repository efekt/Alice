package it.efekt.alice.lang;

import com.google.gson.stream.JsonWriter;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public enum Message {
    ALICE_GOODBYE_RESPONSE("Good night! {1}"),
    ALICE_GOODBYE_REQUIRED("good night"),
    ALICE_MORNING_RESPONSE("Good Morning! {1}"),
    ALICE_MORNING_REQUIRED("good morning"),
    FILE_NOT_FOUND("File not found :("),
    CMD_HELP_TITLE("Alice - Help"),
    CMD_UNKNOWN_COMMAND("Unknown command"),
    CMD_COMMAND_NOT_FOUND("Command not found" ),
    CMD_CHECK_IF_IS_CORRECT("Check if you typed a correct command\n{1}"),
    CMD_THIS_IS_NSFW_CMD("This command is NSFW!"),
    CMD_NSFW_ALLOWED_ONLY("Allowed only on NSFW enabled channels"),
    CMD_NSFW_NOTIFICATION("(nsfw)"),
    CMD_HELP_CATEGORY("Category"),
    CMD_HELP_REQUIRED_PERMS("Required Permissions"),
    CMD_HELP_USAGE_INFO("<command> - shows command's help"),
    CMD_HELP_DESCRIPTION("Shows help screen"),
    CMD_HELP_SHORT_USAGE_INFO("`|cmd|`"),
    CMD_HELP_COMMAND_UNKNOWN("Unknown command"),
    CMD_HELP_NOT_FOUND_TRY_AGAIN("Command not found, try again"),
    CMD_HELP_FOOTER("<command> - shows command's help"),
    CMD_HENTAI_DESC("Watch out! Uncharted waters!\n Available types: \n `random` `neko` `alice`"),
    CMD_HENTAI_SHORT_USAGE_INFO("`type`"),
    CMD_HENTAI_CATEGORY_UNKNOWN("I do not know this category. Choose one of them:\n {1}"),
    CMD_HENTAI_NOT_FOUND("Not found"),
    CMD_HENTAI_CHECK_COMMAND("Proper command usage:"),
    CMD_HENTAI_ERROR_NOT_FOUND("Nothing has been found :("),
    CMD_NEKO_DESC("Nyaaaaaa!"),
    CMD_APEX_SHORT_USAGE_INFO("`pc/psn/xbl` `nick`"),
    CMD_APEX_FULL_USAGE_INFO("`pc/psn/xbl` - platform, choose one\n `nick` - Apex username"),
    CMD_APEX_DESC("Shows Apex statistics (Experimental)"),
    CMD_APEX_PLAYER_NOT_FOUND("Player not found"),
    CMD_APEX_EMBED_TITLE("APEX - Statistics (Experimental)"),
    CMD_APEX_EMBED_DESC("Some values can be incorrect or missing"),
    CMD_APEX_PLAYERNAME("Player Name"),
    CMD_APEX_LEVEL("Level"),
    CMD_APEX_KILLS("Kills"),
    CMD_APEX_HEADSHOTS("Headshots"),
    CMD_APEX_MATCHES("Matches"),
    CMD_APEX_LEGEND_KILLS("Kills: "),
    CMD_APEX_LEGEND_HEADSHOTS("Headshots: "),
    CMD_ASUNA_DESC("Shows random picture of Asuna"),
    CMD_KOJIMA_DESC("Kojumbo"),
    CMD_MC_DESC("Shows current status of given Minecraft server"),
    CMD_MC_SHORT_USAGE_INFO("`ip`"),
    CMD_MC_FULL_USAGE_INFO("`ip` - Minecraft server address"),
    CMD_MC_SERVER_NOT_FOUND("Server not found"),
    CMD_MC_SERVER_STATUS("Server Status"),
    CMD_MC_SERVER_PLAYER_COUNT("Player count"),
    CMD_MC_SERVER_OFFLINE("Serwer is offline"),
    CMD_TOMASZ_DESC("Tomasz"),
    CMD_TOMEK_DESC("Tomek"),
    CMD_FEATURES_DESC("This server's command availability management \n `disable/enable` `command` - disables/enables command"),
    CMD_FEATURES_WRONG_FEATURE_GIVEN("Wrong command name"),
    CMD_FEATURES_CANNOT_DISABLE("You cannot disable this command"),
    CMD_FEATURES_HAS_BEEN_DISABLED("Command {1} has been disabled"),
    CMD_FEATURES_HAS_BEEN_ENABLED("Command {1} has been enabled"),
    CMD_FEATURES_LIST("List of all bot's commands/features"),
    CMD_LOGGER_DESC("Sets server logs channel"),
    CMD_LOGGER_SHORT_USAGE_INFO("`#channel` or `disable`"),
    CMD_LOGGER_FULL_USAGE_INFO("`#channel` - log channel of your choice\n`disable` - disables logger"),
    CMD_LOGGER_NOT_SET("Logger channel not set"),
    CMD_LOGGER_CURRENTLY_USED("Currently used logger channel: {1}"),
    CMD_LOGGER_SET("Logger channel set to: {1}"),
    CMD_LOGGER_DISABLED("Logger has been disabled on this server"),
    CMD_HISTORYDEL_DESC("Deletes last n messages on the channel"),
    CMD_PING_DESC("Ping test"),
    CMD_PING_RESPONSE("Pong: {1}ms"),
    CMD_HISTORYDEL_SHORT_USAGE_INFO("`number of messages`"),
    CMD_PREFIX_DESC("Sets prefix, which will be used on this server"),
    CMD_PREFIX_SHORT_USAGE_INFO("`prefix`"),
    CMD_PREFIX_FULL_USAGE_INFO("`prefix` - your desired prefix"),
    CMD_PREFIX_REQUIREMENTS("It is required for prefix to contain only one character"),
    CMD_NEW_PREFIX_SET("New prefix for this server: {1}"),
    CMD_CHANGED_PREFIX_LOG("{1} changed prefix to: {2}"),
    CMD_TOP_DESC("Shows ranking of the best spammers"),
    CMD_TOP_USAGE_INFO("`number of users` or `loadAll`"),
    CMD_TOP_MINUS_TOP("u mad...?"),
    CMD_TOP_NUMBER_TOO_LARGE("Couldn't find so many users. You can specified max {1}"),
    CMD_TOP_LOADALL_FORBIDDEN("Only Admin can load all messages"),
    CMD_TOP_FULL_USAGE_INFO("`number of users` - optional\n`loadAll` - loads the entire server messages history"),
    CMD_TOP_LOADALL_WARNING("Trying to gather all messages ever written.\nIt may last a couple of seconds/minutes depending of server size :fearful:"),
    CMD_TOP_LOADALL_FOUND("Found {1} messages, processing..."),
    CMD_TOP_LOADALL_FINISHED("Processing finished, saving to database..."),
    CMD_TO_LOADALL_SAVED("All results has been saved :heart_eyes:"),
    CMD_TOP_LIST_TITLE("Ranking of the best {1}'s spammers"),
    CMD_USERINFO_DESC("Shows info about user"),
    CMD_USERINFO_USAGE_INFO("`@user`"),
    CMD_USERINFO_TITLE("Info about {1}"),
    CMD_USERINFO_ACCOUNT_CREATED("Account creation time"),
    CMD_USERINFO_SPAM_LVL("Spam level"),
    CMD_USERINFO_MSGS_SENT("Messages sent"),
    CMD_LANG_DESC("Sets the default language of the server"),
    CMD_LANG_SHORT_USAGE_INFO("`code` - language code"),
    CMD_LANG_LANGUAGE_CHANGED("This server's language has been changed to: {1}"),
    CMD_LANG_AVAILABLE_LANGS("Available languages: {1}"),
    CMD_RANDOMWAIFU_DESC("Shows random waifu from `https://www.thiswaifudoesnotexist.net`\nEach waifu has been generated by neural network\nRead more on the creator's website: `https://www.gwern.net/TWDNE`"),
    MODULE_LOGGER_INSUFFICIENT_PERMS("Hi {1} You have asked me to log messages and stuff to the {2} channel but I don't have sufficient permissions to do that :confused:\nThat's why I had to disable the Logger feature.\nIf you want to enable it, make sure to grant me correct permissions first :flushed:"),
    PERMISSION_NEEDED("I need {1} permission to do that."),

    BLANK(""); // leave it alone ;)

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
                if(!message.equals(Message.BLANK)) {
                    jsonWriter.name(message.getKey()).value(message.getDefaultValue());
                }
            }
            jsonWriter.endObject();
            jsonWriter.close();
            System.out.println("Saved all messages!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
