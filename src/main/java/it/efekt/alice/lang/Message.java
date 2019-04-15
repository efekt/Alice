package it.efekt.alice.lang;

import com.google.gson.stream.JsonWriter;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.entities.Guild;
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
    CMD_HELP_FOOTER("[command] - shows command's help"),
    CMD_HENTAI_DESC("Watch out! Uncharted waters!"),
    CMD_HENTAI_SHORT_USAGE_INFO("`category`"),
    CMD_HENTAI_FULL_USAGE_INFO("`category` - choose one of the categories\n`list` - lists all categories"),
    CMD_HENTAI_CATEGORIES("Available categories:\n{1}"),
    CMD_HENTAI_NOT_FOUND("Not found"),
    CMD_HENTAI_CHECK_COMMAND("Proper command usage:"),
    CMD_HENTAI_ERROR_NOT_FOUND("Nothing has been found :worried:"),
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
    CMD_HISTORYDEL_DESC("Deletes last n messages on the channel\n min: 1, max: 99"),
    CMD_HISTORYDEL_RANGE("I can delete maximum of 99 messages at a time"),
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
    CMD_TOP_USAGE_INFO("`page`"),
    CMD_TOP_MINUS_TOP("u mad...?"),
    CMD_TOP_NUMBER_TOO_LARGE("Couldn't find so many users. You can specified max {1}"),
    CMD_TOP_LOADALL_FORBIDDEN("Only Admin can load all messages"),
    CMD_TOP_FULL_USAGE_INFO("`number of users` - optional"),
    CMD_TOP_LOADALL_WARNING("Trying to gather all messages ever written.\nIt may last a couple of seconds/minutes depending of server size :fearful:"),
    CMD_TOP_LOADALL_FOUND("Found {1} messages, processing..."),
    CMD_TOP_LOADALL_FINISHED("Processing finished, saving to database..."),
    CMD_TOP_LOADALL_SAVED("All results has been saved :heart_eyes:"),
    CMD_TOP_LIST_TITLE("Ranking of the best {1}'s spammers"),
    CMD_TOP_WRONG_PAGE("Wrong page given, check if your page is within range: 1 - {1}"),
    CMD_TOP_NOTHING_FOUND("Couldn't find anything, give me some time and try again later"),
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
    LOGGER_USER_JOINS_GUILD("{1} joins the server"),
    LOGGER_USER_LEAVES_GUILD("{1} leaves the server"),
    LOGGER_USER_CHANGES_STATUS("{1} changes status to: {2}"),
    LOGGER_USER_JOINS_VOICE("{1} joins {2} channel"),
    LOGGER_USER_LEAVES_VOICE("{1} leaves {2} channel"),
    LOGGER_USER_SWITCHES_VOICE("{1} changes {2} channel to {3}"),
    LOGGER_USER_STOPPED_PLAYING("{1} exits {2}, {3}min"),
    CMD_GAMESTATS_DESC("Shows the most played games on this discord server"),
    CMD_GAMESTATS_EMBED_TITLE("Most played games on this server"),
    CMD_GAMESTATS_PAGE("Page {1}/{2}"),
    CMD_TOP_FOOTER("TOP-{1}"),
    CMD_WIKI_NOT_FOUND("Wikipedia article not found"),
    CMD_WIKI_MULTIPLE_FOUND("There are multiple articles associated with this phrase.\nClick the title to open wiki page in the browser."),
    CMD_WIKI_SOURCE("Source"),
    CMD_WIKI_DESC("Shows wikipedia results"),
    CMD_WIKI_SHORT_USAGE_INFO("`phrase`"),
    VOICE_NOTHING_FOUND("Nothing found"),
    VOICE_LOADING_FAILED("Loading failed"),
    CMD_PLAY_DESC("Plays audio from specified url"),
    CMD_PLAY_SHORT_USAGE_INFO("`url`"),
    CMD_PLAY_TRACK_RESUMED("Track resumed"),
    CMD_PAUSE_DESC("Pauses currently playing track"),
    CMD_PAUSE_PAUSED("Track paused"),
    CMD_PAUSE_RESUMED("Track resumed"),
    CMD_NOWPLAYING_DESC("Shows currently playing track"),
    CMD_NOWPLAYING_NOTHING("Nothing is playing"),
    CMD_NOWPLAYING_AUTHOR("Author"),
    CMD_NOWPLAYING_SOURCE("Source"),
    CMD_NOWPLAYING_TIME("Time"),
    CMD_LEAVE_DESC("Alice disconnects from voice channel"),
    CMD_LEAVE_NOT_CONNECTED("I was not connected in the first place! :rolling_eyes:"),
    CMD_LEAVE_LEFT("Ok, I left the channel"),
    CMD_JOIN_DESC("Alice joins the channel you are currently connected to"),
    CMD_JOIN_JOINED("Joined {1}"),
    CMD_JOIN_USER_NOT_CONNECTED("Where are you? I cannot see you on any voice channel :worried:"),
    CMD_EVAL_SHORT_USAGE_INFO("`expression`"),
    CMD_EVAL_DESC("Evaluates given expression\nExample:\n `eval 1+1` returns `2`\nFor supported operators check: https://github.com/uklimaschewski/EvalEx"),
    CMD_REC_DESC("Records audio from the voice channel Alice is currently connected to.\nMaximum recording time: 5min"),
    CMD_REC_FULL_USAGE_INFO("Use the same command to stop current recording, After recording is stopped, Alice will send a .mp3 file"),
    CMD_REC_USER_NOT_CONNECTED("You need to be connected to a voice channel, otherwise I cannot join you to start recording"),
    CMD_REC_STOPPED("Recording stopped. Processing..."),
    CMD_REC_STARTED("Recording started...\nMax time of recording cannot exceed {1} minutes and will be automatically stopped"),
    CMD_REC_USERS("Recorded users:"),

    BLANK(""); // leave it alone ;)

    private String defaultValue;

    Message(String defaultText){
        this.defaultValue = defaultText;
    }

    public String getKey(){
        return this.name().replace("_", "-").toLowerCase();
    }

    public String getDefaultValue(){
        return this.defaultValue;
    }

    public String get(Guild guild, String... vars){
        LangCode langCode = LangCode.valueOf(AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(guild).getLocale());
        Language language = AliceBootstrap.alice.getLanguageManager().getLang(langCode);
        return get(language, vars);
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
