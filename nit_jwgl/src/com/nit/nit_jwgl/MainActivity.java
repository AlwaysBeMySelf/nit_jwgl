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
	 * ��ȡ��ǰ��Ļ���ܶ�
	 */
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((TextView) findViewById(R.id.tv_lable)).setText("������");
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
			builder.setTitle("���羯��").setMessage("û����������");
			builder.setCancelable(false);
			builder.setPositiveButton("����", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {

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
	 * ��PagerSlidingTabStrip�ĸ������Խ��и�ֵ��
	 */
	private void setTabsValue() {
		// ����Tab���Զ��������Ļ��
		tabs.setShouldExpand(true);
		// ����Tab�ķָ�����͸����
		tabs.setDividerColor(Color.TRANSPARENT);
		// ����Tab�ײ��ߵĸ߶�
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// ����Tab Indicator�ĸ߶�
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// ����Tab�������ֵĴ�С
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// ����Tab Indicator����ɫ
		tabs.setIndicatorColor(Color.parseColor("#45c01a"));
		// ����ѡ��Tab���ֵ���ɫ (�������Զ����һ������)
		tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// ȡ�����Tabʱ�ı���ɫ
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "����", "�����", "ͼ���" };

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
			isExit = true; // ׼���˳�
			Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // ȡ���˳�
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
