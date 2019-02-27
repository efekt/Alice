package it.efekt.alice.commands.fun.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
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

        if (getArgs().length >= 1 && this.categories.contains(getArgs()[0])){
            String category = getArgs()[0];

            if (category.equalsIgnoreCase("neko")){
                danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "cat_ears");
                return true;
            }

            if (getArgs()[0].equalsIgnoreCase("random")){
                danbooru.sendPicture(e, DanbooruRating.EXPLICIT, "");
                return true;
            }

            if (getArgs()[0].equalsIgnoreCase("?") && !getArgs()[1].isEmpty()){
                String tag = getArgs()[1];

                danbooru.sendPicture(e, DanbooruRating.EXPLICIT, tag);
                return true;
            }


        }
        return false;
    }

    private void loadCategories(){
        this.categories.add("neko");
        this.categories.add("random");
        this.categories.add("?");
    }

}
