package edu.cmu.lti.dialogos.sphinx.client;

import com.clt.speech.AudioManagementMicrophone;
import com.clt.speech.NewMicrophone;
import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;

import java.io.IOException;
import java.io.InputStream;

/**
 * takes a SphinxContext and configures the speech recognizer accordingly
 *
 * Created by timo on 30.10.17.
 */
public class ConfigurableSpeechRecognizer extends AbstractSpeechRecognizer {

    private Microphone microphone;
    private NewMicrophone newMicrophone;
    public ConfigurableSpeechRecognizer(Context context, InputStream audioSource) throws IOException {
        super(context);
        recognizer.allocate();

        /*recognizer.addStateListener(new StateListener() {
            @Override public void statusChanged(edu.cmu.sphinx.recognizer.Recognizer.State status) {
                System.err.println("Sphinx recognition listener defined in ConfigurableSpeechRecognizer.java: " + status);
            }
            @Override public void newProperties(PropertySheet ps) throws PropertyException { }
        });*/

        StreamDataSource sds = context.getInstance(StreamDataSource.class);
        if (audioSource != null) {
            sds.setInputStream(audioSource);
        } else {
            //microphone = context.getInstance(Microphone.class);
            //microphone.initialize();
            //NewMikrophone instanziieren und seinen InputStream übergebem
            newMicrophone = new NewMicrophone(16000,8, 2,true, false);
            sds.setInputStream(newMicrophone.getStream());
        }
    }

    public synchronized void startRecognition() {
        if (recognizer.getState() == Recognizer.State.DEALLOCATED)
            recognizer.allocate();
        //if (microphone != null)
            //microphone.startRecording();
        //neues Mikrofon
        newMicrophone.start();
    }

    public synchronized void stopRecognition() {
        //if (microphone != null && microphone.isRecording())
            //microphone.stopRecording();
        //neues Mikrofon
        newMicrophone.stop();
    }

    public synchronized void resetRecognition() {
        stopRecognition();
        if (recognizer.getState() != Recognizer.State.DEALLOCATED)
            recognizer.deallocate();
    }

    /**
     * Returns result of the recognition.
     *
     * @return recognition result or {@code null} if there is no result, e.g., because the
     * 			microphone or input stream has been closed
     */
    @Override
    public SpeechResult getResult() {
        Result result = recognizer.recognize();
        return null == result ? null : new SpeechResult(result);
    }


}
