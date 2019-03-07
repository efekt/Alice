package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class PlayCmd extends Command {
    public PlayCmd(String alias) {
        super(alias);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (getArgs().length >= 1){
            String query = Arrays.toString(getArgs()).replaceAll(",", "").replaceAll("]", "").replaceAll("\\[", "");
            play(e, query);
            return true;
        }
        return false;
    }

    private void play(MessageReceivedEvent e, String query){
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        aliceAudioManager.connectVoice(e.getMember().getVoiceState().getChannel());
        aliceAudioManager.play(e, query);
    }

}
