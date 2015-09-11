package com.nit.nit_jwgl;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.PwdModifyService;

public class PwdModifyActivity extends BackGesture_Activity {
	private EditText et_oldPwd;
	private EditText et_newPwd;
	private EditText et_newPwdAgain;
	private TextView tv_alert;
	private Dialog loading;
	private PwdModifyService service;
	private String cookie;
	private String newPwd;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.pwd_modify);
		init();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PwdModifyActivity.this.finish();
			}
		});

	}

	private void init() {
		((TextView) findViewById(R.id.tv_lable)).setText("ÐÞ¸ÄÃÜÂë ");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		loading = new Dialog(PwdModifyActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("ÕýÔÚÐÞ¸Ä...");

		et_oldPwd = (EditText) findViewById(R.id.et_oldPwd);
		et_newPwd = (EditText) findViewById(R.id.et_newPwd);
		et_newPwdAgain = (EditText) findViewById(R.id.et_newPwdAgain);
		tv_alert = (TextView) findViewById(R.id.tv_pwdAlert);
		service = new PwdModifyService(PwdModifyActivity.this);
		cookie = service.getCookie();
	}

	public void modify(View v) {
		String oldPwd = et_oldPwd.getText().toString().trim();
		newPwd = et_newPwd.getText().toString().trim();
		String newPwdAgain = et_newPwdAgain.getText().toString().trim();
		if (!cookie.isEmpty()) {
			if (service.check(newPwd, newPwdAgain, oldPwd, tv_alert)) {
				try {
					service.modifyPwd(oldPwd, newPwd, cookie, loading,
							tv_alert, Path.PASSWORD_MODIFY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			Toast.makeText(getApplicationContext(), "ÇëµÇÂ½ÔÙ³¢ÊÔ",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void pre(View view) {
		finish();
	}
}
