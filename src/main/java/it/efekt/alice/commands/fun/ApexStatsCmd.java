package it.efekt.alice.commands.fun;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
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
        setCategory(CommandCategory.FUN);
        setShortUsageInfo(" `pc/psn/xbl` `nick`");
        setFullUsageInfo("`pc/psn/xbl` - platforma, wybierz jedną\n `nick` - nazwa użytkownika Apex");
        setDescription("Wyświetla statystyki z gry Apex (Eksperymentalne)");
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        if (getArgs().length == 2){
            String platform = getArgs()[0];

            if (platform.equalsIgnoreCase(PC_STRING) || platform.equalsIgnoreCase(PSN_STRING) || platform.equalsIgnoreCase(XBOX_STRING)){
                String playerName = getArgs()[1];

                if (getPlayerInfo(playerName, platform) == null || !getPlayerInfo(playerName, platform).get("playerfound").getAsBoolean()){
                    e.getChannel().sendMessage("Nie znaleziono takiego gracza").queue();
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
                embedBuilder.setTitle("APEX - Statystyki (Eksperymentalne) " + platform.toUpperCase());
                embedBuilder.setDescription("Niektóre wartości mogą się nie zgadzać lub zupełnie brakować");
                embedBuilder.setThumbnail(avatarUrl);
                embedBuilder.addField("Nazwa gracza", name, true);
                embedBuilder.addField("Poziom", String.valueOf(level), true);
                embedBuilder.addField("Zabójstwa", String.valueOf(kills), true);
                embedBuilder.addField("Strzały w głowę", String.valueOf(headshots), true);
                embedBuilder.addBlankField(false);
                embedBuilder.addField("Bloodhound", "\nMecze: " + playerInfo.get("matches_Bloodhound").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Bloodhound").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Bloodhound").getAsString()
                        , true);
                embedBuilder.addField("Gibraltar", "\nMecze: " + playerInfo.get("matches_Gibraltar").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Gibraltar").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Gibraltar").getAsString()
                        , true);
                embedBuilder.addField("Lifeline", "\nMecze: " + playerInfo.get("matches_Lifeline").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Lifeline").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Lifeline").getAsString()
                        , true);
                embedBuilder.addField("Pathfinder", "\nMecze: " + playerInfo.get("matches_Pathfinder").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Pathfinder").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Pathfinder").getAsString()
                        , true);
                embedBuilder.addField("Wraith", "\nMecze: " + playerInfo.get("matches_Wraith").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Wraith").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Wraith").getAsString()
                        , true);
                embedBuilder.addField("Bangalore", "\nMecze: " + playerInfo.get("matches_Bangalore").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Bangalore").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Bangalore").getAsString()
                        , true);
                embedBuilder.addField("Caustic", "\nMecze: " + playerInfo.get("matches_Caustic").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Caustic").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Caustic").getAsString()
                        , true);
                embedBuilder.addField("Mirage", "\nMecze: " + playerInfo.get("matches_Mirage").getAsString() +
                                "\nZabójstwa: " + playerInfo.get("kills_Mirage").getAsString() +
                                "\nHeadshoty: " + playerInfo.get("headshots_Mirage").getAsString()
                        , true);




                e.getChannel().sendMessage(embedBuilder.build()).queue();
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
