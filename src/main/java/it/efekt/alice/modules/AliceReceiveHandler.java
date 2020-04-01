package it.efekt.alice.modules;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import ws.schild.jave.*;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class AliceReceiveHandler implements AudioReceiveHandler {
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private boolean recording = false;
    private final long MAX_RECORD_TIME = 3; // in Minutes
    private long curTime = 0; // in seconds
    private String channelHolderId;
    private Set<User> recordedUsers = new HashSet<>();

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
        if (!recording){
            return;
        }
       byte[] stream = combinedAudio.getAudioData(1.0);
        try {
            if (curTime/1000 >= MAX_RECORD_TIME * 60){
                String channelId = this.channelHolderId;
                this.reset();
                TextChannel textChannel = AliceBootstrap.alice.getShardManager().getTextChannelById(channelId);
                this.sendMessageWithFile(stopRecordingAndGetFile(), textChannel);

            }

            curTime += 20;
            combinedAudio.getUsers().forEach(user -> this.recordedUsers.add(user));
            out.write(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleUserAudio(UserAudio userAudio) {

    }

    public void save(File file) {
        try {
            AudioSystem.write(getAudioInputStream(), AudioFileFormat.Type.WAVE, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public AudioInputStream getAudioInputStream(){
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, OUTPUT_FORMAT, (out.toByteArray().length / OUTPUT_FORMAT.getFrameSize()));
            return AudioSystem.getAudioInputStream(OUTPUT_FORMAT, audioInputStream);
    }

    public void startRecording(TextChannel channel){
            this.channelHolderId = channel.getId();
            this.out.reset();
            this.recording = true;
    }

    public AudioInputStream stopRecording(){
        this.reset();
        this.recording = false;
        return this.getAudioInputStream();
    }

    public byte[] stopRecordingAndGetStream() throws IOException {
        this.reset();
        this.recording = false;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AudioSystem.write(getAudioInputStream(), AudioFileFormat.Type.WAVE, outputStream);
        return outputStream.toByteArray();
    }

    public File stopRecordingAndGetFile() throws IOException, EncoderException {
        this.recording = false;
        String fileName = UUID.randomUUID().toString();
        if (!Files.isDirectory(Paths.get("./temp"))){
            new File("./temp").mkdir();
        }

        File source = new File("./temp/" + fileName + ".wav");
        File target = new File("./temp/" + fileName + ".mp3");

        AudioSystem.write(getAudioInputStream(), AudioFileFormat.Type.WAVE, source);
        return convertToMp3(source, target);
    }

    public boolean isRecording(){
        return this.recording;
    }

    private void reset(){
        this.curTime = 0;
        this.channelHolderId = null;
        this.recordedUsers.clear();
    }

    private File convertToMp3(File source, File target) throws EncoderException {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(160000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        //Encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        //Encode
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
        source.delete();
        return target;
    }

    public void sendMessageWithFile(File file, @Nullable TextChannel channel){
        if (channel == null){
            file.delete();
            return;
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss");
        String dateTime = LocalDateTime.now().format(dateFormat);
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.append(AMessage.CMD_REC_USERS.get(channel.getGuild()) + "\n");
        if (!this.recordedUsers.isEmpty()){
            this.recordedUsers.forEach(user -> messageBuilder.append(user.getName() + " "));
        }
        channel.sendMessage(messageBuilder.build()).addFile(file, "Alice_" + dateTime + ".mp3").complete();
        this.reset();
        file.delete();
    }

    public float getMAX_RECORD_TIME(){
        return this.MAX_RECORD_TIME;
    }

}
