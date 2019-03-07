package it.efekt.alice.commands.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

public class WikiCmd extends Command {
    private final String WIKIPEDIA_LOGO_URL = "https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Wikipedia-logo-v2.svg/1122px-Wikipedia-logo-v2.svg.png";
    private String lang = "en";

    public WikiCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(Message.CMD_WIKI_DESC);
        setShortUsageInfo(Message.CMD_WIKI_SHORT_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        lang = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getLocale().split("_", 2)[0];

        if (getArgs().length >= 1){
            String query = Arrays.toString(getArgs()).replaceAll(",", "").replaceAll("]", "").replaceAll("\\[", "");
            JSONObject wikiPage = query(query);

            if (wikiPage == null){
                e.getChannel().sendMessage(Message.CMD_WIKI_NOT_FOUND.get(e)).complete();
                return true;
            }

            String pageUrl = "https://"+lang+".wikipedia.org/?curid="+ wikiPage.getLong("pageid");
            String desc = wikiPage.getString("extract");
            if (desc.length() >= 1024){
                desc = desc.split("\n", 2)[0].concat("...");
            }

            if (desc.length() >= 1024){
                desc = desc.substring(0, 1000).concat("...");
            }



            if (wikiPage.getJSONObject("pageprops").has("disambiguation")){
                desc = Message.CMD_WIKI_MULTIPLE_FOUND.get(e);
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(wikiPage.getString("title"), pageUrl);
            embedBuilder.setDescription(desc);
            embedBuilder.setFooter(Message.CMD_WIKI_SOURCE.get(e)+": Wikipedia", WIKIPEDIA_LOGO_URL);
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
            e.getChannel().sendMessage(embedBuilder.build()).complete();
            return true;
        }
        return false;
    }

    private JSONObject query(String keyword){
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("https://"+lang+".wikipedia.org/w/api.php")
                    .header("accept", "application/json")
                    .queryString("action", "query")
                    .queryString("format", "json")

                    .queryString("prop", "extracts|pageprops")
                    .queryString("exintro", "")
                    .queryString("explaintext", "")
                    .queryString("redirects", "1")
                    .queryString("titles", keyword)
                    .asJson();
            Set<String> pages = jsonResponse.getBody().getObject().getJSONObject("query").getJSONObject("pages").keySet();
            if (pages.contains("-1")){
                return null;
            }

            JSONObject page = jsonResponse.getBody().getObject().getJSONObject("query").getJSONObject("pages").getJSONObject(pages.toArray()[0].toString());
            return page;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
