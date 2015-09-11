package com.nit.nit_jwgl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.LoginService;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BackGesture_Activity {

	private LinearLayout ll;
	private EditText et_sid;
	private EditText et_passwd;
	private EditText et_check_num;
	private TextView tv_lable;
	private ImageView iv;
	private Button bt;
	private Dialog loading;
	private ToggleButton tb;
	Map<String, String> values = null;
	private LoginService service = null;
	private boolean toggleOn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login);
		init();
		setListener();
		et_check_num.setOnEditorActionListener(new MyOnEditorActionListener());
	}

	private void setListener() {
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPic(Path.CHECKCODE, iv);
				et_check_num.setText(null);
			}
		});
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
		tb.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean arg0) {
				toggleOn = arg0;
			}
		});
	}

	private class MyOnEditorActionListener implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (EditorInfo.IME_ACTION_GO == actionId
					|| KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
				login(bt);
			}
			return true;
		}

	}

	private void init() {
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title);
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		service = new LoginService(getApplicationContext());
		et_sid = (EditText) findViewById(R.id.et_sid);
		et_passwd = (EditText) findViewById(R.id.et_passwd);
		et_check_num = (EditText) findViewById(R.id.et_check_num);
		tb = (ToggleButton) findViewById(R.id.tb_jw_login);
		tb.setToggleOn();
		bt = (Button) findViewById(R.id.bt_login);
		iv = (ImageView) findViewById(R.id.iv);
		tv_lable = (TextView) findViewById(R.id.tv_lable);
		tv_lable.setText("教务登录");

		loading = new Dialog(LoginActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);

		setPic(Path.CHECKCODE, iv);
		service.getSharePreferences(et_sid, et_passwd);

	}

	public void login(View v) {
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputmanger.hideSoftInputFromWindow(getWindow().peekDecorView()
				.getWindowToken(), 0);

		String sid = et_sid.getText().toString();
		String passwd = et_passwd.getText().toString();
		String check = et_check_num.getText().toString();

		values = new HashMap<String, String>();
		values.put("userID", sid);
		values.put("userPwd", passwd);
		values.put("checkCode", check);
		if (TextUtils.isEmpty(sid) || TextUtils.isEmpty(passwd)
				|| TextUtils.isEmpty(check)) {
			Toast.makeText(getApplicationContext(), "缺少参数", Toast.LENGTH_SHORT)
					.show();
		} else {
			loading.show();
			setLogin(values, Path.JW_LOGIN);
		}
	}

	private void setPic(final String PATH, final ImageView iv) {
		HttpUtils.setCookieStore(getApplicationContext());
		HttpUtils.get(PATH, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				iv.setImageBitmap(BitmapFactory.decodeByteArray(arg2, 0,
						arg2.length));

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setLogin(final Map<String, String> values,
			final String LOGIN_PATH) {

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
						if (loading.isShowing()) {
							loading.dismiss();
						}

						String message = null;
						String name = null;
						try {
							Map<String, String> data = JsonTool
									.getMessageAndName(response);
							message = data.get("message");
							name = data.get("name");
							Toast.makeText(LoginActivity.this, message,
									Toast.LENGTH_SHORT).show();
							if ("登录成功".equals(message)) {
								Intent back = new Intent();
								back.putExtra("isLogin", true);
								back.putExtra("name", name);
								LoginActivity.this.setResult(9, back);

								service.saveSpfCookie(service.getCookie());
								if (toggleOn) {
									service.saveSpfUser(values);
								} else {
									service.delSpfUser();
								}
								LoginActivity.this.finish();
							} else {
								setPic(Path.CHECKCODE, iv);
								et_check_num.setText(null);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				});
	}

	@Override
	protected void onStop() {
		super.onStop();
		LoginActivity.this.finish();
	}

	@Override
	public void pre(View view) {
		finish();
	}
}
