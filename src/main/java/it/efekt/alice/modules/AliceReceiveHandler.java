package it.efekt.alice.modules;

import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.CombinedAudio;
import net.dv8tion.jda.core.audio.UserAudio;

public class AliceReceiveHandler implements AudioReceiveHandler {

    @Override
    public boolean canReceiveCombined() {
        return true;
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleCombinedAudio(CombinedAudio combinedAudio) {
        combinedAudio.getAudioData(1.0);
        System.out.println(combinedAudio.getUsers().size());

    }

    @Override
    public void handleUserAudio(UserAudio userAudio) {
        System.out.println(userAudio.getUser().toString());
    }

}
