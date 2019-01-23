package it.efekt.alice.commands.fun.nsfw;

import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pw.aru.api.nekos4j.Nekos4J;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

public class NekoCmd extends Command {
    public NekoCmd(String alias) {
        super(alias);
        setDescription("Nyaaaaaa!");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        Nekos4J api = new Nekos4J.Builder().build();
        ImageProvider imageProvider = api.getImageProvider();
        Image image = imageProvider.getRandomImage("neko").execute();

            String imageUrl = image.getUrl();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setImage(imageUrl);
            e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
