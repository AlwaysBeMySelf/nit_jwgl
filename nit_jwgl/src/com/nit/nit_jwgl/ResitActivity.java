package com.nit.nit_jwgl;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.ResitService;

public class ResitActivity extends BackGesture_Activity {
	private TextView tv_info;
	private TextView tv_lable;
	private ListView lv_info;
	private LinearLayout ll_info;
	private LinearLayout ll_resit;
	private ResitService service;
	private LinearLayout ll_tille;
	private String cookie;
	private Dialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.resit);
		init();
		ll_tille.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ResitActivity.this.finish();
			}
		});
	}

	private void init() {
		tv_lable = (TextView) findViewById(R.id.tv_lable);
		ll_tille = (LinearLayout) findViewById(R.id.ll_title_back);
		ll_tille.setVisibility(ViewGroup.VISIBLE);

		int type = getIntent().getIntExtra("exam_type", 1);
		tv_info = (TextView) findViewById(R.id.tv_resit);
		if (0 == type)
			tv_lable.setText("补考信息");
		else
			tv_lable.setText("考试信息");
		lv_info = (ListView) findViewById(R.id.lv_resit);
		ll_info = (LinearLayout) findViewById(R.id.ll_info);
		ll_resit = (LinearLayout) findViewById(R.id.ll_resit);

		loading = new Dialog(ResitActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在查询...");

		service = new ResitService(tv_info, lv_info, ll_info, ll_resit,
				loading, ResitActivity.this);
		service.setAnim(lv_info, R.anim.list_item_anim);

		cookie = service.getCookie();
		if (cookie.isEmpty()) {
			Toast.makeText(this, "请登录再尝试", Toast.LENGTH_SHORT).show();
		} else {
			if (0 == type) {
				tv_lable.setText("补考信息");
				service.getInfo(Path.RESIT, cookie);
			} else {
				tv_lable.setText("考试信息");
				service.getInfo(Path.EXAM, cookie);
			}
		}

	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
