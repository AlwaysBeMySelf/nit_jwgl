package com.nit.nit_jwgl;

import java.util.Timer;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.CourseService;
import com.nit.view.CircleRefreshLayout;
import com.nit.view.CourseLayout;

public class CoursesActivity extends BackGesture_Activity {
	// 某节课的背景图,用于随机获取
	private int[] bg = { R.drawable.kb1, R.drawable.kb2, R.drawable.kb3,
			R.drawable.kb4, R.drawable.kb5, R.drawable.kb6, R.drawable.kb7,
			R.drawable.kb8, R.drawable.kb9, R.drawable.kb10, R.drawable.kb11,
			R.drawable.kb12, R.drawable.kb13 };
	private CourseService service;
	private CourseLayout layout;
	private String cookie;
	private Dialog loading;
	private LinearLayout ll;
	private TextView bt_reflash;
	private CircleRefreshLayout mRefreshLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courses);
		init();
		setListener();
	}
	private void refresh(boolean flag) {
		if (cookie.isEmpty()) {
			Toast.makeText(CoursesActivity.this, "请登录再尝试",
					Toast.LENGTH_SHORT).show();
			return;
		}
		service.delCourses();
		service.getThroughWeb(Path.COURSE, cookie,flag);
	}
	private void setListener() {
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CoursesActivity.this.finish();
			}
		});
		bt_reflash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refresh(false);
			}
		});
		
		mRefreshLayout
		.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
			@Override
			public void refreshing() {
				// do something when refresh starts
				refresh(true);
				
			}

			@Override
			public void completeRefresh() {
				// do something when refresh complete
			}
		});
	}

	private void init() {
		((TextView) findViewById(R.id.tv_lable)).setText("课表 ");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		bt_reflash = (TextView) findViewById(R.id.bt_title_reflash);
		ll.setVisibility(ViewGroup.VISIBLE);
		bt_reflash.setVisibility(ViewGroup.VISIBLE);

		mRefreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
		
		loading = new Dialog(CoursesActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在获取...");

		layout = (CourseLayout) findViewById(R.id.courses);

		service = new CourseService(CoursesActivity.this, loading, layout, bg);
		service.setmRefreshLayout(mRefreshLayout);
		cookie = service.getCookie();

		service.getInfo(Path.COURSE, cookie);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (service.getAllCourses() >= 100) {
			service.delCourses();
		}

	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
