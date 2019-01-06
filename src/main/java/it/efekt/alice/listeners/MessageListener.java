package it.efekt.alice.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class MessageListener extends ListenerAdapter {
    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();


    public MessageListener(){
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getMessage().getContent().startsWith("!ping")){
            e.getChannel().sendMessage("Pong: " + e.getJDA().getPing() + "ms").queue();
        }

        if (e.getMessage().getContent().equalsIgnoreCase("!join")){
                VoiceChannel voiceChannel = e.getMember().getVoiceState().getChannel();
                e.getGuild().getAudioManager().openAudioConnection(voiceChannel);

            }

            if (e.getMessage().getContent().equalsIgnoreCase("!leave")){
                if (e.getGuild().getAudioManager().isConnected()){
                    e.getGuild().getAudioManager().closeAudioConnection();
                }
            }




    }
}
