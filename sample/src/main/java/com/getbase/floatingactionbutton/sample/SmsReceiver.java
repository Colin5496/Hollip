package com.getbase.floatingactionbutton.sample;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public static String smsnumber;
    public static String smscontent;
    public static String smstime;

    MethodCollection mc = new MethodCollection();
    ScreenLockMethod slm = new ScreenLockMethod();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent testActivityIntent = new Intent(context, PopupActivity.class);
        //testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        //testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //context.startActivity(testActivityIntent);


        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("onReceive()", "부팅완료");
        }
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
            Log.d("onReceive()", "스크린 ON");
        }
        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {
            Log.d("onReceive()", "스크린 OFF");
        }
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.d("onReceive()", "문자가 수신되었습니다");

            // SMS 메시지를 파싱합니다.
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.d("문자 수신 시간", curDate.toString());

            // SMS 발신 번호 확인
            String origNumber = smsMessage[0].getOriginatingAddress();

            // SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            Log.d("문자 내용", "발신자 : " + origNumber + ", 내용 : " + message);

            // abortBroadcast();
            // 우선순위가 낮은 다른 문자 앱이 수신을 받지 못하도록 함
            smsnumber = origNumber;
            smscontent = message;
            smstime = curDate.toString();

            if (MainActivity.dbManager.getStatus(String.valueOf(2)).equals("on") && MainActivity.phonelocation.equals("oncradle")) {
                MainActivity.isevent = true;
                MainActivity.category = "sms";

                slm.BrokeLock(context);//락깨기

                if (isMyServiceRunning(PopupActivity.class, context)) {
                    context.stopService(new Intent(context, PopupActivity.class));
                    context.startService(new Intent(context, PopupActivity.class));
                    context.stopService(new Intent(context, PopupNameActivity.class));////
                    context.startService(new Intent(context, PopupNameActivity.class));////
                } else {
                    context.startService(new Intent(context, PopupActivity.class));
                    context.startService(new Intent(context, PopupNameActivity.class));////
                }
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
