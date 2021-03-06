package it.efekt.alice.commands.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NekoCmd extends Command {
    DanbooruApi danbooru = new DanbooruApi();

    public NekoCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_NEKO_DESC);
        setCategory(CommandCategory.NSFW);
        setNsfw(true);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        danbooru.sendPicture(e, DanbooruRating.SAFE, "cat_girl");
        return true;
    }
}
