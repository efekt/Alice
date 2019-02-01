package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TomekCmd extends Command {

    public TomekCmd(String alias) {
        super(alias);
        setDescription("Tomek");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        AliceBootstrap.db.setGuildPrefix(e.getGuild(), "Nowy");
        try {
            e.getChannel().sendFile(AliceBootstrap.class.getClassLoader().getResourceAsStream("assets/images/tomek.png"), "tomek.png").queue();
        } catch (NullPointerException exc) {
            e.getChannel().sendMessage("Nie znaleziono pliku :(").queue();
        }
    }
}
