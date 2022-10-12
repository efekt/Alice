package it.efekt.alice.lang;

import com.google.gson.stream.JsonWriter;
import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public enum AMessage {
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
    CMD_ANIMECHARACTER_DESC("Sends a picture of your favourite anime character!"),
    CMD_ANIMECHARACTER_SHORT_USAGE_INFO("`character`"),
    CMD_ANIMECHARACTER_FULL_USAGE_INFO("`character` - choose one of the characters\n`list` - lists all available characters"),
    CMD_ANIMECHARACTER_CATEGORIES("Available characters:\n{1}"),
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
    CMD_RANDOMWAIFU_DESC("Shows random waifu from `https://www.thiswaifudoesnotexist.net`"),
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
    CMD_GAMESTATS_EMBED_TITLE_USER("{1}'s most played games on this server"),
    CMD_GAMESTATS_PAGE("Page {1}/{2}"),
    CMD_TOP_FOOTER("TOP-{1}"),
    CMD_WIKI_NOT_FOUND("Wikipedia article not found"),
    CMD_WIKI_MULTIPLE_FOUND("There are multiple articles associated with this phrase.\nClick the title to open wiki page in the browser."),
    CMD_WIKI_SOURCE("Source"),
    CMD_WIKI_DESC("Shows wikipedia results"),
    CMD_WIKI_SHORT_USAGE_INFO("`phrase`"),
    VOICE_NOTHING_FOUND("Nothing found"),
    VOICE_LOADING_FAILED("Loading failed"),
    VOICE_LOADING_RATE_LIMITED("Got rate limited from google. Please try again later."),
    CMD_PLAY_DESC("Plays audio from specified url or keywords"),
    CMD_PLAY_FULL_USAGE_INFO("`keywords` - searches YouTube\n`url` - plays audio from url\nexample: `play darude sandstorm`"),
    CMD_PLAY_SHORT_USAGE_INFO("`url` or `keywords`"),
    CMD_PLAY_TRACK_RESUMED("Track resumed"),
    CMD_PLAY_LOADED_AND_QUEUED("Loaded and queued...\nLately, we are experiencing some issues with being rate limited by YouTube/Google and SoundCloud\nPlease try to load a track from a different source by using `play <url>`\nSorry for the inconvenience\n"),
    CMD_PLAY_LOADED_FOOTER("{1} - shows currently playing and queued audio tracks"),
    CMD_PLAYAGAIN_DESC("Adds previously played track to the end of the queue"),
    CMD_PAUSE_DESC("Pauses currently playing track"),
    CMD_PAUSE_PAUSED("Track paused"),
    CMD_PAUSE_RESUMED("Track resumed"),
    CMD_NOWPLAYING_DESC("Shows currently playing and queued audio tracks"),
    CMD_NOWPLAYING_NOTHING("Nothing is playing"),
    CMD_NOWPLAYING_AUTHOR("Author"),
    CMD_NOWPLAYING_SOURCE("Source"),
    CMD_NOWPLAYING_TIME("Time"),
    CMD_NOWPLAYING_QUEUE("Queue:"),
    CMD_NOWPLAYING_FOOTER("{1} - skips to the next track"),
    CMD_NOWPLAYING_LEFT_IN_QUEUE("and {1} more"),
    CMD_SKIP_DESC("Skips to the next audio track"),
    CMD_SKIP_NOTHING_TO_SKIP_TO("There is nothing to skip to"),
    CMD_SKIP_SKIPPING("Skipping..."),
    CMD_LEAVE_DESC("Alice disconnects from voice channel"),
    CMD_LEAVE_NOT_CONNECTED("I was not connected in the first place! :rolling_eyes:"),
    CMD_LEAVE_LEFT("Ok, I left the channel"),
    CMD_JOIN_DESC("Alice joins the channel you are currently connected to"),
    CMD_JOIN_JOINED("Joined {1}"),
    CMD_JOIN_USER_NOT_CONNECTED("Where are you? I cannot see you on any voice channel :worried:"),
    CMD_CALC_SHORT_USAGE_INFO("`math expression`"),
    CMD_CALC_DESC("Evaluates given MATH/BOOLEAN expression\nFor support check:https://github.com/uklimaschewski/EvalEx"),
    CMD_REC_DESC("Records audio from the voice channel Alice is currently connected to.\nMaximum recording time: 5min"),
    CMD_REC_FULL_USAGE_INFO("Use the same command to stop current recording, After recording is stopped, Alice will send a .mp3 file"),
    CMD_REC_USER_NOT_CONNECTED("You need to be connected to a voice channel, otherwise I cannot join you to start recording"),
    CMD_REC_STOPPED("Recording stopped. Processing..."),
    CMD_REC_STARTED("Recording started...\nMax time of recording cannot exceed {1} minutes and will be automatically stopped"),
    CMD_REC_USERS("Recorded users:"),
    CMD_IMGONLY_DESC("Changes current channel to image-only.\nAfter this every message without picture will be deleted"),
    CMD_IMGONLY_FULL_USAGE_INFO("enables/disables feature"),
    CMD_TIMEZONE_DESC("Sets timezone"),
    CMD_TIMEZONE_SHORT_USAGE_INFO("`zone`"),
    CMD_TIMEZONE_FULL_USAGE_INFO("`zone` - your timezone \nExamples: `GMT+1` `GMT+8` `GMT-4`\nPlease use GMT based format"),
    CMD_TIMEZONE_CURRENT("Timezone: {1} \nCurrent date and time: "),
    CMD_TIMEZONE_WRONG("Wrong timezone given, please try again."),
    CMD_TIMEZONE_CHANGED("Changed timezone to: {1}"),
    CMD_VOTE_DESC("By voting once per day you will get full access to restricted commands!"),
    CMD_VOTE_RESPONSE("Do you want to vote for me? Thank you! :heart_eyes:"),
    VOTE_REQUIRED_TITLE("Please consider voting for me! :blush:"),
    VOTE_REQUIRED_INFO("This command is for voters only!\nVoting once per day will prevent this message from showing up:\n"),
    CMD_CHOOSE_DESC("Randomly chooses one of the given options"),
    CMD_CHOOSE_SHORT_USAGE_INFO("`option1` `option2` `option3` etc. (max 5)"),
    CMD_CHOOSE_MSG_MAX("You can type max up to 5 options"),
    CMD_CHOOSE_MSG_1_OPTION_GIVEN("Whoa! That was a hard one! So many options! :zany_face:"),
    CMD_OPTOUT_DESC("Opt in or out from any of the data collecting features"),
    CMD_OPTOUT_EMBED_TITLE("Your active features on this server"),
    CMD_OPTOUT_EMBED_DESC("In order to opt in/out, please type a command written below"),
    LOGGER_ALICE_NO_PERMS("I need to have Administrator or MANAGE_SERVER permission to be able to write a correct log message. Adjust my permissions or disable this feature"),
    LOGGER_CHANNEL_USER_WRONG_PERMS("This channel can only be accessible by users with Administrator or Manage Server permissions. Adjust your permissions to see log messages or disable this feature"),

    BLANK(""); // leave it alone ;)

    private String defaultValue;

    AMessage(String defaultText){
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

    public String get(CombinedCommandEvent e, String... vars){
       return getLanguage(e.getGuild()).getLanguageString(getKey()).var(vars);
    }

    public String get(MessageReceivedEvent e, String... vars){
        return getLanguage(e.getGuild()).getLanguageString(getKey()).var(vars);
    }

    public Language getLanguage(Guild e){
        String locale = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e).getLocale();
        return AliceBootstrap.alice.getLanguageManager().getLang(LangCode.valueOf(locale));
    }

    public static AMessage getDefaultMessage(String key){
        return Arrays.stream(AMessage.values()).filter(msg -> msg.getKey().equals(key)).findFirst().get();
    }

    public static void main(String[] args){
        // Generate json file with default values from this enum
        System.out.println("Generating all messages to json file");
        try {
            JsonWriter jsonWriter = new JsonWriter(new FileWriter("./en_US.json"));
            jsonWriter.beginObject();
            jsonWriter.setIndent("  ");
            for (AMessage AMessage : AMessage.values()){
                if(!AMessage.equals(AMessage.BLANK)) {
                    jsonWriter.name(AMessage.getKey()).value(AMessage.getDefaultValue());
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
