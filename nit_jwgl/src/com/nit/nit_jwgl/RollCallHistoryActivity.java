package com.nit.nit_jwgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Path;
import com.nit.bean.RollCallHis;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.RollCallService;

@SuppressLint("HandlerLeak")
public class RollCallHistoryActivity extends BackGesture_Activity {
	private LinearLayout ll;
	private ListView lv;
	private Dialog loading;
	private RollCallService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rollcall_history);

		inti();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RollCallHistoryActivity.this.finish();
			}
		});

	}

	private void inti() {
		((TextView) findViewById(R.id.tv_lable)).setText("历史记录");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		lv = (ListView) findViewById(R.id.lv_rollcall_history);

		loading = new Dialog(RollCallHistoryActivity.this,
				R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在查询...");

		service = new RollCallService(loading);
		service.setContext(RollCallHistoryActivity.this);
		String cookie = service.getCookie();
		System.out.println("cookie==" + cookie);
		if (cookie.isEmpty()) {
			Toast.makeText(RollCallHistoryActivity.this, "请登录后查询",
					Toast.LENGTH_LONG).show();
		} else {
			service.getHistory(Path.ROLLCALL_HISTORY, cookie, lv);
		}
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
