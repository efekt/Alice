package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCmd extends Command {
    public LeaveCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setDescription(AMessage.CMD_LEAVE_DESC);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        AudioManager audioManager = e.getGuild().getAudioManager();
        if (audioManager.isConnected()){
            AliceBootstrap.alice.getAliceAudioManager().closeAudioConnAndUnload(e.getGuild());
            e.getChannel().sendMessage(AMessage.CMD_LEAVE_LEFT.get(e)).complete();
            return true;
        }

        e.getChannel().sendMessage(AMessage.CMD_LEAVE_NOT_CONNECTED.get(e)).complete();
        return true;
    }
}
