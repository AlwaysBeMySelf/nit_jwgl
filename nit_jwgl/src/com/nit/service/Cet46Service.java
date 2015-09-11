package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.Cet46;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class Cet46Service {
	Context context;

	private TextView tv;
	// private String id;
	// private String name;

	private Dialog loading;
	private int flag;

	public Cet46Service(Context context, TextView tv, Dialog loading, int flag) {
		super();
		this.context = context;
		this.tv = tv;
		this.loading = loading;
		this.flag = flag;
	}

	public String isEmpty(String id, String name) {
		StringBuffer alert = new StringBuffer();
		if (TextUtils.isEmpty(id)) {
			alert.append("׼��֤��Ϊ��\n");
		}
		if (TextUtils.isEmpty(name)) {
			alert.append("����Ϊ��\n");
		}

		return alert.toString();

	}

	public void getInfo(final Map<String, String> values, final String path) {
		if (!loading.isShowing()) {
			loading.show();
		}
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
							if (loading.isShowing()) {
								loading.dismiss();
							}
							putInfo(JsonTool.getCet46(response));

						} catch (JSONException | UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

	}

	public void applyCET46(final String path, String cookie) {
		if (!loading.isShowing()) {
			loading.show();
		}
		HttpUtils.post(path, new JsonHttpResponseHandler() {

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

					String message = JsonTool.getMessage(response);
					if (message.equals("���¼�ٳ���")) {
						Toast.makeText(context, message, Toast.LENGTH_SHORT)
								.show();
					} else if (message.equals("������Ϣ��ȫ")) {
						tv.setText("������δ��ʼ");
					} else {
						tv.setText(message);
					}

				} catch (JSONException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	public void putInfo(Map<String, Object> info) {
		String message = (String) info.get("message");
		if (message.equals("���¼�ٳ���")) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else if ("��ȡ�ɹ�".equals(message)) {
			List<Cet46> cet46s = (List<Cet46>) info.get("data");
			tv.setText(list2string(cet46s));
		} else {
			tv.setText(message);
		}
	}

	private String list2string(List<Cet46> cet46s) {

		StringBuffer tmp = new StringBuffer();
		Cet46 cet46 = cet46s.get(0);
		tmp.append("\n������" + cet46.name).append("\nѧУ��" + cet46.school)
				.append("\n�������" + cet46.item).append("\n׼��֤�ţ�" + cet46.id)
				.append("\n����ʱ�䣺" + cet46.time).append("\n�ܷ֣�" + cet46.score)
				.append("\n������" + cet46.listening)
				.append("\n�Ķ���" + cet46.reading)
				.append("\nд���뷭�룺" + cet46.writing).append("\n");

		String info = tmp.append("\n").toString();
		// System.out.println("info===" + info);

		return info;
	}

}
