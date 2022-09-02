package it.efekt.alice.commands.nsfw;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.ArrayList;
import java.util.List;

public class AnimeCharacterCmd extends Command {
    private List<String> categories = new ArrayList<>();
    private DanbooruApi danbooru = new DanbooruApi();

    public AnimeCharacterCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_ANIMECHARACTER_DESC);
        setNsfw(true);
        setShortUsageInfo(AMessage.CMD_ANIMECHARACTER_SHORT_USAGE_INFO);
        setFullUsageInfo(AMessage.CMD_ANIMECHARACTER_FULL_USAGE_INFO);
        setCategory(CommandCategory.NSFW);
        setIsVoteRequired(true);
        loadCategories();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {

        if (getArgs().length >= 1){
            String category = getArgs()[0].toLowerCase();

            // dev version, custom characters
            if (category.equalsIgnoreCase("dev") && getArgs().length == 2){
                danbooru.sendPicture(e, DanbooruRating.SAFE, getArgs()[1]);
                return true;
            }

            if (category.equalsIgnoreCase("list") || !this.categories.contains(getArgs()[0].toLowerCase())){
                e.getChannel().sendMessage(AMessage.CMD_ANIMECHARACTER_CATEGORIES.get(e,getCategoriesString())).complete();
                return true;
            }

            if (this.categories.contains(getArgs()[0].toLowerCase())) {
                switch (category) {
                    case "random":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "");
                        return true;
                    case "alice":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "alice_schuberg");
                        return true;
                    case "rin":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "toosaka_rin");
                        return true;
                    case "gray":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "gray_(lord_el-melloi_ii)");
                        return true;
                    case "ishtar":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "ishtar_(fate/grand_order)");
                        return true;
                    case "jeanne":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "jeanne_d'arc_(fate)");
                        return true;
                    case "senko":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "senko_(sewayaki_kitsune_no_senko-san)");
                        return true;
                    case "asuna":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "asuna_(sao)");
                        return true;
                    case "yue":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "yue_(arifureta)");
                        return true;
                    case "asuka":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "souryuu_asuka_langley");
                        return true;
                    case "violet_evergarden":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "violet_evergarden_(character)");
                        return true;
                    case "ichigo":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "ichigo_(darling_in_the_franxx)");
                        return true;
                    case "zero_two":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "zero_two_(darling_in_the_franxx)");
                        return true;
                    case "mordred":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "mordred_(fate)_(all)");
                        return true;
                    case "2b":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "yorha_no._2_type_b");
                        return true;
                    case "a2":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "yorha_type_a_no._2");
                        return true;
                    case "kallen":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "kallen_stadtfeld");
                        return true;
                    case "playstation":
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "playstation_5");
                        return true;
                    default:
                        danbooru.sendPicture(e, DanbooruRating.SAFE, category);
                        return true;
                }
            }
        }
        return false;
    }

    private void loadCategories(){
        this.categories.add("random");
        this.categories.add("alice");
        this.categories.add("rin");
        this.categories.add("saber");
        this.categories.add("megumin");
        this.categories.add("gray");
        this.categories.add("ishtar");
        this.categories.add("jeanne");
        this.categories.add("senko");
        this.categories.add("asuna");
        this.categories.add("sinon");
        this.categories.add("yue");
        this.categories.add("asuka");
        this.categories.add("ayanami_rei");
        this.categories.add("makise_kurisu");
        this.categories.add("hatori_chise");
        this.categories.add("christa_renz");
        this.categories.add("mikasa_ackerman");
        this.categories.add("kamado_nezuko");
        this.categories.add("seras_victoria");
        this.categories.add("chitanda_eru");
        this.categories.add("mori_yuki");
        this.categories.add("hinazuki_kayo");
        this.categories.add("mori_yuki");
        this.categories.add("violet_evergarden");
        this.categories.add("shana");
        this.categories.add("kusanagi_motoko");
        this.categories.add("watanabe_saki");
        this.categories.add("miyazono_kawori");
        this.categories.add("kaname_madoka");
        this.categories.add("akemi_homura");
        this.categories.add("miki_sayaka");
        this.categories.add("sakura_kyouko");
        this.categories.add("tomoe_mami");
        this.categories.add("ichigo");
        this.categories.add("hatsune_miku");
        this.categories.add("zero_two");
        this.categories.add("mordred");
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
