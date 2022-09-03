package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCmd extends Command {

    public SkipCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(AMessage.CMD_SKIP_DESC);

        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        TrackScheduler trackScheduler = aliceAudioManager.getTrackScheduler(e.getGuild());

        int queueSize = trackScheduler.getQueue().size();

        // if there is nothing in queue
        if (queueSize == 0){
            e.sendMessageToChannel(AMessage.CMD_SKIP_NOTHING_TO_SKIP_TO.get(e));
            return true;
        } else {
            trackScheduler.playNextTrack();
            e.sendMessageToChannel(AMessage.CMD_SKIP_SKIPPING.get(e));
            return true;
        }
    }
}
