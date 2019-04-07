package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import it.efekt.alice.commands.voice.TrackScheduler;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import ws.schild.jave.EncoderException;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.Future;

public class AliceAudioManager {
    private AudioPlayerManager audioPlayerManager; // single instance in whole app
    private HashMap<String, AliceSendHandler> sendHandlers = new HashMap<>();
    private HashMap<String, AliceReceiveHandler> receiveHandlers = new HashMap<>();

    public AliceAudioManager(){
        this.audioPlayerManager = new DefaultAudioPlayerManager();
    }

    private AliceSendHandler getSendHandler(Guild guild){
        this.sendHandlers.putIfAbsent(guild.getId(), new AliceSendHandler(this.audioPlayerManager.createPlayer()));
        return this.sendHandlers.get(guild.getId());
    }

    public AliceReceiveHandler getReceiveHandler(Guild guild){
        this.receiveHandlers.putIfAbsent(guild.getId(), new AliceReceiveHandler());
        return this.receiveHandlers.get(guild.getId());
    }

    public AudioPlayer getAudioPlayer(Guild guild){
        return getSendHandler(guild).getAudioPlayer();
    }

    private AudioPlayerManager getAudioPlayerManager(){
        return this.audioPlayerManager;
    }

    private boolean isConnected(Guild guild){
        return guild.getAudioManager().isConnected();
    }

    public void connectVoice(VoiceChannel voiceChannel){
        if (!isConnected(voiceChannel.getGuild())){
            voiceChannel.getGuild().getAudioManager().openAudioConnection(voiceChannel);
        }
    }

    private Future<Void> playRemoteSource(Guild guild, String content, AudioLoadResultHandler audioLoadResultHandler){
        AudioSourceManagers.registerRemoteSources(getAudioPlayerManager());
        AudioSourceManagers.registerLocalSource(getAudioPlayerManager());
        getAudioPlayerManager().getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        getAudioPlayer(guild).addListener(new TrackScheduler());
        AudioManager audioManager = guild.getAudioManager();
        audioManager.setSendingHandler(getSendHandler(guild));

        return this.audioPlayerManager.loadItem(content, audioLoadResultHandler);
    }


    public void startRecording(Guild guild){
        guild.getAudioManager().setSendingHandler(new AliceSilenceSendHandler());
        guild.getAudioManager().setReceivingHandler(getReceiveHandler(guild));
        getReceiveHandler(guild).startRecording();
    }

    public void stopRecordingAndSave(Guild guild, File file){
        if (getReceiveHandler(guild).isRecording()){
            getReceiveHandler(guild).stopRecording();
            getReceiveHandler(guild).save(file);
        }
    }

    public byte[] stopRecordingAndGetStream(Guild guild) throws IOException {
        return getReceiveHandler(guild).stopRecordingAndGetStream();
    }

    public File stopRecordingAndGetFile(Guild guild) throws IOException, EncoderException {
        return getReceiveHandler(guild).stopRecordingAndGetFile();
    }

    public boolean isRecording(Guild guild){
        return getReceiveHandler(guild).isRecording();
    }

    public void play(MessageReceivedEvent e, String content){
        playRemoteSource(e.getGuild(), content, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                getAudioPlayer(e.getGuild()).playTrack(audioTrack);
                AliceBootstrap.alice.getCmdManager().getCommand("np").onCommand(e);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {
                e.getChannel().sendMessage(Message.VOICE_NOTHING_FOUND.get(e.getGuild())).complete();
            }

            @Override
            public void loadFailed(FriendlyException exc) {
                e.getChannel().sendMessage(Message.VOICE_LOADING_FAILED.get(e.getGuild()) + "\n"+exc.getLocalizedMessage()).complete();

            }
        });
    }


}
