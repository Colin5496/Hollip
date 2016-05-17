package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.DataSetObservable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<dumpclass> data;

	public static PackageManager packageManager;

	public ListAdapter(Activity a, ArrayList<dumpclass> basicList){

		activity	=	a;
		data		=	basicList;
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
		packageManager = activity.getPackageManager();
		String nw;
		if(convertView == null)
        {holder				=	new ViewHolder();
		 convertView		=	LayoutInflater.from(activity).inflate(R.layout.test_layout, null);
		 holder.text		=	(TextView) convertView.findViewById(R.id.mem_info_txt_id);
		 holder.image		=	(ImageView) convertView.findViewById(R.id.mem_photo_img_id);
		 convertView.setTag(holder);
        }else
        	holder			=	(ViewHolder) convertView.getTag();

		nw=data.get(position).sampletext;
        holder.text.setText(nw);


		// Icon
		try {
			if (data.get(position).sampleimg.equals("dial"))
				holder.image.setImageResource(R.drawable.phone_icon);
			else if (data.get(position).sampleimg.equals("sms"))
				holder.image.setImageResource(R.drawable.message_icon);
			else
				holder.image.setImageDrawable(packageManager.getApplicationIcon(data.get(position).sampleimg));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
        
        return convertView;
	}

	
	public static class ViewHolder{
		TextView text;
		ImageView image;

		
	}
}
