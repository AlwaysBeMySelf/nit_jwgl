package com.nit.nit_jwgl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.LevelService;

public class LevelActivity extends BackGesture_Activity {
	private ListView lv_level;
	private LinearLayout ll_level;
	private TextView tv_level;
	private LevelService service;
	private LinearLayout ll;
	private String cookie;
	private Dialog loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_exam);
		init();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LevelActivity.this.finish();
			}
		});
		loading.show();
		service.getInfo(Path.LEVEL, cookie);
	}
	private void init() {
		((TextView) findViewById(R.id.tv_lable)).setText("等级考试");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);
		
		ll_level=(LinearLayout) findViewById(R.id.ll_level);
		lv_level=(ListView) findViewById(R.id.lv_level);
		tv_level=(TextView) findViewById(R.id.tv_level);
		
		loading = new Dialog(LevelActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在查询...");
		
		service=new LevelService(LevelActivity.this, lv_level, ll_level, tv_level, R.layout.level_exam_item,loading);
		cookie=getSharedPreferences("cookie", Context.MODE_APPEND).getString("cookie_jwgl", "");
		service.setAnim(lv_level, R.anim.list_item_anim);
	}
	@Override
	public void pre(View view) {
		finish();
	}

}
