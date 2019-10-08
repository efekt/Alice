package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCmd extends Command {
    public JoinCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(Message.CMD_JOIN_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        if (e.getMember().getVoiceState().inVoiceChannel()){
            if (e.getGuild().getAudioManager().isConnected() && e.getMember().getVoiceState().getChannel().getId().equalsIgnoreCase(e.getGuild().getAudioManager().getConnectedChannel().getId())){
                return true;
            }


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
