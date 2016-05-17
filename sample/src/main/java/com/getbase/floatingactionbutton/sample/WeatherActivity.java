package com.getbase.floatingactionbutton.sample;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends Service {
    private View mView;
    GLSurfaceView view;
    private WindowManager mManager;

    VideoView vv_weather;
    TtsMethod tm = new TtsMethod();

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.activity_weather, null);

        stopService(new Intent(getApplicationContext(), OnService.class));
        stopService(new Intent(getApplicationContext(), BlackScreenActivity.class));

        tm.DoTts(getApplicationContext(), "현재 날씨는 구름조금 입니다.");

        MainActivity.nowweather = "03d";
        //stopService(new Intent(getApplicationContext(), OnService.class));

        vv_weather = (VideoView) mView.findViewById(R.id.vv_weather);

        if (MainActivity.nowweather.equals("01d") || MainActivity.nowweather.equals("01n"))//해
        {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_sun");
            vv_weather.setVideoURI(video);
        } else if (MainActivity.nowweather.equals("02d") || MainActivity.nowweather.equals("02n") || MainActivity.nowweather.equals("50d") || MainActivity.nowweather.equals("50n"))//구름조금
        {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_sun");
            vv_weather.setVideoURI(video);
            //tm.DoTts(getApplicationContext(), "현재는 맑음 입니다.");
        } else if (MainActivity.nowweather.equals("03d") || MainActivity.nowweather.equals("03n") || MainActivity.nowweather.equals("04d") || MainActivity.nowweather.equals("04n"))//구름많이
        {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_lotcloud");
            vv_weather.setVideoURI(video);
            //tm.DoTts(getApplicationContext(), "현재는 구름 입니다.");
        } else if (MainActivity.nowweather.equals("09d") || MainActivity.nowweather.equals("09n") || MainActivity.nowweather.equals("10d") || MainActivity.nowweather.equals("10n") || MainActivity.nowweather.equals("11d") || MainActivity.nowweather.equals("11n"))//비
        {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_rain");
            vv_weather.setVideoURI(video);
            //tm.DoTts(getApplicationContext(), "현재는 비 입니다.");
        } else if (MainActivity.nowweather.equals("13d") || MainActivity.nowweather.equals("13n"))//눈
        {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_snow");
            vv_weather.setVideoURI(video);
            //tm.DoTts(getApplicationContext(), "현재는 눈 입니다.");
        }

        MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (MainActivity.nowweather.equals("01d") || MainActivity.nowweather.equals("01n"))//해
                {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_sun");
                    vv_weather.setVideoURI(video);
                } else if (MainActivity.nowweather.equals("02d") || MainActivity.nowweather.equals("02n") || MainActivity.nowweather.equals("50d") || MainActivity.nowweather.equals("50n"))//구름조금
                {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_sun");
                    vv_weather.setVideoURI(video);
                } else if (MainActivity.nowweather.equals("03d") || MainActivity.nowweather.equals("03n") || MainActivity.nowweather.equals("04d") || MainActivity.nowweather.equals("04n"))//구름많이
                {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_lotcloud");
                    vv_weather.setVideoURI(video);
                } else if (MainActivity.nowweather.equals("09d") || MainActivity.nowweather.equals("09n") || MainActivity.nowweather.equals("10d") || MainActivity.nowweather.equals("10n") || MainActivity.nowweather.equals("11d") || MainActivity.nowweather.equals("11n"))//비
                {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_rain");
                    vv_weather.setVideoURI(video);
                } else if (MainActivity.nowweather.equals("13d") || MainActivity.nowweather.equals("13n"))//눈
                {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/weather_snow");
                    vv_weather.setVideoURI(video);
                }
                vv_weather.start();
            }
        };
        //리스너 등록
        vv_weather.setOnCompletionListener(mComplete);
        //비디오 시작
        vv_weather.start();

        MainActivity.countvoice = 0;

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.LEFT | Gravity.RIGHT | Gravity.TOP;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);


        Thread thread = new Thread() {
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
        thread.start();
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
        if (MainActivity.phonelocation.equals("oncradle")) {
            startService(new Intent(getApplicationContext(), BlackScreenActivity.class));
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}