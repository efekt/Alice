package it.efekt.alice.modules;

import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.CombinedAudio;
import net.dv8tion.jda.core.audio.UserAudio;
import ws.schild.jave.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


public class AliceReceiveHandler implements AudioReceiveHandler {
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private boolean recording = false;

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
            out.write(stream);
        } catch (IOException e) {
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

    public void startRecording(){
            this.out.reset();
            this.recording = true;
    }

    public AudioInputStream stopRecording(){
        this.recording = false;
        return this.getAudioInputStream();
    }

    public byte[] stopRecordingAndGetStream() throws IOException {
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

    private File convertToMp3(File source, File target) throws EncoderException {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
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
}
