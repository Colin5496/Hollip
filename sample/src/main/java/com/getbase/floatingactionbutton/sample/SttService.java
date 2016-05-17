package com.getbase.floatingactionbutton.sample;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jimin on 2015-07-21.
 */
public class SttService extends Service {

    private static final String TAG = "SttService";
    SpeechRecognizer mRecognizer;
    Intent intent_voice;
    TtsMethod tm = new TtsMethod();
    ScreenLockMethod slm = new ScreenLockMethod();
    MethodCollection mc = new MethodCollection();
    private Timer timer;
    boolean iscorrect = false;

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

        stopService(new Intent(getApplicationContext(), SpottingService.class));

        intent_voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent_voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent_voice);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer = null;
        }

        stopSelf();
    }

    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void onRmsChanged(float rmsdB) {
            // TODO Auto-generated method stub
            Log.i("순서", "onRmsChanged");
        }

        @Override
        public void onResults(Bundle results) {
            // TODO Auto-generated method stub
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            /*if (rs[0].contains("시계") || rs[0].contains("몇 시")) {
                slm.BrokeLock(getApplicationContext());//락깨기
                mc.StartCloseActivity(getApplicationContext());//투명close창
                startService(new Intent(getApplicationContext(), ClockActivity.class));

                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else if (rs[0].contains("날씨")) {
                slm.BrokeLock(getApplicationContext());//락깨기
                startService(new Intent(getApplicationContext(), OnService.class));

                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else if (rs[0].contains("홀드")) {
                slm.LockScreen(getApplicationContext());

                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else if (rs[0].contains("고마워")) {
                tm.DoTts(getApplicationContext(), "아닙니다 주인님");
                startService(new Intent(getApplicationContext(), SpottingService.class));

                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else // 맞는 단어 없으면 다시..
            {
                startService(new Intent(getApplicationContext(), SttService.class));
                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            }*/
            if (rs[0].contains("시계") || rs[0].contains("몇 시")) {
                iscorrect = true;
                //tm.DoTts(getApplicationContext(), "시간이다");
                slm.BrokeLock(getApplicationContext());//락깨기
                mc.StartCloseActivity(getApplicationContext());//투명close창
                startService(new Intent(getApplicationContext(), ClockActivity.class));
                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else if (rs[0].contains("날씨")) {
                iscorrect = true;
                //tm.DoTts(getApplicationContext(), "날씨다");
                slm.BrokeLock(getApplicationContext());//락깨기
                //startService(new Intent(getApplicationContext(), OnService.class));

                mc.StartCloseActivity(getApplicationContext());//투명close창
                startService(new Intent(getApplicationContext(), WeatherActivity.class));

                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else if (rs[0].contains("홀드")) {
                iscorrect = true;
                tm.DoTts(getApplicationContext(), "잠금이다");
                timer = new Timer();
                timer.schedule(new MyTimer(), 1500);
            } else if (rs[0].contains("고마워")) {
                tm.DoTts(getApplicationContext(), "아닙니다 주인님");
                startService(new Intent(getApplicationContext(), SpottingService.class));
                stopSelf();
            } else // 맞는 단어 없으면 다시..
            {
                stopSelf();
                startService(new Intent(getApplicationContext(), SttService.class));
            }
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            // TODO Auto-generated method stub
            Log.i("순서", "onReadyForSpeech");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // TODO Auto-generated method stub
            Log.i("순서", "onPartialResults");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // TODO Auto-generated method stub
            Log.i("순서", "onEvent");
        }

        @Override
        public void onError(int error) {
            // TODO Auto-generated method stub
            //Log.i("순서", "onError");
            /*switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    Log.i("에러", "오디오 입력 중 오류 발생");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    Log.i("에러", "단말에서 오류 발생");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    Log.i("에러", "권한 없음");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    Log.i("에러", "일치하는 항목 없음");
                    if (mRecognizer != null) {
                        mRecognizer.destroy();
                        mRecognizer = null;
                    }
                    intent_voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent_voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                    intent_voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                    mRecognizer.setRecognitionListener(listener);
                    mRecognizer.startListening(intent_voice);
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    Log.i("에러", "음성인식 서비스 과부하@@@@@@@@@@@@@@@@@@@@");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    Log.i("에러", "서버 오류 발생");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    Log.i("에러", "입력 없음");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
            }*/
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    Log.i("에러", "오디오 입력 중 오류 발생");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    Log.i("에러", "단말에서 오류 발생");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    Log.i("에러", "권한 없음");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    Log.i("에러", "일치하는 항목 없음");
                    if (mRecognizer != null) {
                        mRecognizer.destroy();
                        mRecognizer = null;
                    }
                    intent_voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent_voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                    intent_voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                    mRecognizer.setRecognitionListener(listener);
                    mRecognizer.startListening(intent_voice);
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    Log.i("에러", "음성인식 서비스 과부하@@@@@@@@@@@@@@@@@@@@");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    Log.i("에러", "서버 오류 발생");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    Log.i("에러", "입력 없음");
                    startService(new Intent(getApplicationContext(), SpottingService.class));
                    stopSelf();
                    break;
            }
        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub
            Log.i("순서", "onEndOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // TODO Auto-generated method stub
            Log.i("순서", "onBufferReceived");
        }

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub
            Log.i("순서", "onBeginningOfSpeech");
        }
    };

    private class MyTimer extends TimerTask {
        @Override
        public void run() {
            stopSelf();
            startService(new Intent(getApplicationContext(), SttService.class));
        }
    }

}