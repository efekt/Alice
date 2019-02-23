package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TomekCmd extends Command {

    public TomekCmd(String alias) {
        super(alias);
        setDescription("Tomek");
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        try {
            e.getChannel().sendFile(AliceBootstrap.class.getClassLoader().getResourceAsStream("assets/images/tomek.png"), "tomek.png").queue();
            return true;
        } catch (NullPointerException exc) {
            e.getChannel().sendMessage("Nie znaleziono pliku :(").queue();
            return true;
        }
    }
}
