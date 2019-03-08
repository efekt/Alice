package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class JoinCmd extends Command {
    public JoinCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(Message.CMD_JOIN_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (e.getGuild().getAudioManager().isConnected()){
            return true;
        }

        if (e.getMember().getVoiceState().inVoiceChannel()){
            VoiceChannel voiceChannel = e.getMember().getVoiceState().getChannel();
            AudioManager audioManager = e.getGuild().getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
            e.getChannel().sendMessage(Message.CMD_JOIN_JOINED.get(e, voiceChannel.getName())).complete();
            return true;
        } else {
            e.getChannel().sendMessage(Message.CMD_JOIN_USER_NOT_CONNECTED.get(e)).complete();
            return true;
        }
    }
}
