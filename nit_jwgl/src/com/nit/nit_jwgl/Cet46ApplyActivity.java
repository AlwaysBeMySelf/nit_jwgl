package com.nit.nit_jwgl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.Cet46Service;

public class Cet46ApplyActivity extends BackGesture_Activity {

	private TextView tv_alert;
	private Cet46Service service;
	private String cookie;
	private LinearLayout ll;
	private Dialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cet46_apply);
		init();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Cet46ApplyActivity.this.finish();
			}
		});
	}

	private void init() {
		((TextView) findViewById(R.id.tv_lable)).setText("CET4/6报名");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		tv_alert = (TextView) findViewById(R.id.tv_cet46_apply_alert);

		loading = new Dialog(Cet46ApplyActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在操作...");

		cookie = getSharedPreferences("cookie", Context.MODE_APPEND).getString(
				"cookie_jwgl", "");
		service = new Cet46Service(Cet46ApplyActivity.this, tv_alert, loading,
				1);
	}

	public void apply(View v) {
		String path = null;
		switch (v.getId()) {
		case R.id.bt_cet4_apply:
			path = Path.CET4;
			break;
		case R.id.bt_cet6_apply:
			path = Path.CET6;
			break;
		case R.id.bt_cet46_cancel:
			path = Path.CET_46_CANCEL;
			break;

		default:
			break;
		}
		if (cookie.isEmpty()) {
			Toast.makeText(getApplicationContext(), "请登录再尝试",
					Toast.LENGTH_SHORT).show();
		} else {
			service.applyCET46(path, cookie);
		}

	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
