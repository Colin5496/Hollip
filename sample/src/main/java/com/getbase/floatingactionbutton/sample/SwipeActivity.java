package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SwipeActivity extends Activity {

    private ListView cmn_list_view;
    private ListAdapter listAdapter;
    private ArrayList<dumpclass> listdata;

    public static PackageManager packageManager;
    public static DBManager dbManager;
    String applicationName;
    View convertView;

    Intent appList;
    boolean setListValue = false;
    boolean checkOverlap = false;
    ImageButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        btn_add = (ImageButton) findViewById(R.id.btn_add);

        packageManager = this.getPackageManager();
        dbManager = new DBManager(getApplicationContext(), "app.db", null, 1);
        applicationName = "null";

        cmn_list_view = (ListView) findViewById(R.id.cmn_list_view);
        listdata = new ArrayList<dumpclass>();

        InitializeValues();

        final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
                cmn_list_view, swipeListener, this);

        touchListener.SwipeType = ListViewSwipeGesture.Dismiss;
        cmn_list_view.setOnTouchListener(touchListener);
    }

    // Add AppList
    public void onAddBtnClicked(View v) {
        if (applicationName.equals("null")) {
            appList = new Intent(getApplicationContext(), AppInfoActivity.class);
            startActivityForResult(appList, 1); // AppIcon / AppName / OnOffButton ?????? ????.
            setListValue = true; // AppList?? ???.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (setListValue == true) {

                String packageName = data.getStringExtra("packageName"); // Icon ?? packageName???? ??????.
                applicationName = data.getStringExtra("appName");

                checkOverlap = CheckPackageName(packageName);
                if (checkOverlap) {// if packageName Check
                    //Toast.makeText(getApplicationContext(), "�̹� ��ϵ� ��", Toast.LENGTH_LONG).show();
                    applicationName = "null";
                    setListValue = false;
                } else {
                    // list �߰�
                    listdata.add(new dumpclass(applicationName, packageName));
                    dbManager.insert("insert into APP_LIST values(null, '" + packageName + "', '" + applicationName + "', 4, 'on');");

                    listAdapter.notifyDataSetChanged();
                    applicationName = "null";
                }
                setListValue = false;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean CheckPackageName(String packageName) {

        String dbPackage = dbManager.select("select package from APP_LIST where package = '" + packageName + "';");
        if (dbPackage.equals("")) return false; // ��ġ�°� ���°��.
        else return true;
    }
    //

    private void InitializeValues() {

        MainActivity.mainList = dbManager.selectAll();
        for (String element : MainActivity.mainList) {
            String[] split = element.split("/");

            String def_packageName = split[0];
            String def_appName = split[1];
            int def_btn_id = Integer.parseInt(split[2]);
            String def_btn_status = split[3];

            listdata.add(new dumpclass(def_appName, def_packageName));


        }
        // TODO Auto-generated method stub
        listAdapter = new ListAdapter(this, listdata);
        cmn_list_view.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.swipe, menu);
        return true;
    }

    ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

        @Override
        public void FullSwipeListView(int position) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "Action_2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void HalfSwipeListView(int position) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "Action_1", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void LoadDataForScroll(int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            // TODO Auto-generated method stub
            for (int i : reverseSortedPositions) {
                if (i == 0 || i == 1 || i == 2) {
                    Toast.makeText(getApplicationContext(), "Can't delete", Toast.LENGTH_SHORT).show();
                    continue;
                }
                else {
                    String temp_pack = listdata.get(i).getSampleimg();
                    String temp_appName = listdata.get(i).getSampletext();
                    listdata.remove(i);
                    // delete DB
                    dbManager.delete("delete from APP_LIST where package = '" + temp_pack+ "';");
                    listAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "Delete " + temp_appName, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void OnClickListView(int position) {
            // TODO Auto-generated method stub
        }

    };

}
