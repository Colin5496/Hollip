package com.getbase.floatingactionbutton.sample;

import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener {
    private String TAG = "CallCatcher";
    public static String dialnumber;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE: //통화끝, 벨소리 울리는 중에 통화 거절
                Log.i(TAG,
                        "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_IDLE "
                                + incomingNumber);
                dialnumber = incomingNumber;
                MainActivity.dialstate = "통화끝거절";
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: //통화시작
                Log.i(TAG,
                        "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_OFFHOOK "
                                + incomingNumber);
                dialnumber = incomingNumber;
                MainActivity.dialstate = "통화시작";
                break;
            case TelephonyManager.CALL_STATE_RINGING: //벨소리 울리는중
                Log.i(TAG,
                        "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_RINGING "
                                + incomingNumber);
                dialnumber = incomingNumber;
                MainActivity.dialstate = "전화옴";
                break;
            default:
                Log.i(TAG,
                        "MyPhoneStateListener->onCallStateChanged() -> default -> "
                                + Integer.toString(state));
                break;
        }
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        switch (serviceState.getState()) {
            case ServiceState.STATE_IN_SERVICE:
                Log.i(TAG,
                        "MyPhoneStateListener->onServiceStateChanged() -> STATE_IN_SERVICE");
                serviceState.setState(ServiceState.STATE_IN_SERVICE);
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                Log.i(TAG,
                        "MyPhoneStateListener->onServiceStateChanged() -> STATE_OUT_OF_SERVICE");
                serviceState.setState(ServiceState.STATE_OUT_OF_SERVICE);
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                Log.i(TAG,
                        "MyPhoneStateListener->onServiceStateChanged() -> STATE_EMERGENCY_ONLY");
                serviceState.setState(ServiceState.STATE_EMERGENCY_ONLY);
                break;
            case ServiceState.STATE_POWER_OFF:
                Log.i(TAG,
                        "MyPhoneStateListener->onServiceStateChanged() -> STATE_POWER_OFF");
                serviceState.setState(ServiceState.STATE_POWER_OFF);
                break;
            default:
                Log.i(TAG,
                        "MyPhoneStateListener->onServiceStateChanged() -> default -> "
                                + Integer.toString(serviceState.getState()));
                break;
        }
    }
}