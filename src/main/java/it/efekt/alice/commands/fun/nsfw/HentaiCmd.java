package it.efekt.alice.commands.fun.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.util.*;

public class HentaiCmd extends Command {
    List<String> categories = new ArrayList<>();
    private DanbooruApi danbooru = new DanbooruApi();

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

        if (getArgs().length >= 1 && this.categories.contains(getArgs()[0].toLowerCase())){
            String category = getArgs()[0].toLowerCase();

            switch(category){
                case "random":
                    danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "");
                    return true;
                case "neko":
                    danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "cat_girl");
                    return true;
                case "alice":
                    danbooru.sendPicture(e, DanbooruRating.QUESTIONABLE, "alice_schuberg");
                    return true;
                default:
                    return false;
            }


        }
        return false;
    }

    private void loadCategories(){
        this.categories.add("random");
        this.categories.add("neko");
        this.categories.add("alice");
    }

}
