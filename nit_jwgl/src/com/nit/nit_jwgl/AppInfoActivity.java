package com.nit.nit_jwgl;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nit.gesture.BackGesture_Activity;

public class AppInfoActivity extends BackGesture_Activity {
	private LinearLayout ll;
	private TextView tv_app_name;
	private TextView tv_app_developer;
	private TextView tv_copyright;
	private StringBuffer buffer_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.app_info);

		init();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppInfoActivity.this.finish();
			}
		});
	}

	private void init() {
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		((TextView) findViewById(R.id.tv_lable)).setText("关于");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);
		
		tv_app_name=(TextView) findViewById(R.id.tv_app_name);
		tv_app_developer=(TextView) findViewById(R.id.tv_app_developer);
		tv_copyright=(TextView) findViewById(R.id.tv_copyright);
		
		buffer_version=new StringBuffer();
		buffer_version.append("nit教务管理\n版本号：");
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			buffer_version.append(info.versionName);
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tv_app_name.setText(buffer_version.toString());
		tv_app_developer.setText("开发者\n军师：nitapi.hiunique.com\n大将：距&离\n\n\n友情赞助商：三江潮");
		tv_copyright.setText("All Rights Reserved");
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
