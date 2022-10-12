package it.efekt.alice.commands.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PauseCmd extends Command {
    public PauseCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(AMessage.CMD_PAUSE_DESC);

        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        AudioPlayer audioPlayer = aliceAudioManager.getAudioPlayer(e.getGuild());
        if (audioPlayer.getPlayingTrack() != null){
            if (!audioPlayer.isPaused()){
                audioPlayer.setPaused(true);
                e.sendMessageToChannel(AMessage.CMD_PAUSE_PAUSED.get(e));
                return true;
            } else {
                audioPlayer.setPaused(false);
                e.sendMessageToChannel(AMessage.CMD_PLAY_TRACK_RESUMED.get(e));
                return true;
            }
        }
        //No confirmation here for slash
        e.sendSlashConfirmation(AMessage.CMD_PAUSE_DESC.get(e));
        return true;
    }
}
