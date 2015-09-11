package com.nit.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class PwdModifyService {
	private Context context;
	private String pwd;

	public PwdModifyService(Context context) {
		super();
		this.context = context;
		SharedPreferences preferences = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		pwd = preferences.getString("illegalPwd", "");
	}

	public boolean check(String newPwd, String newPwdAgain, String oldPwd,
			TextView tv) {
		StringBuffer message = new StringBuffer();
		int flag = 0;
		if (!checkOldPwd(oldPwd)) {
			message.append("原密码不正确\n");
			flag++;
		}
		if (!checkNewPwd_again(newPwd, newPwdAgain)) {
			message.append("新密码两次输入不一致\n");
			flag++;
		}
		if (checkNewPwd_old(newPwd, oldPwd)) {
			message.append("新旧密码一致");
			flag++;
		}
		tv.setText(message);
		if (0 == flag) {
			return true;

		}
		return false;
	}

	public void modifyPwd(String oldPwd, final String newPwd,
			final String cookie, final Dialog loading, final TextView tv_alert,
			final String path) throws Exception {
		
		RequestParams params = new RequestParams();
		params.put("oldPwd", oldPwd);
		params.put("newPwd", newPwd);
		if (!loading.isShowing()) {
			loading.show();
		}
		HttpUtils.post(path, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				String message;
				try {
					message = (String) response.get("message");
					if (loading.isShowing()) {
						loading.dismiss();
						if ("修改成功！".equals(message)) {
							saveNewPwd(newPwd);
						}
						tv_alert.setText(message);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

	}

	public boolean checkOldPwd(String oldPwd) {
		return pwd.equals(oldPwd) ? true : false;
	}

	public boolean checkNewPwd_again(String newPwd, String newPwdAgain) {
		return newPwd.equals(newPwdAgain) ? true : false;
	}

	public boolean checkNewPwd_old(String newPwd, String oldPwd) {
		return oldPwd.equals(newPwd) ? true : false;
	}

	public String getCookie() {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		return preferences.getString("cookie_jwgl", "");
	}

	public void saveNewPwd(String pwd) {

		SharedPreferences preferences = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString("userPwd", pwd);
		edit.commit();
	}
}
