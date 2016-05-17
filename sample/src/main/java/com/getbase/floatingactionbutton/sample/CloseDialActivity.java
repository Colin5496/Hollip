package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class CloseDialActivity extends Activity {

    MethodCollection mc = new MethodCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_closedial);
    }

    public void onCloseDialBtnClicked(View v) {
        if (mc.isMyServiceRunning(PopupActivity.class, getApplicationContext())) {
            stopService(new Intent(getApplicationContext(), PopupActivity.class));
            stopService(new Intent(getApplicationContext(), PopupNameActivity.class));
        }
        finish();
    }
}
