package it.efekt.alice.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SankakuApi {
    private final int LIMIT = 20;
    // HashMap<ServerId, List<PictureId>>
    private HashMap<String, List<Integer>> lastPictures= new HashMap<>();

    public void sendPicture(MessageReceivedEvent event, DanbooruRating rating, String tag){
        String guildId = event.getGuild().getId();
        try {
            URL url = new URL("https://iapi.sankakucomplex.com/post/index.json?random=true&limit="+LIMIT+"&tags=rating:"+rating.getName()+"%20"+tag);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

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
           // String character;
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

                        score = jsonObject.get("total_score").getAsInt();
                        // if bestRatingObject doesn't exist, assign current one to it
                        if (bestRatingObject == null){
                            bestRatingObject = jsonObject;
                        } else {
                            // if score of current jsonObject is greater than the one that is already in bestRatingObject
                            // assign new bestRatingObject
                            if (score > bestRatingObject.get("total_score").getAsInt()){
                                bestRatingObject = jsonObject;
                            }
                        }
                }
            }

            // if nothing has been found (applies usually to the cases when current tag's images has been all already sent)
            if (bestRatingObject == null){
                this.lastPictures.get(guildId).clear();
                event.getChannel().sendMessage(AMessage.CMD_HENTAI_ERROR_NOT_FOUND.get(event)).queue();
                return;
            }

            imgUrl = bestRatingObject.get("file_url").getAsString();
            //character = bestRatingObject.get("tag_string_character").getAsString();
            imgId = bestRatingObject.get("id").getAsInt();

            if (imgUrl.isEmpty()){
                event.getChannel().sendMessage(AMessage.CMD_HENTAI_ERROR_NOT_FOUND.get(event)).queue();
                return;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setImage(imgUrl);
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
//            if (!character.isEmpty()){
//                embedBuilder.setFooter(character, null);
//            }

            // add id to last retrieved img ids
            addId(guildId, imgId);
            //hearts, todo
        //    event.getChannel().sendMessage(embedBuilder.build()).queue(message -> message.addReaction(AEmoji.HEART.get()).queue());
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
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
