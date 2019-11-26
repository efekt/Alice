package it.efekt.alice.commands.voice;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import it.efekt.alice.modules.AliceAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ws.schild.jave.EncoderException;
import java.io.File;
import java.io.IOException;

public class RecordCmd extends Command {
    public RecordCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.VOICE);
        setFullUsageInfo(AMessage.CMD_REC_FULL_USAGE_INFO);
        setDescription(AMessage.CMD_REC_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {

        AliceAudioManager audioManager = AliceBootstrap.alice.getAliceAudioManager();

        if (!e.getMember().getVoiceState().inVoiceChannel()){
            e.getChannel().sendMessage(AMessage.CMD_REC_USER_NOT_CONNECTED.get(e)).complete();
            return true;
        }

        if (!e.getGuild().getAudioManager().isConnected()){
            e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
        }


        if (audioManager.isRecording(e.getGuild())){
            try {
                e.getChannel().sendMessage(AMessage.CMD_REC_STOPPED.get(e)).complete();
                File file = audioManager.stopRecordingAndGetFile(e.getGuild());
                audioManager.getReceiveHandler(e.getGuild()).sendMessageWithFile(file, e.getTextChannel());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (EncoderException e1) {
                e1.printStackTrace();
            }
        } else {
            audioManager.startRecording(e.getGuild(), e.getTextChannel());
            e.getChannel().sendMessage( AMessage.CMD_REC_STARTED.get(e, String.valueOf(audioManager.getReceiveHandler(e.getGuild()).getMAX_RECORD_TIME()))).complete();
        }


        return true;
    }
}
