package com.getbase.floatingactionbutton.sample;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Jimin on 2015-10-05.
 */
public class MethodCollection {
    public MethodCollection() {
    }

    SharedPreferencesMethod spm = new SharedPreferencesMethod();

    public static int getBatteryPercentage(Context context) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;
        return (int) (batteryPct * 100);
    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    public static void StartCloseActivity(Context context) {
        Intent intentclose = new Intent(context, CloseServiceActivity.class);//끄는곳
        intentclose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intentclose);
    }

    public static void StartCloseDialActivity(Context context) {
        Intent intentclose = new Intent(context, CloseDialActivity.class);//끄는곳
        intentclose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intentclose);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
