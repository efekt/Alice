package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class KojimaCmd extends Command {
    public KojimaCmd(String alias) {
        super(alias);
        setDescription("Kojumbo");
        setCategory(CommandCategory.FUN);
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage(new EmbedBuilder().setImage("https://i.imgur.com/18C9F73.jpg").setColor(AliceBootstrap.EMBED_COLOR).build()).queue();
    }
}
