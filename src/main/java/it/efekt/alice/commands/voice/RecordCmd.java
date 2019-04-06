package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.modules.AliceSilenceSendHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RecordCmd extends Command {
    public RecordCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        e.getGuild().getAudioManager().setSendingHandler(new AliceSilenceSendHandler(AliceBootstrap.alice.getAliceAudioManager().getAudioPlayer(e.getGuild())));
        AliceBootstrap.alice.getAliceAudioManager().record(e.getGuild());


        return false;
    }
}
