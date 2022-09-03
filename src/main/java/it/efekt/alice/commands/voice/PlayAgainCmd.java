package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayAgainCmd extends Command {
    public PlayAgainCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(AMessage.CMD_PLAYAGAIN_DESC);

        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        if (aliceAudioManager.getLastPlayed(e.getGuild()) == null){
            e.sendMessageToChannel("I didn't play anything yet");
            return true;
        } else {
            if (AliceBootstrap.alice.getCmdManager().getCommand("join").onCommand(e)){
                aliceAudioManager.play(e, aliceAudioManager.getLastPlayed(e.getGuild()));
                return true;
            }
        }
        // No slash confirmation
        e.sendSlashConfirmation(AMessage.CMD_PLAYAGAIN_DESC.get(e));
        return false;
    }
}
