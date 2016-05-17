package com.getbase.floatingactionbutton.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class DialActivity extends Service {
    private View mView;
    GLSurfaceView view;
    private WindowManager mManager;

    VideoView vv_dial;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.activity_dial, null);

        stopService(new Intent(getApplicationContext(), OnService.class));

        vv_dial = (VideoView) mView.findViewById(R.id.vv_dial);

        if (MainActivity.category.equals("dial")) {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/video_dial");
            vv_dial.setVideoURI(video);
        }

        MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (MainActivity.category.equals("dial")) {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/video_dial");
                    vv_dial.setVideoURI(video);
                }
                vv_dial.start();
            }
        };
        //리스너 등록
        vv_dial.setOnCompletionListener(mComplete);
        //비디오 시작

        timer = new Timer();//sendData 타이머
        timer.schedule(new MyTimer(), 1000);

        vv_dial.start();

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.LEFT | Gravity.RIGHT | Gravity.TOP;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);

        /*Thread thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(8000);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        };
        thread.start();*/
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //ConfirmOver();
            //Log.e("최상위 : ", topactivityname);
            //if (MainActivity.category.equals("sms") || MainActivity.category.equals("kakao") || MainActivity.category.equals("kakaoall")) {
            stopSelf();
            //}
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    private class MyTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.sendData(getApplicationContext(), "dial");
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}