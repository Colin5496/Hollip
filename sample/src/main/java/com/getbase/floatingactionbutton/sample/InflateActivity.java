package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InflateActivity extends Activity {

    public static TextView tv_appName;
    public static ImageButton btn_onoffButton;
    public static ImageView img_appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inflate);

        tv_appName = (TextView) findViewById(R.id.tv_appName);
        btn_onoffButton = (ImageButton) findViewById(R.id.btn_onoffButton);
        img_appIcon = (ImageView) findViewById(R.id.img_appIcon);

        tv_appName.setText("fuck you");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
