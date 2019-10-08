package it.efekt.alice.commands.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
    public NowPlayingCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(Message.CMD_NOWPLAYING_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        AudioPlayer audioPlayer = aliceAudioManager.getAudioPlayer(e.getGuild());

        if (audioPlayer.getPlayingTrack() == null){
            e.getChannel().sendMessage(Message.CMD_NOWPLAYING_NOTHING.get(e)).complete();
            return true;
        }

        AudioTrack audioTrack = audioPlayer.getPlayingTrack();

        String title = audioTrack.getInfo().title;
        String author = audioTrack.getInfo().author;
        String uri = audioTrack.getInfo().uri;
        String sourceName = audioTrack.getSourceManager().getSourceName();

        long currentMin = TimeUnit.MILLISECONDS.toMinutes(audioTrack.getPosition());
        long currentSec = TimeUnit.MILLISECONDS.toSeconds(audioTrack.getPosition()) - currentMin * 60;

        long durationMin = TimeUnit.MILLISECONDS.toMinutes(audioTrack.getDuration());
        long durationSec = TimeUnit.MILLISECONDS.toSeconds(audioTrack.getDuration()) - durationMin * 60;

        NumberFormat numberFormat = new DecimalFormat("00");
        String curMin = numberFormat.format(currentMin);
        String curSec = numberFormat.format(currentSec);
        String durMin = numberFormat.format(durationMin);
        String durSec = numberFormat.format(durationSec);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(title, uri);
        embedBuilder.setColor(AliceBootstrap.EMBED_COLOR);
        embedBuilder.addField(Message.CMD_NOWPLAYING_AUTHOR.get(e), author, true);
        embedBuilder.addField(Message.CMD_NOWPLAYING_SOURCE.get(e), sourceName, true);
        embedBuilder.addField(Message.CMD_NOWPLAYING_TIME.get(e),curMin + ":" + curSec  +"/" + durMin + ":" + durSec, false);

        e.getChannel().sendMessage(embedBuilder.build()).complete();

        return true;
    }
}
