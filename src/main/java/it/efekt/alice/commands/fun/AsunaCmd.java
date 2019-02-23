package it.efekt.alice.commands.fun;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsunaCmd extends Command {

    public AsunaCmd(String alias) {
        super(alias);
        setDescription("Wyswietla losowe zdjecie Asuny");
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String imgurClientId = AliceBootstrap.alice.getConfig().getImgurClientId();
        try {
            URL url = new URL("https://api.imgur.com/3/album/l5sPP/images");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Client-Id " + imgurClientId);

            if (conn.getResponseCode() != 200){
                throw new IOException(conn.getResponseMessage());
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            conn.disconnect();

            JsonObject obj = new JsonParser().parse(sb.toString()).getAsJsonObject();
            JsonArray array = obj.getAsJsonArray("data");

            List<String> urls = new ArrayList<>();

            for (JsonElement element : array){
                JsonObject object = element.getAsJsonObject();
                urls.add(object.get("link").getAsString());
            }

            Random rand = new Random();
            embedBuilder.setImage(urls.get(rand.nextInt(urls.size())));
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
            e.getChannel().sendMessage(embedBuilder.build()).queue();
            return true;
        } catch(IOException exc){
            exc.printStackTrace();
        }
        return false;
    }
}
