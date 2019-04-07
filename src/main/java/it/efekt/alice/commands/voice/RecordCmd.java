package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;

public class RecordCmd extends Command {
    public RecordCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        AliceAudioManager audioManager = AliceBootstrap.alice.getAliceAudioManager();

        if (audioManager.isRecording(e.getGuild())){
            try {
                File file = audioManager.stopRecordingAndGetFile(e.getGuild());
                e.getChannel().sendMessage("Recording stopped. Sending...").complete();
                e.getChannel().sendFile(file, "recording.mp3").complete();
                file.delete();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (EncoderException e1) {
                e1.printStackTrace();
            }
        } else {
            audioManager.startRecording(e.getGuild());
            e.getChannel().sendMessage("Recording started...").complete();
        }


        return true;
    }
}
