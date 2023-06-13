package com.clt.speech;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AudioManagementMicrophone implements Runnable{
    private AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
    private AudioInputStream audioInputStream;
    public Thread thread;

    public AudioManagementMicrophone() {
        super();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        thread = null;
    }

    @Override
    public void run() {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); TargetDataLine line = getTargetDataLineForRecording();) {

            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            createByteOutputStream(out, line, bufferLengthInBytes);
            this.audioInputStream = new AudioInputStream(line);
            setAudioInputStream(convertToAudioInputStream(out, frameSizeInBytes));
            audioInputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createByteOutputStream(ByteArrayOutputStream out, TargetDataLine line, int bufferLengthInBytes) {
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        line.start();
        while (thread != null) {
            if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                break;
            }
            out.write(data, 0, numBytesRead);
        }
    }

    private void setAudioInputStream(AudioInputStream recordedAudio) {
        this.audioInputStream = recordedAudio;
    }

    public AudioInputStream convertToAudioInputStream(ByteArrayOutputStream out, int frameSizeInBytes) {
        byte audioBytes[] = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioStream = new AudioInputStream(in, format, audioBytes.length / frameSizeInBytes);

        return audioStream;
    }
    public TargetDataLine getTargetDataLineForRecording() {
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return line;
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }
    public Thread getThread() {
        return thread;
    }

    public AudioFormat getFormat() {
        return format;
    }

    //Testen, obs abspielbar ist
    void playAudio(AudioInputStream audioInputStream){
        try {
            System.out.println("Abspielen...");
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            byte[] bufferBytes = new byte[4096];
            int readBytes = -1;
            while ((readBytes = audioInputStream.read(bufferBytes)) != -1) {
                sourceDataLine.write(bufferBytes, 0, readBytes);
            }
            sourceDataLine.drain();
            sourceDataLine.close();
            audioInputStream.close();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    //Zum Testen: Audio soll aufgenommen werden und danachh abgespielt werden
    public static void main(String[] args) {
        AudioManagementMicrophone recorder = new AudioManagementMicrophone();
        Thread newThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.stop();
            }
        });

        newThread.start();

        recorder.start();
        try{
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recorder.playAudio(recorder.getAudioInputStream());
    }
     
}
