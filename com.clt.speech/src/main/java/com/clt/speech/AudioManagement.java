package com.clt.speech;

import com.clt.speech.recognition.Recognizer;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class AudioManagement {
    private static final int BUFFER_SIZE = 4096;
    private AudioInputStream audioStreamTTS;

    public AudioManagement() {

    }

    public void setAudioStreamTTS(AudioInputStream audioStreamTTS) {
        this.audioStreamTTS = audioStreamTTS;
    }

    public AudioInputStream getAudioStreamTTS() {
        return audioStreamTTS;
    }

    public void playAudio(){
        try {
            AudioFormat audioFormat = audioStreamTTS.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            byte[] bufferBytes = new byte[BUFFER_SIZE];
            int readBytes = -1;
            while ((readBytes = audioStreamTTS.read(bufferBytes)) != -1) {
                sourceDataLine.write(bufferBytes, 0, readBytes);
            }

            sourceDataLine.drain();
            sourceDataLine.close();
            audioStreamTTS.close();
            textAusgeben();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    };

    public void recordAudio() {

    };
    public void textAusgeben() {
        System.out.println("Audio im Audiomanagement!");
    }
}
