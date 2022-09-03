package it.efekt.alice.commands.nsfw;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class HentaiCmd extends Command {
    private List<String> categories = new ArrayList<>();
    private DanbooruApi danbooru = new DanbooruApi();

    public HentaiCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_HENTAI_DESC);
        setNsfw(true);
        setShortUsageInfo(AMessage.CMD_HENTAI_SHORT_USAGE_INFO);
        setFullUsageInfo(AMessage.CMD_HENTAI_FULL_USAGE_INFO);
        setCategory(CommandCategory.NSFW);
        setIsVoteRequired(true);
        loadCategories();

        OptionData data = new OptionData(OptionType.STRING, "category", "category", true);
//        for(String s: categories) data.addChoice(s, s); //too many for this max is 25
//        data.addChoice("list", "list");
        optionData.add(data);
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {

        if (getArgs().length >= 1){
            String category = getArgs()[0].toLowerCase();

            // dev version, custom characters
            if (category.equalsIgnoreCase("dev") && getArgs().length == 2){
                danbooru.sendPicture(e, DanbooruRating.EXPLICIT, getArgs()[1]);
                return true;
            }

            if (category.equalsIgnoreCase("list") || !this.categories.contains(getArgs()[0].toLowerCase())){
                e.sendMessageToChannel(AMessage.CMD_HENTAI_CATEGORIES.get(e,getCategoriesString()));
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
                    case "rin":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "toosaka_rin");
                        return true;
                    case "gray":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "gray_(lord_el-melloi_ii)");
                        return true;
                    case "ishtar":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "ishtar_(fate/grand_order)");
                        return true;
                    case "mordred":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "mordred_(fate)_(all)");
                        return true;
                    case "2b":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "yorha_no._2_type_b");
                        return true;
                    case "a2":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "yorha_type_a_no._2");
                        return true;
                    case "kallen":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "kallen_stadtfeld");
                        return true;
                    case "playstation":
                        danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "playstation_5");
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
        this.categories.add("rin");
        this.categories.add("saber");
        this.categories.add("megumin");
        this.categories.add("gray");
        this.categories.add("ishtar");
        this.categories.add("mordred");
        this.categories.add("christmas");
        this.categories.add("2b");
        this.categories.add("a2");
        this.categories.add("c.c.");
        this.categories.add("kallen");
        this.categories.add("playstation");
    }

    private String getCategoriesString(){
        return this.categories.toString().replace("]", "`").replace("[", "`").replace(" ", " `").replace(",", "`");
    }

}
