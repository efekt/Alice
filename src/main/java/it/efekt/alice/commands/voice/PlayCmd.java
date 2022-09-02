package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.net.URL;

public class PlayCmd extends Command {
    public PlayCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setShortUsageInfo(AMessage.CMD_PLAY_SHORT_USAGE_INFO);
        setDescription(AMessage.CMD_PLAY_DESC);
        setFullUsageInfo(AMessage.CMD_PLAY_FULL_USAGE_INFO);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        if (getArgs().length >= 1){
            String query = String.join(" ", getArgs());
            if (!isValidURL(query)) {
                play(e, "ytsearch: " + query);
            } else {
                play(e, query);
            }
            return true;
        } else {
            if (aliceAudioManager.getAudioPlayer(e.getGuild()).isPaused()){
                aliceAudioManager.getAudioPlayer(e.getGuild()).setPaused(false);
                e.getChannel().sendMessage(AMessage.CMD_PLAY_TRACK_RESUMED.get(e.getGuild())).complete();
                return true;
            }
        }
        return false;
    }

    private void play(CombinedCommandEvent e, String query){
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        if (AliceBootstrap.alice.getCmdManager().getCommand("join").onCommand(e)){
            aliceAudioManager.play(e, query);
        }
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
