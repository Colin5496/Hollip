package com.getbase.floatingactionbutton.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class BlackScreenActivity extends Service {
    private View mView;
    GLSurfaceView view;
    private WindowManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.activity_blackscreen, null);

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

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}