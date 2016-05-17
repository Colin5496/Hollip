package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;


public class CloseServiceActivity extends Activity {

    MethodCollection mc = new MethodCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_closeservice);

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
            finish();
        }
    };

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        stopService(new Intent(getApplicationContext(), WeatherActivity.class));
        stopService(new Intent(getApplicationContext(), ClockActivity.class));
        finish();
    }

    public void onCloseBtnClicked(View v) {
        stopService(new Intent(getApplicationContext(), WeatherActivity.class));
        stopService(new Intent(getApplicationContext(), ClockActivity.class));
        finish();
    }
}
