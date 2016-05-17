package com.getbase.floatingactionbutton.sample;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jimin on 2015-07-21.
 */
public class OnService extends Service {
    private static final String TAG = "OnService";
    double latitude, longitude;
    long minTime = 1000;
    float minDistance = 0;

    LocationManager manager;
    MyLocationListener[] listener = new MyLocationListener[]{
            new MyLocationListener(),
            new MyLocationListener()
    };

    boolean gps_enabled = false;
    boolean network_enabled = false;
    MethodCollection mc = new MethodCollection();
    TtsMethod tm = new TtsMethod();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        latitude = longitude = 0.0;
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener[0] = new MyLocationListener();
        listener[1] = new MyLocationListener();

        gps_enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled && !network_enabled)
            alertCheckGPS();
        else {
            if (network_enabled)
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener[0]);
            if (gps_enabled)
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener[1]);
        }
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (latitude != 0.0 && longitude != 0.0) {
                Log.e("현재좌표", "Lat / Lon: " + latitude + "\t" + longitude);
                new AsyncTaskParseJson().execute();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(), "onStatus", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "onProvider", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Disabled", Toast.LENGTH_LONG).show();
        }
    }


    private void alertCheckGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS is disabled! Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                        })
                .setNegativeButton("Do nothing",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    public void onWeatherBtnClicked(View v) {
        if (!gps_enabled && !network_enabled)
            alertCheckGPS();
        else {
            if (network_enabled)
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener[0]);
            if (gps_enabled)
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener[1]);
        }
        new AsyncTaskParseJson().execute();
    }

    // ********************************* User ************************* //

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "MainActivity";

        // set your json string url here
        // String yourJsonStringUrl = "http://api.openweathermap.org/data/2.5/weather?lat=37.550713&lon=127.074469";
        String yourJsonStringUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude +"&APPID=d454e7c8e83c35e871075922e926a4ff";


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                // instantiate our json parser
                JsonParser jParser = new JsonParser();

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("weather");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = Integer.parseInt(jsonObject.optString("id").toString());
                    String main = jsonObject.optString("main").toString();
                    String description = jsonObject.optString("description").toString();
                    String icon = jsonObject.optString("icon").toString();

                    //weather = "Node= " + i + " : \n id= " + id + " \n main= " + main + " \n description= " + description + " \n Icon= " + icon;
                    MainActivity.nowweather = icon;

                    /*mc.StartCloseActivity(getApplicationContext());//투명close창
                    startService(new Intent(getApplicationContext(), WeatherActivity.class));*/
                }

                mc.StartCloseActivity(getApplicationContext());//투명close창
                startService(new Intent(getApplicationContext(), WeatherActivity.class));

                // show the values in our logcat
                Log.e("날씨", "RESULT: " + MainActivity.nowweather);
                Log.e("날씨좌표", "Lat / Lon: " + latitude + "\t" + longitude);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            //tv_weather.setText(weather + "");
        }
    }
    @Override
    public void onDestroy() {
        Log.e("OnService:", "onDestroy");
        super.onDestroy();

        stopSelf();

        if (manager != null) {
            for (int i = 0; i < listener.length; i++) {
                try {
                    manager.removeUpdates(listener[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }
}