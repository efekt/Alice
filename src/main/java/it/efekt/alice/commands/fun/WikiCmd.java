package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.http.client.config.CookieSpecs;
import java.util.Set;

public class WikiCmd extends Command {
    private final String WIKIPEDIA_LOGO_URL = "https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Wikipedia-logo-v2.svg/1122px-Wikipedia-logo-v2.svg.png";
    private String lang = "en";

    public WikiCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(AMessage.CMD_WIKI_DESC);
        setShortUsageInfo(AMessage.CMD_WIKI_SHORT_USAGE_INFO);

        optionData.add(new OptionData(OptionType.STRING, "query", "query", true));
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        lang = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getLocale().split("_", 2)[0];

        if (getArgs().length >= 1){
            String query = String.join(" ", getArgs());
            JSONObject wikiPage = query(query);

            if (wikiPage == null){
                e.sendMessageToChannel(AMessage.CMD_WIKI_NOT_FOUND.get(e));
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
                desc = AMessage.CMD_WIKI_MULTIPLE_FOUND.get(e);
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(wikiPage.getString("title"), pageUrl);
            embedBuilder.setDescription(desc);
            embedBuilder.setFooter(AMessage.CMD_WIKI_SOURCE.get(e)+": Wikipedia", WIKIPEDIA_LOGO_URL);
            embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
            e.sendEmbeddedMessageToChannel(embedBuilder.build());
            return true;
        }
        return false;
    }

    private JSONObject query(String keyword){
        try {
            Unirest.primaryInstance().config().cookieSpec(CookieSpecs.IGNORE_COOKIES);
            HttpResponse<JsonNode> jsonResponse = Unirest.get("https://"+lang+".wikipedia.org/w/api.php")
                    .header("Accept", "application/json")
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
