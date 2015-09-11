package com.nit.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nit.bean.Resit;
import com.nit.nit_jwgl.R;

public class ResitBaseAdapter extends BaseAdapter {

	private List<Resit> resits;
	private int resource;
	private LayoutInflater inflater;

	@Override
	public int getCount() {
		return resits.size();
	}

	public ResitBaseAdapter(Context context, List<Resit> resits, int resource) {
		super();
		System.out.println("ResitBaseAdapter=="+resits.toString());
		this.resits = resits;
		this.resource = resource;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position) {
		return resits.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv_name = null;
		TextView tv_time = null;
		TextView tv_place = null;
		TextView tv_seat = null;
		if (null == convertView) {
			convertView = inflater.inflate(resource, null);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_place = (TextView) convertView.findViewById(R.id.tv_place);
			tv_seat = (TextView) convertView.findViewById(R.id.tv_seat);
			convertView.setTag(new ViewCache(tv_name, tv_time, tv_place,
					tv_seat));
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			tv_name = cache.tv_name;
			tv_time = cache.tv_time;
			tv_place = cache.tv_place;
			tv_seat = cache.tv_seat;
		}
		Resit resit = resits.get(position);
		tv_name.setText(resit.name);
		tv_time.setText(resit.time);
		tv_place.setText(resit.place);
		tv_seat.setText(resit.seat);
		return convertView;
	}

	private final class ViewCache {
		public TextView tv_name = null;
		public TextView tv_time = null;
		public TextView tv_place = null;
		public TextView tv_seat = null;

		public ViewCache(TextView tv_name, TextView tv_time, TextView tv_place,
				TextView tv_seat) {
			super();
			this.tv_name = tv_name;
			this.tv_time = tv_time;
			this.tv_place = tv_place;
			this.tv_seat = tv_seat;
		}
	}
}
