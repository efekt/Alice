package it.efekt.alice.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DanbooruApi {
    private final int LIMIT = 20;
    // HashMap<ServerId, List<PictureId>>
    private HashMap<String, List<Integer>> lastPictures= new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(DanbooruApi.class);

    public void sendPicture(CombinedCommandEvent event, DanbooruRating rating, String tag){
        String guildId = event.getGuild().getId();
        try {
            URL url = new URL("https://danbooru.donmai.us/posts.json?random=true&limit="+LIMIT+"&tags=rating:"+rating.getName()+"%20"+tag);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTML code 200 = OK
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

            JsonArray array = new JsonParser().parse(sb.toString()).getAsJsonArray();
            String imgUrl;
            String character;
            int imgId;
            int score;

            JsonObject bestRatingObject = null;

            for (JsonElement element : array){
                JsonObject jsonObject = element.getAsJsonObject();
                //Filtering results based on score, to return best quality picture possible
                if (jsonObject.has("file_url")){

                    if (containsId(guildId, jsonObject.get("id").getAsInt())){
                        continue;
                    }

                        score = jsonObject.get("score").getAsInt();
                        // if bestRatingObject doesn't exist, assign current one to it
                        if (bestRatingObject == null){
                            bestRatingObject = jsonObject;
                        } else {
                            // if score of current jsonObject is greater than the one that is already in bestRatingObject
                            // assign new bestRatingObject
                            if (score > bestRatingObject.get("score").getAsInt()){
                                bestRatingObject = jsonObject;
                            }
                        }
                }
            }

            // if nothing has been found (applies usually to the cases when current tag's images has been all already sent)
            if (bestRatingObject == null){
                if (this.lastPictures.containsKey(guildId)) {
                    this.lastPictures.get(guildId).clear();
                }
                event.sendMessageToChannel(AMessage.CMD_HENTAI_ERROR_NOT_FOUND.get(event));
                return;
            }

            imgUrl = bestRatingObject.get("file_url").getAsString();
            character = bestRatingObject.get("tag_string_character").getAsString();
            imgId = bestRatingObject.get("id").getAsInt();

            if (imgUrl.isEmpty()){
                event.sendMessageToChannel(AMessage.CMD_HENTAI_ERROR_NOT_FOUND.get(event));
                return;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setImage(imgUrl);
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);

            String commandSenderInfo = "["+event.getMessageString()+"]" + " requested by " + event.getUser().getName();
            character = character.isEmpty() ? "unknown" : character;

            embedBuilder.setFooter(commandSenderInfo + "\ncharacters: " + character, null);


            // add id to last retrieved img ids
            addId(guildId, imgId);
            //hearts, todo
        //    event.getChannel().sendMessage(embedBuilder.build()).queue(message -> message.addReaction(AEmoji.HEART.get()).queue());
            event.sendEmbeddedMessageToChannel(embedBuilder.build());
            try {
                event.deleteMessage();
            } catch (Exception exc){
              logger.warn("Message already deleted.");
            }
        } catch(IOException exc){
            exc.printStackTrace();
        }

    }

    private boolean containsId(String guildId, int pictureId){
        if (!this.lastPictures.containsKey(guildId)){
            return false;
        }

        if (this.lastPictures.get(guildId).contains(pictureId)){
            return true;
        }

        return false;
    }

    private void addId(String guildId, int pictureId){
        if (this.lastPictures.containsKey(guildId)){
            if (!this.lastPictures.get(guildId).contains(pictureId)){
                this.lastPictures.get(guildId).add(pictureId);
            }
        } else {
            List<Integer> newList = new ArrayList<>();
            newList.add(pictureId);
            this.lastPictures.put(guildId, newList);
        }
    }
}
