package com.getbase.floatingactionbutton.sample;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class DialReceiver extends BroadcastReceiver {

    MethodCollection mc = new MethodCollection();
    ScreenLockMethod slm = new ScreenLockMethod();

    @Override
    public void onReceive(Context context, Intent intent) {
        MyPhoneStateListener phoneListener = new MyPhoneStateListener();
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Intent testActivityIntent = new Intent(context, PopupActivity.class);
        //testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        //testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (MainActivity.dbManager.getStatus(String.valueOf(1)).equals("on") && MainActivity.dialstate.equals("전화옴") && MainActivity.phonelocation.equals("oncradle")) {
            MainActivity.category = "dial";
            MainActivity.isevent = true;

            slm.BrokeLock(context);//락깨기
            mc.StartCloseDialActivity(context);//투명close창

            if (isMyServiceRunning(DialActivity.class, context)) {
                context.stopService(new Intent(context, DialActivity.class));
                context.startService(new Intent(context, DialActivity.class));
                context.stopService(new Intent(context, PopupNameActivity.class));////
                context.startService(new Intent(context, PopupNameActivity.class));////
            } else {
                context.startService(new Intent(context, DialActivity.class));
                context.startService(new Intent(context, PopupNameActivity.class));////
            }
        } else if ((MainActivity.dbManager.getStatus(String.valueOf(1)).equals("on") && MainActivity.dialstate.equals("통화시작")) || MainActivity.phonelocation.equals("oncradle")) { //통화 시작하면 서비스 끝 + 나중에 nfc 재태그되면 서비스 끝
            if (isMyServiceRunning(DialActivity.class, context)) {
                MainActivity.isevent = false;
                context.stopService(new Intent(context, DialActivity.class));
                context.stopService(new Intent(context, PopupNameActivity.class));////
            }
        } else if (MainActivity.dialstate.equals("통화끝거절")) { //통화 끝나거나 거절하면 서비스 끝
            if (isMyServiceRunning(DialActivity.class, context)) {
                MainActivity.isevent = false;
                context.stopService(new Intent(context, DialActivity.class));
                context.stopService(new Intent(context, PopupNameActivity.class));////
            }
        }
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
