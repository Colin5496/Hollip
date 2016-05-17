package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppInfoActivity extends Activity {
    private static final String TAG = AppInfoActivity.class.getSimpleName();

    private final int MENU_DOWNLOAD = 0;
    private final int MENU_ALL = 1;
    private int MENU_MODE = MENU_DOWNLOAD;

    private PackageManager pm;

    private View mLoadingContainer;
    private ListView mListView = null;
    private IAAdapter mAdapter = null;

    ViewHolder holder;
    LayoutInflater inflater;

    LinearLayout inflatedLayout, addLinear; // Main
    String appIconString; //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);


        mLoadingContainer = findViewById(R.id.loading_container);
        mListView = (ListView) findViewById(R.id.listView1);

        mAdapter = new IAAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                // Ŭ���� ���� ������ MainActivity�� �Ѱ������.

                String package_name = ((TextView) view.findViewById(R.id.app_package)).getText().toString();
                String applicationName = ((TextView) view.findViewById(R.id.app_name)).getText().toString();
                ImageView appIcon = ((ImageView) view.findViewById(R.id.app_icon));

                // MainActivity�� �� ����.
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                main.putExtra("appName", applicationName);
                main.putExtra("packageName", package_name);
                setResult(RESULT_OK, main);
                finish();
            }
        });

        // Main
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTask();
    }

    private void startTask() {
        new AppTask().execute();
    }


    private void setLoadingView(boolean isView) {
        if (isView) {
            mLoadingContainer.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mLoadingContainer.setVisibility(View.GONE);
        }
    }

    private class ViewHolder {
        // App Icon
        public ImageView mIcon;
        // App Name
        public TextView mName;
        // App Package Name
        public TextView mPacakge;
    }

    private class IAAdapter extends BaseAdapter {
        private Context mContext = null;

        private List<ApplicationInfo> mAppList = null;
        private ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();

        public IAAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public int getCount() {
            return mListData.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_layout, null);

                holder.mIcon = (ImageView) convertView
                        .findViewById(R.id.app_icon);
                holder.mName = (TextView) convertView
                        .findViewById(R.id.app_name);
                holder.mPacakge = (TextView) convertView
                        .findViewById(R.id.app_package);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppInfo data = mListData.get(position);

            if (data.mIcon != null) {
                holder.mIcon.setImageDrawable(data.mIcon);
            }

            holder.mName.setText(data.mAppName);
            holder.mPacakge.setText(data.mAppPackage);

            return convertView;
        }

        public void rebuild() {
            if (mAppList == null) {

                Log.d(TAG, "Is Empty Application List");
                pm = AppInfoActivity.this.getPackageManager();

                mAppList = pm
                        .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
                                | PackageManager.GET_DISABLED_COMPONENTS);
            }

            AppInfo.AppFilter filter;
            switch (MENU_MODE) {
                case MENU_DOWNLOAD:
                    filter = AppInfo.THIRD_PARTY_FILTER;
                    break;
                default:
                    filter = null;
                    break;
            }

            if (filter != null) {
                filter.init();
            }

            mListData.clear();

            AppInfo addInfo = null;
            ApplicationInfo info = null;
            for (ApplicationInfo app : mAppList) {
                info = app;

                if (filter == null || filter.filterApp(info)) {

                    addInfo = new AppInfo();
                    // App Icon
                    addInfo.mIcon = app.loadIcon(pm);
                    // App Name
                    addInfo.mAppName = app.loadLabel(pm).toString();
                    // App Package Name
                    addInfo.mAppPackage = app.packageName;
                    mListData.add(addInfo);
                }
            }

            // ????? ??????? ???(???, ????)
            Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
        }
    }


    private class AppTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            setLoadingView(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAdapter.rebuild();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            setLoadingView(false);
        }

    }

    ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, MENU_DOWNLOAD, 1, R.string.menu_download);
//		menu.add(0, MENU_ALL, 2, R.string.menu_all);

        return true;
    }

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (MENU_MODE == MENU_DOWNLOAD) {
            menu.findItem(MENU_DOWNLOAD).setVisible(false);
            menu.findItem(MENU_ALL).setVisible(true);
        } else {
            menu.findItem(MENU_DOWNLOAD).setVisible(true);
            menu.findItem(MENU_ALL).setVisible(false);
        }

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == MENU_DOWNLOAD) {
            MENU_MODE = MENU_DOWNLOAD;
        } else {
            MENU_MODE = MENU_ALL;
        }

        startTask();

        return true;
    }
}