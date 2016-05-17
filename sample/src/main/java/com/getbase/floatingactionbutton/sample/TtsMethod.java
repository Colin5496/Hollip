package com.getbase.floatingactionbutton.sample;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by Jimin on 2015-10-15.
 */
public class TtsMethod implements TextToSpeech.OnInitListener {
    public TtsMethod() {
    }

    public static TextToSpeech myTTS;
    String myText;

    public void DoTts(Context context, String content) {
        myText = content;
        myTTS = new TextToSpeech(context, this);
        myTTS.setSpeechRate(1);

    }


    public void onInit(int status) {
        myTTS.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
    }

}
