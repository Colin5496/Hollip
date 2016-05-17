package com.getbase.floatingactionbutton.sample;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by Jimin on 2015-07-21.
 */
public class SpottingService extends Service implements RecognitionListener {

    private static final String TAG = "SpottingService";
    TtsMethod tm = new TtsMethod();

    private static final String KWS_SEARCH = "wakeup";
    //private static final String KEYPHRASE = "hello(2)";
    private static final String KEYPHRASE = "computer";
    //private static final String KEYPHRASE = "narcisse";
    //private static final String KEYPHRASE = "marron";

    private SpeechRecognizer recognizer;

    private Timer timer;
    ScreenLockMethod slm = new ScreenLockMethod();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        /*try {
            Assets assets = new Assets(getApplicationContext());
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        } catch (IOException e) {
        }*/

        stopService(new Intent(getApplicationContext(), SttService.class));

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(getApplicationContext());
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        String text = hypothesis.getHypstr();
        if (text.contains(KEYPHRASE)) {
            //makeText(getApplicationContext(), "지민짱", Toast.LENGTH_SHORT).show();
            //tm.DoTts(getApplicationContext(), "말씀하세요");
            /*slm.BrokeLock(getApplicationContext());//락깨기
            startService(new Intent(getApplicationContext(), WaitActivity.class));*/
            recognizer.stop();
            recognizer = null;

            timer = new Timer();
            timer.schedule(new MyTimer(), 500);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            //String text = hypothesis.getHypstr();
            //makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
        //switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();
        recognizer.startListening(searchName);
    }

    private void setupRecognizer(File assetsDir) {
        File modelsDir = new File(assetsDir, "models");
        recognizer = defaultSetup()
                .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                .setRawLogDir(assetsDir).setKeywordThreshold(1e-20f)
                .getRecognizer();
        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        switchSearch(KWS_SEARCH);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (recognizer != null) {
            recognizer.stop();
            recognizer = null;
        }

        stopSelf();
    }

    private class MyTimer extends TimerTask {
        @Override
        public void run() {
            slm.BrokeLock(getApplicationContext());//락깨기
            startService(new Intent(getApplicationContext(), WaitActivity.class));
            startService(new Intent(getApplicationContext(), SttService.class));
            stopSelf();
        }
    }
}