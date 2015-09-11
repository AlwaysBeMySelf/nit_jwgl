package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nit.adapter.ResitBaseAdapter;
import com.nit.bean.Resit;
import com.nit.nit_jwgl.R;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class ResitService {
	private TextView tv_info;
	private ListView lv_info;
	private LinearLayout ll_resit;
	private Dialog loading;
	Context context = null;

	public ResitService(Context context) {
		super();
		this.context = context;
	}

	public ResitService(TextView tv_info, ListView lv_info,
			LinearLayout ll_info, LinearLayout ll_resit, Dialog loading,
			Context context) {
		super();
		this.tv_info = tv_info;
		this.lv_info = lv_info;
		this.ll_resit = ll_resit;
		this.context = context;
		this.loading = loading;
	}

	public void getInfo(final String path, final String cookie) {
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
					Map<String, Object> info = JsonTool.getResits(response);
					if (loading.isShowing()) {
						loading.dismiss();
					}
					putInfo(info, R.layout.resit_item);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	public void putInfo(Map<String, Object> info, int resource) {
		String message = (String) info.get("message");
		List<Resit> resits = (List<Resit>) info.get("data");
		if (message.equals("请登录再尝试")) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else if ("暂无补考考试安排".equals(message)) {
			ll_resit.removeView(ll_resit);
			ll_resit.removeView(lv_info);
			tv_info.setText(message);
		} else {
			ll_resit.removeView(tv_info);
			setAdapter(resits, resource);
		}
	}

	public void setAnim(ListView lv, int resource) {
		Animation animation = (Animation) AnimationUtils.loadAnimation(context,
				resource);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setDelay(0.4f); // 设置动画间隔时间
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 设置列表的显示顺序
		lv.setLayoutAnimation(lac);
	}

	public void setAdapter(List<Resit> resits, int resource) {
		ResitBaseAdapter adapter = new ResitBaseAdapter(context, resits,
				resource);
		lv_info.setAdapter(adapter);
	}

	public String getCookie() {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		return preferences.getString("cookie_jwgl", "");
	}
}
