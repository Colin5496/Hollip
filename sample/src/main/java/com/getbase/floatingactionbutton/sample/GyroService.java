package com.getbase.floatingactionbutton.sample;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jimin on 2015-07-21.
 */
public class GyroService extends Service implements SensorEventListener {

    private static final String TAG = "GyroService";
    // 센서 관리자
    public static SensorManager sm = null;
    // 방향 센서
    Sensor oriSensor = null;

    SharedPreferencesMethod spm = new SharedPreferencesMethod();
    MethodCollection mc = new MethodCollection();
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

        // SensorManager 인스턴스를 가져옴
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 방향 센서
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // 방향 센서 리스너 오브젝트를 등록
        sm.registerListener(this, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (166.0 < Math.abs(event.values[1]) && 185.0 >= Math.abs(event.values[1])) {
            MainActivity.phonelocation = "oncradle";
            //Toast.makeText(getApplicationContext(), MainActivity.phonelocation, Toast.LENGTH_LONG).show();
        } else {
            MainActivity.phonelocation = "default";
            stopService(new Intent(getApplicationContext(), DialActivity.class));
            stopService(new Intent(getApplicationContext(), PopupNameActivity.class));
            stopService(new Intent(getApplicationContext(), BlackScreenActivity.class));
        }

        if (MainActivity.phonelocation.equals("oncradle") && spm.getPreferences(this, "maindisplay", "category").equals("clock")) {
            mc.StartCloseActivity(getApplicationContext());//투명close창
            startService(new Intent(getApplicationContext(), ClockActivity.class));
        } else if (MainActivity.phonelocation.equals("oncradle") && spm.getPreferences(this, "maindisplay", "category").equals("weather")) {
            startService(new Intent(getApplicationContext(), OnService.class));
        } else if (MainActivity.phonelocation.equals("oncradle") && spm.getPreferences(this, "maindisplay", "category").equals("none") && !MainActivity.isevent) {
            //slm.LockScreen(this);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

        // 센서에서 이벤트 리스너 분리
        sm.unregisterListener(this);

        stopSelf();
    }
}