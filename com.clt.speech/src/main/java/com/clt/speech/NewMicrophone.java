package com.clt.speech;

import javax.sound.sampled.*;
import java.io.InputStream;

public class NewMicrophone {

    private final TargetDataLine targetDataLine;
    private final InputStream inputStream;

    public NewMicrophone(float sampleRate, int sampleSize, int channels, boolean signed, boolean bigEndian) {
        AudioFormat format = new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);
        try {
            targetDataLine = AudioSystem.getTargetDataLine(format);
            targetDataLine.open();
        } catch (LineUnavailableException e) {
            throw new IllegalStateException(e);
        }
        inputStream = new AudioInputStream(targetDataLine);
    }

    public void start() {
        targetDataLine.start();
        System.out.println("Aufnahme gestartet");
    }

    public void stop() {
        targetDataLine.stop();
        System.out.println("Aufnahme gestoppt");
    }

    public void closeConnection() {
        targetDataLine.close();
    }

    public InputStream getStream() {
        return inputStream;
    }
}
