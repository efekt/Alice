package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import java.nio.ByteBuffer;

public class AliceSilenceSendHandler implements AudioSendHandler {
    private AudioFrame lastFrame;
    private boolean canProvide = true;
    long startTime = System.currentTimeMillis();

    @Override
    public boolean canProvide() {
        return this.canProvide;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        if (System.currentTimeMillis() - startTime > 1){
            this.canProvide = false;
        }
        return ByteBuffer.wrap(new byte[1]);
    }

    @Override
    public boolean isOpus() {
        return true;
    }

}
