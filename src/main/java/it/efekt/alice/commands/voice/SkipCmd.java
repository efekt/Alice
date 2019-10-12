package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCmd extends Command {

    public SkipCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(Message.CMD_SKIP_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        AliceAudioManager aliceAudioManager = AliceBootstrap.alice.getAliceAudioManager();
        TrackScheduler trackScheduler = aliceAudioManager.getTrackScheduler(e.getGuild());

        int queueSize = trackScheduler.getQueue().size();

        // if there is nothing in queue
        if (queueSize == 0){
            e.getChannel().sendMessage(Message.CMD_SKIP_NOTHING_TO_SKIP_TO.get(e)).complete();
            return true;
        } else {
            trackScheduler.playNextTrack();
            e.getChannel().sendMessage(Message.CMD_SKIP_SKIPPING.get(e)).complete();
            return true;
        }
    }
}
