package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nit.bean.Level;
import com.nit.nit_jwgl.R;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class LevelService {
	Context context;
	private ListView lv;
	private LinearLayout ll;
	private TextView tv;
	private int resource;
	private Dialog loading;
	private String[] from = { "name", "identify", "time", "sumScore",
			"listenScore", "readScore", "writeScore", "integrateScore" };
	private int[] to = { R.id.tv_name, R.id.tv_identify, R.id.tv_time,
			R.id.tv_sumScore, R.id.tv_listenScore, R.id.tv_readScore,
			R.id.tv_writeScore, R.id.tv_integrateScore, };

	public LevelService(Context context, ListView lv, LinearLayout ll,
			TextView tv, int resource, Dialog loading) {
		super();
		this.context = context;
		this.lv = lv;
		this.ll = ll;
		this.tv = tv;
		this.resource = resource;
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
					Map<String, Object> info = JsonTool.getLevels(response);
					if (loading.isShowing()) {
						loading.dismiss();
					}
					putInfo(info);
				} catch (JSONException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	public void setAnim(ListView lv, int resource) {
		Animation animation = (Animation) AnimationUtils.loadAnimation(context,
				resource);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setDelay(0.4f); // 设置动画间隔时间
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 设置列表的显示顺序
		lv.setLayoutAnimation(lac);
	}

	public void setAdapter(Context context, ListView lv, List<Level> levels,
			int resource, String[] from, int[] to) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> value = null;
		Level level = null;
		for (int i = 0; i < levels.size(); i++) {
			value = new HashMap<String, String>();
			level = levels.get(i);
			value.put("name", level.name);
			value.put("identify", level.identify);
			value.put("sumScore", level.sumScore);
			value.put("listenScore", level.listenScore);
			value.put("readScore", level.readScore);
			value.put("writeScore", level.writeScore);
			value.put("integrateScore", level.integrateScore);
			data.add(value);

		}
		SimpleAdapter adapter = new SimpleAdapter(context, data, resource,
				from, to);
		lv.setAdapter(adapter);

	}

	public void putInfo(Map<String, Object> info) {
		String message = (String) info.get("message");
		List<Level> levels = (List<Level>) info.get("data");
		// System.out.println("levels=="+levels.toString());
		if (message.equals("请登录再尝试")) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else if ("暂无等级考试记录".equals(message)) {
			ll.removeView(lv);
			tv.setText(message);
		} else {
			ll.removeView(tv);
			// System.out.println("setAdapter");
			setAdapter(context, lv, levels, resource, from, to);
		}
	}

}
