package it.efekt.alice.commands.fun;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApexStatsCmd extends Command {
    private final String PC_STRING = "pc";
    private final String PSN_STRING = "psn";
    private final String XBOX_STRING = "xbl";

    public ApexStatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.GAMES);
        setShortUsageInfo(Message.CMD_APEX_SHORT_USAGE_INFO);
        setFullUsageInfo(Message.CMD_APEX_FULL_USAGE_INFO);
        setDescription(Message.CMD_APEX_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        if (getArgs().length == 2){
            String platform = getArgs()[0];

            if (platform.equalsIgnoreCase(PC_STRING) || platform.equalsIgnoreCase(PSN_STRING) || platform.equalsIgnoreCase(XBOX_STRING)){
                String playerName = getArgs()[1];

                if (getPlayerInfo(playerName, platform) == null || !getPlayerInfo(playerName, platform).get("playerfound").getAsBoolean()){
                    e.getChannel().sendMessage(Message.CMD_APEX_PLAYER_NOT_FOUND.get(e)).complete();
                    return true;
                }

                JsonObject playerInfo = getPlayerInfo(playerName, platform);
                String name = playerInfo.get("name").getAsString();
                String avatarUrl = playerInfo.get("avatar").getAsString();
                int level = playerInfo.get("level").getAsInt();
                int headshots = playerInfo.get("headshots").getAsInt();
                int kills = playerInfo.get("kills").getAsInt();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
                embedBuilder.setTitle(Message.CMD_APEX_EMBED_TITLE.get(e) + " " + platform.toUpperCase());
                embedBuilder.setDescription(Message.CMD_APEX_EMBED_DESC.get(e));
                embedBuilder.setThumbnail(avatarUrl);
                embedBuilder.addField(Message.CMD_APEX_PLAYERNAME.get(e), name, true);
                embedBuilder.addField(Message.CMD_APEX_LEVEL.get(e), String.valueOf(level), true);
                embedBuilder.addField(Message.CMD_APEX_KILLS.get(e), String.valueOf(kills), true);
                embedBuilder.addField(Message.CMD_APEX_HEADSHOTS.get(e), String.valueOf(headshots), true);
                embedBuilder.addBlankField(false);
                embedBuilder.addField("Bloodhound", "\n" + Message.CMD_APEX_MATCHES.get(e) + " " + playerInfo.get("matches_Bloodhound").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Bloodhound").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Bloodhound").getAsString()
                        , true);
                embedBuilder.addField("Gibraltar", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Gibraltar").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Gibraltar").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Gibraltar").getAsString()
                        , true);
                embedBuilder.addField("Lifeline", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Lifeline").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Lifeline").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Lifeline").getAsString()
                        , true);
                embedBuilder.addField("Pathfinder", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Pathfinder").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Pathfinder").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Pathfinder").getAsString()
                        , true);
                embedBuilder.addField("Wraith", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Wraith").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Wraith").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Wraith").getAsString()
                        , true);
                embedBuilder.addField("Bangalore", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Bangalore").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Bangalore").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Bangalore").getAsString()
                        , true);
                embedBuilder.addField("Caustic", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Caustic").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Caustic").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Caustic").getAsString()
                        , true);
                embedBuilder.addField("Mirage", "\n" + Message.CMD_APEX_MATCHES.get(e) + " "  + playerInfo.get("matches_Mirage").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_KILLS.get(e) + playerInfo.get("kills_Mirage").getAsString() +
                                "\n" + Message.CMD_APEX_LEGEND_HEADSHOTS.get(e) + playerInfo.get("headshots_Mirage").getAsString()
                        , true);
                e.getChannel().sendMessage(embedBuilder.build()).complete();
                return true;

            }
        }
        return false;
    }


    private JsonObject getPlayerInfo(String playerName, String platform){
        return getPlayer(getPlayerId(playerName, platform));
    }

    private JsonObject getPlayer(String playerId){
        JsonObject playerObject = new JsonObject();
        try {
            URL url = new URL("https://apextab.com/api/player.php?aid=" + playerId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            if (connection.getResponseCode() != 200){
                throw new IOException(connection.getResponseMessage());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            reader.close();
            connection.disconnect();

             playerObject = new JsonParser().parse(sb.toString()).getAsJsonObject();
        } catch (IOException exc){
            exc.printStackTrace();
        }


        return playerObject;
    }

    private String getPlayerId(String playerName, String platform){
        String playerId = null;

        try {
            URL url = new URL("https://apextab.com/api/search.php?platform="+platform+"&search=" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            if (connection.getResponseCode() != 200){
                throw new IOException(connection.getResponseMessage());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            reader.close();
            connection.disconnect();

            JsonObject jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();

            if (jsonObject.get("totalresults").getAsInt() == 0){
                return playerId;
            }

            playerId = jsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject().get("aid").getAsString();
        } catch (IOException exc){
            exc.printStackTrace();
        }

        return playerId;
    }

}
