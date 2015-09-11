package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.RollCallHis;
import com.nit.bean.RollCallMenber;
import com.nit.nit_jwgl.LoginActivity;
import com.nit.nit_jwgl.R;
import com.nit.nit_jwgl.RollCallHistoryActivity;
import com.nit.nit_jwgl.RollCallMainActivity;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RollCallService {
	private Dialog loading;
	private Context context;
	private String[] id ;

	public void setContext(Context context) {
		this.context = context;
	}


	public RollCallService(Dialog loading) {
		super();
		this.loading = loading;
	}

	public String[] getId() {
		return id;
	}

	public void getMenber(final String PATH, final String cookie,
			final TextView tv_time, final ListView lv) {
		if (loading != null) {
			loading.show();
		}
		HttpUtils.post(PATH, new JsonHttpResponseHandler() {

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
				if (loading.isShowing()) {
					loading.dismiss();
				}
				try {
					Map<String, Object> menbers = JsonTool
							.getRollCallMenber(response);
					showData(menbers, tv_time, lv);
				} catch (JSONException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	private void showData(Map<String, Object> menbers, TextView tv_time,
			ListView lv) {
		String message = (String) menbers.get("message");

		if (message.equals("请登录再尝试")) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else {

			String date = (String) menbers.get("date");
			List<RollCallMenber> data = (List<RollCallMenber>) menbers
					.get("data");
			if (!message.equals("获取成功")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}

			tv_time.setText("当前日期：" + date);

			String[] names = new String[data.size()];
			id = new String[data.size()];
			for (int i = 0; i < data.size(); i++) {
				names[i] = data.get(i).name;
				id[i] = data.get(i).id;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					R.layout.rollcall_item, R.id.cb_rollcall_name, names);
			lv.setAdapter(adapter);
		}
	}

	public void getHistory(final String PATH, final String cookie,final ListView lv) {
		if (loading != null) {
			loading.show();
		}
		HttpUtils.post(PATH,  new JsonHttpResponseHandler(){

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
				try {
					if (loading.isShowing()) {
						loading.dismiss();
					}
					Map<String, Object> rollCallHis = JsonTool.getRollCAllHis(response);
					showData(rollCallHis, lv);
				} catch (JSONException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	
	}
	private void showData(Map<String, Object> his,ListView lv) {
		String message = (String) his.get("message");
		if (message.equals("请登录再尝试")) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else{
			@SuppressWarnings("unchecked")
			List<RollCallHis> list = (List<RollCallHis>) his.get("data");

			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				map.put("date", list.get(i).date);
				map.put("status", list.get(i).status);
				data.add(map);
			}

			SimpleAdapter adapter = new SimpleAdapter(context,
					data, R.layout.rollcall_history_item, new String[] { "date",
							"status" }, new int[] { R.id.tv_rollcall_his_date,
							R.id.tv_rollcall_his_status });

			lv.setAdapter(adapter);
		}
		
	}
	public void submitMen(final String[] values, final String PATH,
			final String cookie) {
		if (loading != null) {
			loading.show();
		}
		RequestParams params = new RequestParams();
		params.put("persons", values);
		HttpUtils.post(PATH, params, new JsonHttpResponseHandler(){

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
				try {
					if (loading.isShowing()) {
						loading.dismiss();
					}
					Toast.makeText(context, JsonTool.getMessage(response), Toast.LENGTH_SHORT).show();
				} catch (JSONException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	public void saveSharePreferences(Map<String, String> values) {

		SharedPreferences preferences = context.getSharedPreferences(
				"rollcalllogin", Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		if (values != null) {
			edit.putString("userID", values.get("userID"));
			edit.putString("userPwd", values.get("userPwd"));
		} else {
			edit.clear();
		}
		edit.commit();
	}

	public void saveSpAuto(Boolean value) {

		SharedPreferences preferences = context.getSharedPreferences(
				"rollcalllogin", Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putBoolean("autoLogin", value);
		edit.commit();
	}

	public boolean getSpAuto() {

		SharedPreferences preferences = context.getSharedPreferences(
				"rollcalllogin", Context.MODE_PRIVATE);
		return preferences.getBoolean("autoLogin", false);
	}

	public void getSharePreferences(EditText et_sid, EditText et_passwd) {

		SharedPreferences preferences = context.getSharedPreferences(
				"rollcalllogin", Context.MODE_PRIVATE);
		et_sid.setText(preferences.getString("userID", null));
		et_passwd.setText(preferences.getString("userPwd", null));
	}

	public void saveSpfCookie(String cookie) {

		// System.out.println("cookie===" + cookie);
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString("cookie_rollcall", cookie);
		edit.commit();
	}

	public void delSpfCookie() {

		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.remove("cookie_rollcall");
		edit.commit();
	}

	public String getCookie() {

		// System.out.println("cookie===" + cookie);
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		return preferences.getString("cookie_rollcall", "");

	}

}
