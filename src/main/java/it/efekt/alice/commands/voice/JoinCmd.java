package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCmd extends Command {
    public JoinCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(AMessage.CMD_JOIN_DESC);

        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {

        if (e.getMember().getVoiceState().inAudioChannel()){
            if (e.getGuild().getAudioManager().isConnected() && e.getMember().getVoiceState().getChannel().getId().equalsIgnoreCase(e.getGuild().getAudioManager().getConnectedChannel().getId())){
                e.sendSlashConfirmation(AMessage.CMD_JOIN_DESC.get(e));
                return true; //What is this return and check, confirmation is needed here for slash command
            }


            AudioChannel voiceChannel = e.getMember().getVoiceState().getChannel();
            AudioManager audioManager = e.getGuild().getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
            e.sendMessageToChannel(AMessage.CMD_JOIN_JOINED.get(e, voiceChannel.getName()));
            return true;
        } else {
            e.sendMessageToChannel(AMessage.CMD_JOIN_USER_NOT_CONNECTED.get(e));
            return true;
        }
    }
}
