package it.efekt.alice.commands;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.io.File;



public class TomekCmd extends Command {

    public TomekCmd(String alias) {
        super(alias);
        setDescription("Tomek");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        try {
            File file = new File(AliceBootstrap.class.getClassLoader().getResource("assets/images/tomek.png").getFile());
            e.getChannel().sendFile(file, "assets/images/tomek.png").queue();
        }
        catch (NullPointerException exc){
            e.getChannel().sendMessage("Nie znaleziono pliku :(");
        }
    }
}
