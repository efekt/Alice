package it.efekt.alice.commands.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCmd extends Command {
    public PauseCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(Message.CMD_PAUSE_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        AudioPlayer audioPlayer = aliceAudioManager.getAudioPlayer(e.getGuild());
        if (audioPlayer.getPlayingTrack() != null){
            if (!audioPlayer.isPaused()){
                audioPlayer.setPaused(true);
                e.getChannel().sendMessage(Message.CMD_PAUSE_PAUSED.get(e)).complete();
                return true;
            } else {
                audioPlayer.setPaused(false);
                e.getChannel().sendMessage(Message.CMD_PLAY_TRACK_RESUMED.get(e)).complete();
                return true;
            }
        }
        return true;
    }
}
