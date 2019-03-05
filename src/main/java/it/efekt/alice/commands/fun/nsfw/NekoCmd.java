package it.efekt.alice.commands.fun.nsfw;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.DanbooruApi;
import it.efekt.alice.modules.DanbooruRating;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NekoCmd extends Command {
    DanbooruApi danbooru = new DanbooruApi();

    public NekoCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_NEKO_DESC);
        setCategory(CommandCategory.NSFW);
        setNsfw(true);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        danbooru.sendPicture(e, DanbooruRating.SAFE, "cat_girl");
        return true;
    }
}
