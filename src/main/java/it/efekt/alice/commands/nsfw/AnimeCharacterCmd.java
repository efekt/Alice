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
    }

    private String getCategoriesString(){
        return this.categories.toString().replace("]", "`").replace("[", "`").replace(" ", " `").replace(",", "`");
    }

}
