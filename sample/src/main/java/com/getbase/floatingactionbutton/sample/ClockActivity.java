package com.getbase.floatingactionbutton.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClockActivity extends Service {
    private View mView;
    GLSurfaceView view;
    private WindowManager mManager;

    TextView tv_clocktop;
    TextView tv_clockcenter;
    TextView tv_clockbottom;
    TtsMethod tm = new TtsMethod();
    AnalogClock analog_clock;

    boolean first = true;
    boolean afternoon = false;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.activity_clock, null);

        stopService(new Intent(getApplicationContext(), BlackScreenActivity.class));

        tv_clocktop = (TextView) mView.findViewById(R.id.tv_clocktop);
        tv_clockcenter = (TextView) mView.findViewById(R.id.tv_clockcenter);
        tv_clockbottom = (TextView) mView.findViewById(R.id.tv_clockbottom);
        analog_clock = (AnalogClock) mView.findViewById(R.id.analog_clock);


        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                //WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
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

        Thread thread2 = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    handler2.sendEmptyMessage(0);
                }
            }
        };
        thread2.start();
    }

    public void SetTime() {
        Calendar c = Calendar.getInstance();

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        if (Integer.parseInt(df.format(c.getTime()).substring(0, 2)) > 12) {
            formattedDate = "0" + String.valueOf(Integer.parseInt(df.format(c.getTime()).substring(0, 2)) - 12) + df.format(c.getTime()).substring(2, 8);
            afternoon = true;
        }

        ((ReverseAbleRelativeLayout) mView.findViewById(R.id.reverse_clock)).setReverse(true);
        tv_clockcenter.setRotation(-90);
        analog_clock.setRotation(-90);
        tv_clockcenter.setText(formattedDate);
        if (first) {
            if (afternoon) {
                tm.DoTts(getApplicationContext(), "오후 " + formattedDate.substring(0, 2) + "시 " + formattedDate.substring(3, 5) + "분 입니다.");
            } else {
                tm.DoTts(getApplicationContext(), "오전 " + formattedDate.substring(0, 2) + "시 " + formattedDate.substring(3, 5) + "분 입니다.");
            }

            first = false;
        }
        // tv_clockcenter.setGravity(Gravity.CENTER);
        //tv_clockcenter.setTextSize(35);
        // tv_clockcenter.setTextSize(30);
        //setContentView(tv_clockcenter);
        MainActivity.countvoice = 0;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //ConfirmOver();
            //Log.e("최상위 : ", topactivityname);
            //if (MainActivity.category.equals("sms") || MainActivity.category.equals("kakao") || MainActivity.category.equals("kakaoall")) {
            first = true;
            stopSelf();
            //}
        }
    };

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mView != null) {
                SetTime();
            }
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