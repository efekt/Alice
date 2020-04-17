package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.lava.extensions.youtuberotator.YoutubeIpRotatorSetup;
import com.sedmelluq.lava.extensions.youtuberotator.planner.AbstractRoutePlanner;
import com.sedmelluq.lava.extensions.youtuberotator.planner.RotatingNanoIpRoutePlanner;
import com.sedmelluq.lava.extensions.youtuberotator.tools.ip.IpBlock;
import com.sedmelluq.lava.extensions.youtuberotator.tools.ip.Ipv6Block;
import it.efekt.alice.commands.voice.TrackScheduler;
import it.efekt.alice.config.Config;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

public class AliceAudioManager {
    private Config config;
    private Logger logger = LoggerFactory.getLogger(AliceAudioManager.class);
    private AudioPlayerManager audioPlayerManager; // single instance in whole app
    private HashMap<String, AliceSendHandler> sendHandlers = new HashMap<>();
    private HashMap<String, AliceReceiveHandler> receiveHandlers = new HashMap<>();
    private HashMap<String, String> lastPlayedContent = new HashMap<>();
    private HashMap<String, TrackScheduler> trackSchedulers = new HashMap<>();
    private final int MAX_QUEUE_SIZE = 10;

    public AliceAudioManager(Config config){
        this.config = config;
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.audioPlayerManager.setFrameBufferDuration(3000);
        this.audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);

        if (config.getLavaPlayerNodeUrl() != null && !config.getLavaPlayerNodeUrl().isEmpty()){
            this.audioPlayerManager.useRemoteNodes(config.getLavaPlayerNodeUrl());
        }

        registerAudioSources();
    }

    private void registerAudioSources(){
        YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager();
        youtubeAudioSourceManager.configureRequests(config -> RequestConfig.copy(config)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build());

        if (this.config.getIpv6Block() != null) {
            List<IpBlock> ipBlocks = Collections.singletonList(new Ipv6Block(this.config.getIpv6Block()));

            AbstractRoutePlanner planner = new RotatingNanoIpRoutePlanner(ipBlocks);
            new YoutubeIpRotatorSetup(planner).forSource(youtubeAudioSourceManager).setup();
            logger.info("YouTube rotator set up, ips: "+ ipBlocks);
        }
        this.audioPlayerManager.registerSourceManager(youtubeAudioSourceManager);

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
        audioManager.setAutoReconnect(true);
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
        // if guild/user rate limit is being met, do not allow alice to even try to load a new track

        if (getTrackScheduler(e.getGuild()).getQueue().size() >= this.MAX_QUEUE_SIZE){
            e.getChannel().sendMessage("You can queue max up to " + this.MAX_QUEUE_SIZE + " tracks").complete();
            return;
        }

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
                // if queue is empty and no matches, destroy player
                if (getAudioPlayer(e.getGuild()).getPlayingTrack() == null  && getTrackScheduler(e.getGuild()).getQueue().isEmpty()){
                    getAudioPlayer(e.getGuild()).destroy();
                }

                e.getChannel().sendMessage(AMessage.VOICE_NOTHING_FOUND.get(e.getGuild())).complete();
            }

            @Override
            public void loadFailed(FriendlyException exc) {
                // if queue is empty and no matches, destroy player
                if (getAudioPlayer(e.getGuild()).getPlayingTrack() == null  && getTrackScheduler(e.getGuild()).getQueue().isEmpty()){
                    getAudioPlayer(e.getGuild()).destroy();
                }

                exc.printStackTrace();
                e.getChannel().sendMessage(AMessage.VOICE_LOADING_FAILED.get(e.getGuild()) + "\n"+exc.getLocalizedMessage()).complete();
                if (isValidURL(content)){
                    e.getChannel().sendMessage("Please try using keywords instead of the direct url").complete();
                }

                if (exc.getMessage() != null){
                    // on youtube block
                    if (exc.getMessage().contains("Loading information for a YouTube track failed.") && exc.getCause() != null && exc.getCause().getMessage().contains("YouTube rate limit reached")){
                        e.getChannel().sendMessage(AMessage.VOICE_LOADING_RATE_LIMITED.get(e.getGuild())).complete();
                    }

                    // On retry limit
                    if (exc.getMessage().contains("Loading information for a YouTube track failed.") && exc.getCause() != null && exc.getCause().getMessage().contains("Retry aborted, too many retries on ratelimit.")){
                        e.getChannel().sendMessage(AMessage.VOICE_LOADING_RATE_LIMITED.get(e.getGuild())).complete();
                    }
                }
            }
        });
    }

    private void sendLoadedMessage(MessageReceivedEvent e, AudioTrack audioTrack){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri);
        embedBuilder.addField(AMessage.CMD_PLAY_LOADED_AND_QUEUED.get(e), "", false);
        String prefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();
        embedBuilder.setFooter(prefix + AMessage.CMD_PLAY_LOADED_FOOTER.get(e, "np"), e.getJDA().getSelfUser().getEffectiveAvatarUrl());
        e.getChannel().sendMessage(embedBuilder.build()).complete();
    }

    public void closeAudioConnAndUnload(Guild guild){
        guild.getAudioManager().closeAudioConnection();
        AliceBootstrap.alice.getAliceAudioManager().getAudioPlayer(guild).destroy();
        AliceBootstrap.alice.getAliceAudioManager().getTrackScheduler(guild).getQueue().clear();
    }

    private boolean isValidURL(String url){
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
