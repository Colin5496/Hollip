package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends Activity {

    Button btn_appinfo;
    public static TextView tv_selectedapp;
    public static String selectedapplist;
    SharedPreferencesMethod spm = new SharedPreferencesMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_selectedapp = (TextView) findViewById(R.id.tv_selectedapp);

        spm.splitPreferences(this);
        selectedapplist = "";
        for (int i = 0; i < MainActivity.selectedapp; i++) {
            selectedapplist = selectedapplist + "\n" + MainActivity.spl[i];
        }
        tv_selectedapp.setText(selectedapplist);

        btn_appinfo = (Button) findViewById(R.id.btn_appinfo);
        btn_appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AppInfoActivity.class));
            }
        });
    }
}