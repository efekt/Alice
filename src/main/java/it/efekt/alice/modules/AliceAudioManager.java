package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import it.efekt.alice.commands.voice.TrackScheduler;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import ws.schild.jave.EncoderException;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.Future;

public class AliceAudioManager {
    private AudioPlayerManager audioPlayerManager; // single instance in whole app
    private HashMap<String, AliceSendHandler> sendHandlers = new HashMap<>();
    private HashMap<String, AliceReceiveHandler> receiveHandlers = new HashMap<>();
    private HashMap<String, String> lastPlayedContent = new HashMap<>();
    private HashMap<String, TrackScheduler> trackSchedulers = new HashMap<>();

    public AliceAudioManager(){
        this.audioPlayerManager = new DefaultAudioPlayerManager();
    }

    public AliceSendHandler getSendHandler(Guild guild){
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
        // yeah, it's not so nice, both player and track scheduler should be handled differently, todo: refactor this someday
        getAudioPlayer(guild).removeListener(getTrackScheduler(guild));
        getAudioPlayer(guild).addListener(getTrackScheduler(guild));
        AudioManager audioManager = guild.getAudioManager();
        audioManager.setSendingHandler(getSendHandler(guild));
        return this.audioPlayerManager.loadItem(content, audioLoadResultHandler);
    }


    public void startRecording(Guild guild, TextChannel textChannel){
        guild.getAudioManager().setSendingHandler(new AliceSilenceSendHandler());
        guild.getAudioManager().setReceivingHandler(getReceiveHandler(guild));
        getReceiveHandler(guild).startRecording(textChannel);
    }

    public String getLastPlayed(Guild guild){
        return this.lastPlayedContent.get(guild.getId());
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

    public TrackScheduler getTrackScheduler(Guild guild){
       this.trackSchedulers.putIfAbsent(guild.getId(), new TrackScheduler(getAudioPlayer(guild)));
       return this.trackSchedulers.get(guild.getId());
    }

    public long getCurrentPlaybackCount(){
       return  sendHandlers.values().stream().filter(handler -> handler.getAudioPlayer().getPlayingTrack() != null).count();
    }

    public long getCurrentPausedCount(){
        return  sendHandlers.values().stream().filter(handler -> handler.getAudioPlayer().isPaused()).count();
    }

    public long getCurrentAudioChannelJoinedCount(){
        long totalAudioChannelJoined = 0;
        for (JDA jda : AliceBootstrap.alice.getShardManager().getShards()){
            totalAudioChannelJoined += jda.getAudioManagers().stream().filter(AudioManager::isConnected).count();
        }

        return totalAudioChannelJoined;
    }

    public void play(MessageReceivedEvent e, String content){
        this.lastPlayedContent.put(e.getGuild().getId(), content);
        playRemoteSource(e.getGuild(), content, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                getTrackScheduler(e.getGuild()).queue(audioTrack);
                sendLoadedMessage(e, audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                getTrackScheduler(e.getGuild()).queue(audioPlaylist.getTracks().get(0));
                sendLoadedMessage(e, audioPlaylist.getTracks().get(0));
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

    private void sendLoadedMessage(MessageReceivedEvent e, AudioTrack audioTrack){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri);
        embedBuilder.addField(Message.CMD_PLAY_LOADED_AND_QUEUED.get(e), "", false);
        String prefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
        embedBuilder.setFooter(prefix + Message.CMD_PLAY_LOADED_FOOTER.get(e, "np"), e.getJDA().getSelfUser().getEffectiveAvatarUrl());
        e.getChannel().sendMessage(embedBuilder.build()).complete();
    }

}
