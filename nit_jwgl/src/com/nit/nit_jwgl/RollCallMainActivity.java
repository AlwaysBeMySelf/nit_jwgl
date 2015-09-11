package com.nit.nit_jwgl;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Path;
import com.nit.bean.RollCallMenber;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.RollCallService;

public class RollCallMainActivity extends BackGesture_Activity {

	private ListView lv;
	private RollCallService service;
	private TextView tv_time;
	private LinearLayout ll;
	private TextView tv_logout;
	private Dialog loading;
	private String cookie;
	private String[] id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rollcall_main);
		init();
		setListener();
	}

	private void setListener() {
		tv_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("rollcalllogin");
				intent.putExtra("logout", true);
				service.delSpfCookie();
				RollCallMainActivity.this.finish();
				startActivity(intent);
			}
		});
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RollCallMainActivity.this.finish();
			}
		});
	}

	private void init() {

		lv = (ListView) findViewById(R.id.lv_rollcall_menber);
		tv_time = (TextView) findViewById(R.id.tv_rollcall_time);

		((TextView) findViewById(R.id.tv_lable)).setText("晚点名");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		tv_logout = (TextView) findViewById(R.id.bt_title_reflash);
		tv_logout.setText("注销");
		tv_logout.setVisibility(ViewGroup.VISIBLE);

		loading = new Dialog(RollCallMainActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在操作...");

		service = new RollCallService(loading);
		service.setContext(this);
		cookie = service.getCookie();
		if (!cookie.isEmpty()) {
			service.getMenber(Path.ROLLCALL_MEMBER, cookie, tv_time, lv);
		} else {
			Toast.makeText(RollCallMainActivity.this, "请登录再尝试",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void submit(View v) {
		int count = lv.getChildCount();
		String id[] = service.getId();
		int j = 0;
		String[] tmp = new String[count];
		for (int i = 0; i < count; i++) {
			View view = lv.getChildAt(i);
			if (view instanceof LinearLayout) {
				CheckBox checkBox = (CheckBox) ((LinearLayout) view)
						.findViewById(R.id.cb_rollcall_name);
				if (checkBox.isChecked()) {
					tmp[j] = id[i];
					j++;
				}
			}
		}

		int i;
		for (i = 0; i < tmp.length && null != tmp[i]; i++)
			;
		String usefullId[] = new String[i];
		for (int k = 0; k < i; k++) {
			usefullId[k] = tmp[k];
		}
		service.submitMen(usefullId, Path.ROLLCALL_SUBMIT, cookie);
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
