package it.efekt.alice.commands.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.util.*;

public class HentaiCmd extends Command {
    private List<String> categories = new ArrayList<>();
    private DanbooruApi danbooru = new DanbooruApi();

    public HentaiCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_HENTAI_DESC);
        setNsfw(true);
        setShortUsageInfo(Message.CMD_HENTAI_SHORT_USAGE_INFO);
        setFullUsageInfo(Message.CMD_HENTAI_FULL_USAGE_INFO);
        setCategory(CommandCategory.NSFW);
        loadCategories();
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        if (getArgs().length >= 1){
            String category = getArgs()[0].toLowerCase();
            if (category.equalsIgnoreCase("list") || !this.categories.contains(getArgs()[0].toLowerCase())){
                e.getChannel().sendMessage(Message.CMD_HENTAI_CATEGORIES.get(e,getCategoriesString())).complete();
                return true;
            }

            if (this.categories.contains(getArgs()[0].toLowerCase())) {
                switch (category) {
                    case "random":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "");
                        return true;
                    case "neko":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "cat_girl");
                        return true;
                    case "alice":
                        danbooru.sendPicture(e, DanbooruRating.QUESTIONABLE, "alice_schuberg");
                        return true;
                    case "pantyhose":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "black_legwear");
                        return true;
                    case "6+girls":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "6%2Bgirls");
                        return true;
                    default:
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, category);
                        return true;
                }
            }
        }
        return false;
    }

    private void loadCategories(){
        this.categories.add("random");
        this.categories.add("neko");
        this.categories.add("alice");
        this.categories.add("highres");
        this.categories.add("cosplay");
        this.categories.add("video_game");
        this.categories.add("pantyhose");
        this.categories.add("thighhighs");
        this.categories.add("ahegao");
        this.categories.add("blush");
        this.categories.add("smile");
        this.categories.add("open_mouth");
        this.categories.add("long_hair");
        this.categories.add("very_long_hair");
        this.categories.add("blonde_hair");
        this.categories.add("black_hair");
        this.categories.add("blue_hair");
        this.categories.add("silver_hair");
        this.categories.add("white_hair");
        this.categories.add("multicolored_hair");
        this.categories.add("twintails");
        this.categories.add("fang");
        this.categories.add("blue_eyes");
        this.categories.add("red_eyes");
        this.categories.add("green_eyes");
        this.categories.add("purple_eyes");
        this.categories.add("yellow_eyes");
        this.categories.add("one_eye_closed");
        this.categories.add("skirt");
        this.categories.add("hat");
        this.categories.add("large_breasts");
        this.categories.add("1girl");
        this.categories.add("2girls");
        this.categories.add("3girls");
        this.categories.add("4girls");
        this.categories.add("5girls");
        this.categories.add("6+girls");
        this.categories.add("gloves");
        this.categories.add("fingerless_gloves");
        this.categories.add("long_sleeves");
        this.categories.add("dress");
        this.categories.add("bow");
        this.categories.add("bangs");
        this.categories.add("underwear");
        this.categories.add("shirt");
        this.categories.add("swimsuit");
        this.categories.add("bikini");
        this.categories.add("detached_sleeves");
        this.categories.add("food");
        this.categories.add("tears");
        this.categories.add("tongue");
        this.categories.add("idolmaster");
        this.categories.add("uniform");
        this.categories.add("flat_chest");
        this.categories.add("shiny");
        this.categories.add("armor");
        this.categories.add("high_heels");
        this.categories.add("gun");
    }

    private String getCategoriesString(){
        return this.categories.toString().replaceAll("\\]", "`").replaceAll("\\[", "`").replaceAll(" ", " `").replaceAll(",", "`");
    }

}
