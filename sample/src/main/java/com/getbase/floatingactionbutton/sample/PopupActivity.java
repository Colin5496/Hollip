package com.getbase.floatingactionbutton.sample;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PopupActivity extends Service {
    private View mView;
    GLSurfaceView view;
    private WindowManager mManager;
    private ArrayList<Contact> contactlist;
    int i = 0;
    String topactivityname;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.activity_popup, null);


        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                //WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.LEFT | Gravity.RIGHT | Gravity.TOP;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //mManager.addView(mView, mParams);

        if (MainActivity.category.equals("main"))
            return;
        else if (MainActivity.category.equals("dial")) {
            view = new GLSurfaceView(this);
            MyRenderer renderer = new MyRenderer(this);
            view.setRenderer(renderer);
            mManager.addView(view, mParams);

            MainActivity.sendData(getApplicationContext(), "dial");

            //this.startService(new Intent(this, PopupNameActivity.class));
        } else if (MainActivity.category.equals("sms")) {
            view = new GLSurfaceView(this);
            MyRenderer renderer = new MyRenderer(this);
            view.setRenderer(renderer);
            mManager.addView(view, mParams);

            MainActivity.sendData(getApplicationContext(), "sms");

            //this.startService(new Intent(this, PopupNameActivity.class));
        } else if (MainActivity.category.equals("kakao")) {
            view = new GLSurfaceView(this);
            MyRenderer renderer = new MyRenderer(this);
            view.setRenderer(renderer);
            mManager.addView(view, mParams);

            MainActivity.sendData(getApplicationContext(), "kakao");

            //this.startService(new Intent(this, PopupNameActivity.class));
        } else if (MainActivity.category.equals("kakaoall")) {
            view = new GLSurfaceView(this);
            MyRenderer renderer = new MyRenderer(this);
            view.setRenderer(renderer);
            mManager.addView(view, mParams);

            MainActivity.sendData(getApplicationContext(), "kakao");

            //this.startService(new Intent(this, PopupNameActivity.class));
        } else if (MainActivity.category.equals("custom")) {
            view = new GLSurfaceView(this);
            MyRenderer renderer = new MyRenderer(this);
            view.setRenderer(renderer);
            mManager.addView(view, mParams);

            MainActivity.sendData(getApplicationContext(), "custom");

        }

        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(7000);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        };
        thread.start();

            /*view = new GLSurfaceView(this);
            MyRenderer renderer = new MyRenderer(this);
            view.setRenderer(renderer);
            mManager.addView(view, mParams);*/
    }

    public String MatchName(String number) {
        contactlist = getContactList();

        for (i = 0; i < contactlist.size(); i++) {
            if (contactlist.get(i).getPhonenum().equals(number)) {
                return contactlist.get(i).getName();
            }
        }
        return "없는번호";
    }

    public ArrayList<Contact> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        //Cursor contactCursor = managedQuery(uri, projection, null, selectionArgs, sortOrder);

        Cursor contactCursor = this.getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                        /*phonenumber = phonenumber.substring(0, 3) + "-"
                                + phonenumber.substring(3, 6) + "-"
                                + phonenumber.substring(6);*/
                } else if (phonenumber.length() > 8) {
                        /*phonenumber = phonenumber.substring(0, 3) + "-"
                                + phonenumber.substring(3, 7) + "-"
                                + phonenumber.substring(7);*/
                }

                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));

                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }

        return contactlist;
    }

    public void ConfirmOver() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);

        ComponentName topActivity = Info.get(0).topActivity;

        topactivityname = topActivity.getPackageName();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //ConfirmOver();
            //Log.e("최상위 : ", topactivityname);
            if (MainActivity.category.equals("sms") || MainActivity.category.equals("kakao") || MainActivity.category.equals("kakaoall") || MainActivity.category.equals("custom")) {
                MainActivity.isevent = false;
                stopSelf();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (view != null) {
            mManager.removeView(view);
            view = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}