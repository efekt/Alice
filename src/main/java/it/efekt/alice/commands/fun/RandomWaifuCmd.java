package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.Random;

public class RandomWaifuCmd extends Command {
    private final int IMAGES_MAX = 60000;

    public RandomWaifuCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.FUN);
        setDescription(AMessage.CMD_RANDOMWAIFU_DESC);

        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        int imageNr = new Random().nextInt(IMAGES_MAX);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setTitle("Your random Waifu");
        embedBuilder.setImage("https://www.thiswaifudoesnotexist.net/example-"+imageNr+".jpg");
        e.sendEmbeddedMessageToChannel(embedBuilder.build());

        return true;
    }
}
