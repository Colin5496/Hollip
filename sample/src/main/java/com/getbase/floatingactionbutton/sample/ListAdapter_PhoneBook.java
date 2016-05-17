package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter_PhoneBook extends BaseAdapter {
    private Activity activity;
    private ArrayList<phoneBook> data;

    public static PackageManager packageManager;

    public ListAdapter_PhoneBook(Activity a, ArrayList<phoneBook> basicList) {

        activity = a;
        data = basicList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        String userName, userNumber;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.testspam_layout, null);
            holder.name = (TextView) convertView.findViewById(R.id.mem_name);
            holder.number = (TextView) convertView.findViewById(R.id.mem_number);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        userName = data.get(position).sampleName;
        holder.name.setText(userName);

        userNumber = data.get(position).sampleNumber;
        holder.number.setText(userNumber);


        return convertView;
    }


    public static class ViewHolder {
        TextView name, number;

    }
}
