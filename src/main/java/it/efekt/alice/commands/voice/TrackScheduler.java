package it.efekt.alice.commands.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private AudioPlayer audioPlayer;
    private BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    public Queue<AudioTrack> getQueue(){
        return this.queue;
    }

    public void playNextTrack(){
        this.audioPlayer.startTrack(this.queue.poll(), false);
    }

    public void queue(AudioTrack audioTrack){
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!this.audioPlayer.startTrack(audioTrack, true)) {
            queue.offer(audioTrack);
        }
    }

    public AudioPlayer getAudioPlayer(){
        return this.audioPlayer;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            playNextTrack();
        }
    }

}
