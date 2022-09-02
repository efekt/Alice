package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KojimaCmd extends Command {
    public KojimaCmd(String alias) {
        super(alias);
        setDescription(AMessage.CMD_KOJIMA_DESC);
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        e.getChannel().sendMessageEmbeds(new EmbedBuilder().setImage("https://i.imgur.com/18C9F73.jpg").setColor(AliceBootstrap.EMBED_COLOR).build()).complete();
        return true;
    }
}
