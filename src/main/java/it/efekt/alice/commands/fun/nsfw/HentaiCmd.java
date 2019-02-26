package it.efekt.alice.commands.fun.nsfw;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pw.aru.api.nekos4j.Nekos4J;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HentaiCmd extends Command {
    private Nekos4J api = new Nekos4J.Builder().build();
    private ImageProvider imageProvider = api.getImageProvider();
    private HashMap<String, List<String>> categories = new HashMap<>();

    public HentaiCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_HENTAI_DESC);
        setNsfw(true);
        setShortUsageInfo(Message.CMD_HENTAI_SHORT_USAGE_INFO);
        setCategory(CommandCategory.FUN);
        loadCategories();
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        if (getArgs().length == 1 ){

            if (getArgs()[0].equalsIgnoreCase("random")){
                hPicture(e);
                return true;
            }

            if (!this.categories.containsKey(getArgs()[0])){
                e.getChannel().sendMessage(Message.CMD_HENTAI_CATEGORY_UNKNOWN.get(e, categories.keySet().toString())).queue();
                return true;
            }

            Random random = new Random();
            int randomPic = random.nextInt(categories.get(getArgs()[0]).size());
            Future<Image> imageFuture = imageProvider.getRandomImage(categories.get(getArgs()[0]).get(randomPic)).submit();
                try {
                    Image image = imageFuture.get();
                    String imageUrl = image.getUrl();

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
                    embedBuilder.setImage(imageUrl);
                    e.getChannel().sendMessage(embedBuilder.build()).queue();
                    return true;
                } catch (InterruptedException| ExecutionException e1) {
                    e.getChannel().sendMessage(Message.CMD_HENTAI_NOT_FOUND.get(e)).queue();
                    return true;
                }
        } else {
            e.getChannel().sendMessage(Message.CMD_HENTAI_CHECK_COMMAND + " " + getGuildPrefix(e.getGuild()) + getAlias() + getShortUsageInfo()).queue();
            return true;
        }
    }

    private void loadCategories(){
        List<String> nekoTags = new ArrayList<>();
        nekoTags.add("lewdk");
        nekoTags.add("ngif");
        nekoTags.add("lewdkemo");
        nekoTags.add("erokemo");
        nekoTags.add("eron");
        categories.put("neko",nekoTags);
        categories.put("gifneko", Collections.singletonList("ngif"));
    }

    private void hPicture(MessageReceivedEvent event){

        try {
            URL url = new URL("https://danbooru.donmai.us/posts.json?random=true&limit=5&tags=censored");
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
            String imgUrl = "";
            String character = "";

            for (JsonElement element : array){
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has("file_url")){
                     imgUrl = jsonObject.get("file_url").getAsString();
                     character = jsonObject.get("tag_string_character").getAsString();
                    break;
                }
            }


            if (imgUrl == ""){
                event.getChannel().sendMessage(Message.CMD_HENTAI_ERROR_NOT_FOUND.get(event)).queue();
                return;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setImage(imgUrl);
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
            if (character != ""){
                embedBuilder.setFooter(character, null);
            }

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch(IOException exc){
            exc.printStackTrace();
        }

    }
}
