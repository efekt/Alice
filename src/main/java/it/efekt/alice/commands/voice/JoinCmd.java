package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class JoinCmd extends Command {
    public JoinCmd(String alias) {
        super(alias);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (e.getMember().getVoiceState().inVoiceChannel()){
            VoiceChannel voiceChannel = e.getMember().getVoiceState().getChannel();
            AudioManager audioManager = e.getGuild().getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
            e.getChannel().sendMessage("Joined " + voiceChannel.getName()).complete();
            return true;
        } else {
            e.getChannel().sendMessage("Where are you? I cannot see you on any voice channel :worried:").complete();
            return true;
        }
    }
}
