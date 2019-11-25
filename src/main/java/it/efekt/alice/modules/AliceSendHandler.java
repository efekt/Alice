package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AliceSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private MutableAudioFrame frame;
    private ByteBuffer buffer;

    public AliceSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(this.buffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        this.buffer.flip();
        return this.buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    public AudioPlayer getAudioPlayer(){
        return this.audioPlayer;
    }
}
