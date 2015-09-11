package com.nit.nit_jwgl;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.Cet46Service;

public class Cet46ScoreActivity extends BackGesture_Activity {
	private TextView tv_info;
	private TextView tv_alert;
	private EditText et_id;
	private EditText et_name;
	private Cet46Service service;
	private Dialog loading;
	private LinearLayout ll;
	private String id;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.cet46_score);
		init();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Cet46ScoreActivity.this.finish();
			}
		});
	}

	private void init() {
		((TextView) findViewById(R.id.tv_lable)).setText("等级考试");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		tv_info = (TextView) findViewById(R.id.tv_cet46_Info);
		tv_alert = (TextView) findViewById(R.id.tv_cet46_alert);
		et_id = (EditText) findViewById(R.id.et_cet46_id);
		et_name = (EditText) findViewById(R.id.et_cet46_name);

		loading = new Dialog(Cet46ScoreActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在查询...");

		service = new Cet46Service(this, tv_info, loading, 0);
	}

	public void getInfo(View v) {
		id = et_id.getText().toString().trim();
		name = et_name.getText().toString().trim();
		String alert = service.isEmpty(id, name);
		tv_alert.setText(alert);
		if (alert.isEmpty()) {
			loading.show();
			Map<String, String> values = new HashMap<String, String>();
			values.put("id", id);
			values.put("name", name);
			service.getInfo(values, Path.CET_46_QUERY);
		}
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
