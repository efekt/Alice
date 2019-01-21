package it.efekt.alice.listeners;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        String msg = e.getMessage().getContentDisplay();

        if (msg.equalsIgnoreCase("!join")){
                VoiceChannel voiceChannel = e.getMember().getVoiceState().getChannel();
                e.getGuild().getAudioManager().openAudioConnection(voiceChannel);
        }

        if (msg.equalsIgnoreCase("!leave")){
            if (e.getGuild().getAudioManager().isConnected()){
                e.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }
}
