package com.getbase.floatingactionbutton.sample;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

/**
 * Created by Jimin on 2015-10-05.
 */
public class ScreenLockMethod {
    public ScreenLockMethod()
    {
    }
    public static final int ADMIN_INTENT = 15;
    public static final String description = "Sample Administrator description";
    public static DevicePolicyManager mDevicePolicyManager;
    public static ComponentName mComponentName;

    public static void LockEnable(Context context)
    {
        mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, MyAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
        //context.startActivityForResult(intent, ADMIN_INTENT);
        //context.startActivity(intent, ADMIN_INTENT);
        context.startActivity(intent);
        //context.startActivityForResult(intent, 0);
    }

    public static void LockDisable(Context context)
    {
        mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, MyAdminReceiver.class);
        mDevicePolicyManager.removeActiveAdmin(mComponentName);
    }

    public static void LockScreen(Context context)
    {
        mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, MyAdminReceiver.class);
        boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
        if (isAdmin) {
            mDevicePolicyManager.lockNow();
        }else{
        }
    }

    public static void BrokeLock(Context context)
    {
        if(!isScreenOn(context))
        {
            Intent intent = new Intent(context, BrokeLockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }
}
