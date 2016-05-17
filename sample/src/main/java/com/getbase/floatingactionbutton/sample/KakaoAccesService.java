package com.getbase.floatingactionbutton.sample;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class KakaoAccesService extends AccessibilityService {

    public static String kakaoname;

    static final String TAG = "KakaoAccesService";

    MethodCollection mc = new MethodCollection();
    ScreenLockMethod slm = new ScreenLockMethod();

    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            /*case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";*/
        }
        return "default";
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.v(TAG, String.format(
                "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
                getEventType(event), event.getClassName(), event.getPackageName(),
                event.getEventTime(), getEventText(event)));

        try {
            if (event.getPackageName().equals("com.kakao.talk") && event.getClassName().equals("android.app.Notification") && MainActivity.phonelocation.equals("oncradle")) {
                if (getEventText(event).contains(":")) {
                    int i = 0;
                    i = getEventText(event).indexOf(":");
                    String saved = getEventText(event);
                    kakaoname = saved.substring(0, i - 1);

                    if (MainActivity.dbManager.getStatus(String.valueOf(3)).equals("on")) {
                        MainActivity.category = "kakao";
                        MainActivity.isevent = true;

                        slm.BrokeLock(this);//락깨기

                        if (isMyServiceRunning(PopupActivity.class)) {
                            this.stopService(new Intent(this, PopupActivity.class));
                            this.startService(new Intent(this, PopupActivity.class));
                            this.stopService(new Intent(this, PopupNameActivity.class));////
                            this.startService(new Intent(this, PopupNameActivity.class));////
                        } else {
                            this.startService(new Intent(this, PopupActivity.class));
                            this.startService(new Intent(this, PopupNameActivity.class));////
                        }
                    }
                }
                if (getEventText(event).equals("")) {

                    if (MainActivity.dbManager.getStatus(String.valueOf(3)).equals("on")) {
                        MainActivity.category = "kakaoall";
                        MainActivity.isevent = true;

                        slm.BrokeLock(this);//락깨기

                        if (isMyServiceRunning(PopupActivity.class)) {
                            this.stopService(new Intent(this, PopupActivity.class));
                            this.startService(new Intent(this, PopupActivity.class));
                            this.stopService(new Intent(this, PopupNameActivity.class));////
                            this.startService(new Intent(this, PopupNameActivity.class));////
                        } else {
                            this.startService(new Intent(this, PopupActivity.class));
                            this.startService(new Intent(this, PopupNameActivity.class));////
                        }
                    }
                }
            } else // 커스텀앱
            {
                if (MainActivity.dbManager.isExistData(String.valueOf(event.getPackageName())) && !event.getPackageName().equals("com.kakao.talk") && MainActivity.phonelocation.equals("oncradle")) { //db에 존재하면
                    MainActivity.category = "custom";
                    MainActivity.isevent = true;
                    MainActivity.nowcustom = String.valueOf(event.getPackageName());
                    MainActivity.nowappname = MainActivity.dbManager.getAppname(String.valueOf(event.getPackageName()));
                    slm.BrokeLock(this);//락깨기

                    if (isMyServiceRunning(PopupActivity.class)) {
                        this.stopService(new Intent(this, PopupActivity.class));
                        this.startService(new Intent(this, PopupActivity.class));
                        this.stopService(new Intent(this, PopupNameActivity.class));////
                        this.startService(new Intent(this, PopupNameActivity.class));////
                    } else {
                        this.startService(new Intent(this, PopupActivity.class));
                        this.startService(new Intent(this, PopupNameActivity.class));////
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
