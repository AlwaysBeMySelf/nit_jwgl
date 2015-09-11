package com.nit.nit_jwgl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.LibraryService;
import com.nit.service.RollCallService;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LibraryLoginActivity extends BackGesture_Activity {
	private LinearLayout ll;
	private EditText et_sid;
	private EditText et_passwd;
	private Dialog loading;
	private LibraryService service;
	private ToggleButton tb_store;
	private boolean logout;
	private boolean store_toggleOn = true;
	private Map<String, String> values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_login);
		init();
		setListener();
	}

	private void init() {
		logout = getIntent().getBooleanExtra("logout", false);

		((TextView) findViewById(R.id.tv_lable)).setText("图书馆名登录");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		loading = new Dialog(LibraryLoginActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("正在登录...");

		et_sid = (EditText) findViewById(R.id.et_library_sid);
		et_passwd = (EditText) findViewById(R.id.et_library_passwd);
		tb_store = (ToggleButton) findViewById(R.id.tb_library_login);

		tb_store.setToggleOn();

		service = new LibraryService();
		service.setLoading(loading);
		service.setContext(this);

		service.getSpfUser(et_sid, et_passwd);
		if (TextUtils.isEmpty(et_sid.getText().toString().trim())
				|| TextUtils.isEmpty(et_passwd.getText().toString().trim())) {
			store_toggleOn = false;
			tb_store.setToggleOff();
		}

	}

	private void setListener() {
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LibraryLoginActivity.this.finish();
			}
		});
		tb_store.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean arg0) {
				store_toggleOn = arg0;
			}
		});
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
			login(values, Path.LIBRARY_LOGIN);
		}
	}

	private void login(final Map<String, String> values, final String LOGIN_PATH) {
		if (loading != null) {
			loading.show();
		}
		HttpUtils.post(LOGIN_PATH, new RequestParams(values),
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
							if (loading.isShowing()) {
								loading.dismiss();
							}
							Map<String, String> data = JsonTool
									.getMessageNameAndCount(response);

							String message = data.get("message");
							String name = data.get("name");
							String count = data.get("count");
							String cookie = data.get("cookie");
							if ("登录成功".equals(message)) {
								Intent back = new Intent();
								back.putExtra("isLogin", true);
								back.putExtra("name", name);
								back.putExtra("count", count);
								LibraryLoginActivity.this.setResult(11, back);

								saveInfo(cookie);

								LibraryLoginActivity.this.finish();
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

	private void saveInfo(String cookie) {
		service.saveSpfCookie(cookie);
		if (store_toggleOn) {
			service.saveSpfUser(values);
		} else {
			service.delSpfUser();
		}
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
