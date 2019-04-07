package it.efekt.alice.modules;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AliceSilenceSendHandler implements AudioSendHandler {
    private AudioFrame lastFrame;
    private boolean canProvide = true;
    long startTime = System.currentTimeMillis();

    @Override
    public boolean canProvide() {
        return this.canProvide;
    }

    @Override
    public byte[] provide20MsAudio() {
        if (System.currentTimeMillis() - startTime > 1){
            this.canProvide = false;
        }
        byte[] bytes = new byte[1];
        return bytes;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

}
