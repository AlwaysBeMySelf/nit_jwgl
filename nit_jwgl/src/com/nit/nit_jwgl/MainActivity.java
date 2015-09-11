package com.nit.nit_jwgl;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.nit.util.HttpUtils;

public class MainActivity extends FragmentActivity {

	private static Boolean isExit = false;
	private JwFragment jwFragment;
	private RollCallFragment rcFragment;
	private LibraryFragment libFragment;
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((TextView) findViewById(R.id.tv_lable)).setText("功能区");
		netState();

		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		setTabsValue();
	}

	private void netState() {
		boolean netState = getIntent().getBooleanExtra("isNetworkAvailable",
				false);
		if (false == netState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("网络警告").setMessage("没有连接网络");
			builder.setCancelable(false);
			builder.setPositiveButton("连接", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
		}
		// return false;
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#45c01a"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "教务", "晚点名", "图书馆" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (jwFragment == null) {
					jwFragment = new JwFragment();
				}
				return jwFragment;
			case 1:
				if (rcFragment == null) {
					rcFragment = new RollCallFragment();
				}
				return rcFragment;
			case 2:
				if (libFragment == null) {
					libFragment = new LibraryFragment();
				}
				return libFragment;
			default:
				return null;
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000);

		} else {
			exit();
		}
	}

	private void exit() {
		// TODO Auto-generated method stub
		SharedPreferences sp = getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();

		HttpUtils.clearCookieStore();
		finish();
	}
	@Override
	protected void onDestroy() {
		File cache = new File(Environment.getExternalStorageDirectory()
				+ "/nit_jwgl", "cache");
		if (cache != null && cache.exists()) {
			for (File file : cache.listFiles()) {
				file.delete();
			}
			cache.delete();
		}
		super.onDestroy();
	}

}
