package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AliceSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private MutableAudioFrame frame;

    public AliceSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.frame = new MutableAudioFrame();
        this.frame.setFormat(StandardAudioDataFormats.DISCORD_OPUS);
        this.frame.setBuffer(ByteBuffer.allocate(this.frame.getFormat().maximumChunkSize()));
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(this.frame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    public AudioPlayer getAudioPlayer(){
        return this.audioPlayer;
    }
}
