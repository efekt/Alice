package it.efekt.alice.commands.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.util.ArrayList;
import java.util.List;

public class AnimeCharacterCmd extends Command {
    private List<String> categories = new ArrayList<>();
    private DanbooruApi danbooru = new DanbooruApi();

    public AnimeCharacterCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_ANIMECHARACTER_DESC);
        setNsfw(true);
        setShortUsageInfo(Message.CMD_ANIMECHARACTER_SHORT_USAGE_INFO);
        setFullUsageInfo(Message.CMD_ANIMECHARACTER_FULL_USAGE_INFO);
        setCategory(CommandCategory.NSFW);
        loadCategories();
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        if (getArgs().length >= 1){
            String category = getArgs()[0].toLowerCase();
            if (category.equalsIgnoreCase("list") || !this.categories.contains(getArgs()[0].toLowerCase())){
                e.getChannel().sendMessage(Message.CMD_ANIMECHARACTER_CATEGORIES.get(e,getCategoriesString())).complete();
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
                        danbooru.sendPicture(e, DanbooruRating.SAFE, "jeanne_d'arc_(fate)_(all)");
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
    }

    private String getCategoriesString(){
        return this.categories.toString().replace("]", "`").replace("[", "`").replace(" ", " `").replace(",", "`");
    }

}
