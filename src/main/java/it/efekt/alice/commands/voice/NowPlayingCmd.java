package it.efekt.alice.commands.voice;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class NowPlayingCmd extends Command {
    private final int MAX_QUEUE_DISPLAY_SIZE = 5;

    public NowPlayingCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(Message.CMD_NOWPLAYING_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        TrackScheduler trackScheduler = aliceAudioManager.getTrackScheduler(e.getGuild());
        String guildPrefix = AliceBootstrap.alice.getGuildConfigManager().getGuildConfig(e.getGuild()).getPrefix();

        int queueSize = trackScheduler.getQueue().size();

        if (trackScheduler.getAudioPlayer().getPlayingTrack() == null){
            e.getChannel().sendMessage(Message.CMD_NOWPLAYING_NOTHING.get(e)).complete();
            return true;
        }

        String output = "**"+Message.CMD_NOWPLAYING_QUEUE.get(e)+"**\n";

        AudioTrack currentTrack = trackScheduler.getAudioPlayer().getPlayingTrack();

        String title = currentTrack.getInfo().title;
        String url = currentTrack.getInfo().uri;
        String sourceName = currentTrack.getSourceManager().getSourceName();
        NumberFormat numberFormat = new DecimalFormat("00");

        long currentMin = TimeUnit.MILLISECONDS.toMinutes(currentTrack.getPosition());
        long currentSec = TimeUnit.MILLISECONDS.toSeconds(currentTrack.getPosition()) - currentMin * 60;

        long currentDurationMin = TimeUnit.MILLISECONDS.toMinutes(currentTrack.getDuration());
        long currentDurationSec = TimeUnit.MILLISECONDS.toSeconds(currentTrack.getDuration()) - currentDurationMin * 60;

        String curMin = numberFormat.format(currentMin);
        String curSec = numberFormat.format(currentSec);
        String curDurMin = numberFormat.format(currentDurationMin);
        String curDurSec = numberFormat.format(currentDurationSec);

        String playingNowTitle = ":loud_sound: " + "**"+ title + "**";
        String playingNowInfo =  ":notes: " + curMin + ":" + curSec  +"/" + curDurMin + ":" + curDurSec + "    [" + sourceName + "]";

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.setTitle(playingNowTitle, url);
        embedBuilder.setFooter(guildPrefix + Message.CMD_NOWPLAYING_FOOTER.get(e, "skip"), e.getJDA().getSelfUser().getEffectiveAvatarUrl());

        if (queueSize == 0){
            embedBuilder.addField(playingNowInfo, "", false);
            e.getChannel().sendMessage(embedBuilder.build()).complete();
            return true;
        }

        int i = 1;
        for (AudioTrack audioTrack : trackScheduler.getQueue()){
            long durationMin = TimeUnit.MILLISECONDS.toMinutes(audioTrack.getDuration());
            long durationSec = TimeUnit.MILLISECONDS.toSeconds(audioTrack.getDuration()) - durationMin * 60;


            String durMin = numberFormat.format(durationMin);
            String durSec = numberFormat.format(durationSec);
            String extraInfo = "[" + durMin + ":" + durSec + "]    [" + sourceName + "]";
            output = output.concat("**" + i + ".** **" + audioTrack.getInfo().title + "**\n  " + extraInfo + "\n");

            if (i >= MAX_QUEUE_DISPLAY_SIZE){
                if (trackScheduler.getQueue().size() > MAX_QUEUE_DISPLAY_SIZE){
                    String leftInQueue = String.valueOf(trackScheduler.getQueue().size() - MAX_QUEUE_DISPLAY_SIZE);
                    output = output.concat("**" + Message.CMD_NOWPLAYING_LEFT_IN_QUEUE.get(e, leftInQueue)+ "**");
                }
                break;
            }
            i++;

        }

        embedBuilder.addField(playingNowInfo, output, false);
        e.getChannel().sendMessage(embedBuilder.build()).complete();

        return true;
    }
}
