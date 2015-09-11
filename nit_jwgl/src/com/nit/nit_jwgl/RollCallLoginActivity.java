package com.nit.nit_jwgl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.RollCallService;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

public class RollCallLoginActivity extends BackGesture_Activity {
	private LinearLayout ll;
	private EditText et_sid;
	private EditText et_passwd;
	private Dialog loading;
	private RollCallService service;
	private ToggleButton tb_store;
	private ToggleButton tb_auto;
	private boolean logout;
	private boolean store_toggleOn = true;
	private boolean auto_toggleOn = true;
	private Map<String, String> values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rollcall_login);

		init();
		setListener();

		if (store_toggleOn && auto_toggleOn && false == logout) {
			login(null);
		}

	}

	private void setListener() {
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RollCallLoginActivity.this.finish();
			}
		});
		tb_store.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean arg0) {
				store_toggleOn = arg0;
			}
		});
		tb_auto.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean arg0) {
				// TODO Auto-generated method stub
				auto_toggleOn = arg0;
			}
		});
	}

	private void init() {
		logout = getIntent().getBooleanExtra("logout", false);

		((TextView) findViewById(R.id.tv_lable)).setText("晚点名登录");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		loading = new Dialog(RollCallLoginActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在登录...");

		service = new RollCallService(loading);

		et_sid = (EditText) findViewById(R.id.et_rollcall_sid);
		et_passwd = (EditText) findViewById(R.id.et_rollcall_passwd);
		tb_store = (ToggleButton) findViewById(R.id.tb_rollcall_login);
		tb_auto = (ToggleButton) findViewById(R.id.tb_rollcall_auto);

		tb_auto.setToggleOn();
		tb_store.setToggleOn();

		service.setContext(this);

		service.getSharePreferences(et_sid, et_passwd);
		if (service.getSpAuto()) {
			auto_toggleOn = true;
			tb_auto.setToggleOn();
		} else {
			auto_toggleOn = false;
			tb_auto.setToggleOff();
		}
		if (TextUtils.isEmpty(et_sid.getText().toString().trim())
				|| TextUtils.isEmpty(et_passwd.getText().toString().trim())) {
			auto_toggleOn = false;
			store_toggleOn = false;
			tb_auto.setToggleOff();
			tb_store.setToggleOff();
		}

	}

	public void login(View v) {
		String sid = et_sid.getText().toString().trim();
		String passwd = et_passwd.getText().toString().trim();
		values = new HashMap<String, String>();
		values.put("userID", sid);
		values.put("userPwd", passwd);
		if (TextUtils.isEmpty(sid) || TextUtils.isEmpty(passwd)) {
			Toast.makeText(getApplicationContext(), "缺少参数", Toast.LENGTH_SHORT)
					.show();
		} else {
			login(values, Path.ROLLCALL_LOGIN);
		}
	}

	private void login(Map<String, String> values, String path) {
		if (loading != null) {
			loading.show();
		}
		HttpUtils.setCookieStore(getApplicationContext());
		HttpUtils.post(path, new RequestParams(values),
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						try {
							Map<String, String> data = JsonTool
									.getMessageNameAndCookie(response);
							if (loading.isShowing()) {
								loading.dismiss();
							}

							String message = data.get("message");
							String name = data.get("name");
							String cookie = data.get("cookie");
							if ("登录成功".equals(message)) {
								Intent back = new Intent();
								back.putExtra("isLogin", true);
								back.putExtra("name", name);
								RollCallLoginActivity.this.setResult(11, back);

								saveInfo(cookie);
								Intent intent = new Intent("rollcallmain");
								Toast.makeText(getApplicationContext(),
										message, Toast.LENGTH_SHORT).show();
								RollCallLoginActivity.this.finish();
								startActivity(intent);
								RollCallLoginActivity.this
										.overridePendingTransition(
												R.anim.activity_right_in,
												R.anim.activity_left_out);
							} else {
								Toast.makeText(getApplicationContext(),
										message, Toast.LENGTH_LONG).show();
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});
	}

	private void saveInfo(String cookie) throws Exception {
		service.saveSpfCookie(cookie);
		if (store_toggleOn) {
			service.saveSharePreferences(values);
		} else {
			service.saveSharePreferences(null);
		}
		service.saveSpAuto(auto_toggleOn);
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
