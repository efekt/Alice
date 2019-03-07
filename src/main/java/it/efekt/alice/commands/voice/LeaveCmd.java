package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class LeaveCmd extends Command {
    public LeaveCmd(String alias) {
        super(alias);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        AudioManager audioManager = e.getGuild().getAudioManager();
        if (audioManager.isConnected()){
            audioManager.closeAudioConnection();
            e.getChannel().sendMessage("Ok, I left the channel").complete();
            return true;
        }

        e.getChannel().sendMessage("I was not connected in the first place! :rolling_eyes:").complete();
        return true;
    }
}
