package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.nit_jwgl.LibraryLoginActivity;
import com.nit.nit_jwgl.R;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LibraryService {
	private Dialog loading;
	private Handler handler;
	private Context context;

	public void setLoading(Dialog loading) {
		this.loading = loading;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void renew(final Map<String, String> values, final String PATH,
			final String cookie) {
		if (loading != null) {
			((TextView) loading.findViewById(R.id.tv_loading))
					.setText("»ðËÙÐø½èÖÐ...");
			loading.show();
		}
		HttpUtils.post(PATH, new RequestParams(values),
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
							Toast.makeText(context,
									JsonTool.getMessage(response),
									Toast.LENGTH_SHORT).show();
						} catch (JSONException | UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	public void saveSpfUser(Map<String, String> values) {

		SharedPreferences preferences = context.getSharedPreferences(
				"librarylogin", Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		if (values != null) {
			edit.putString("userID", values.get("userID"));
			edit.putString("userPwd", values.get("userPwd"));
			edit.commit();
		}
	}

	public void getSpfUser(EditText et_sid, EditText et_passwd) {

		SharedPreferences preferences = context.getSharedPreferences(
				"librarylogin", Context.MODE_PRIVATE);
		et_sid.setText(preferences.getString("userID", null));
		et_passwd.setText(preferences.getString("userPwd", null));
	}

	public void delSpfUser() {

		SharedPreferences preferences = context.getSharedPreferences(
				"librarylogin", Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.clear();
		edit.commit();
	}

	public void saveSpfCookie(String cookie) {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString("cookie_lib", cookie);
		edit.commit();
	}

	public String getCookie() {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		return preferences.getString("cookie_lib", "");
	}

	public void delSpfCookie() {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.remove("cookie_lib");
		edit.commit();
	}

}
