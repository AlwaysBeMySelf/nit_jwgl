package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.Flag;
import com.nit.bean.Score;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class ScoreService {
	Context context;
	private ListView lv;
	private LinearLayout ll;
	private TextView tv;
	private int flag;
	private int resource;
	private int textViewResourceId;
	private Dialog loading;

	public ScoreService(Context context, ListView lv, LinearLayout ll,
			TextView tv, int resource, int textViewResourceId, Dialog loading) {
		super();
		this.context = context;
		this.lv = lv;
		this.ll = ll;
		this.tv = tv;
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
		this.loading = loading;
	}

	public void getInfoHistory(final String path, final String cookie) {
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
					Map<String, Object> info = getJson2Map(response);
					if (loading.isShowing()) {
						loading.dismiss();
					}
					putInfo(info);
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

	public void getInfoYearOrTerm(final String path,
			final Map<String, String> values, final String cookie) {
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
							Map<String, Object> info = getJson2Map(response);
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

	private Map<String, Object> getJson2Map(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = null;
		switch (flag) {
		case Flag.UNPASS_PATH:
		case Flag.HIGHEST_PATH:
			info = JsonTool.getScoreUnpassOrHighest(jsonObject);
			break;
		case Flag.HISTORY_PATH:
		case Flag.TERM_PATH:
		case Flag.YEAR_PATH:
			info = JsonTool.getScoreHistoryOrYearOrTerm(jsonObject);
			break;
		default:
			break;
		}
		return info;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void putInfo(Map<String, Object> info) {
		String message = (String) info.get("message");
		List<Score> scores = (List<Score>) info.get("data");
		String[] strings = list2string(scores);
		if (message.equals("请登录再尝试")) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else if ("无未通过课程成绩记录".equals(message)) {
			ll.removeView(tv);
			ll.removeView(lv);
			ll.addView(tv);
			tv.setText(message);
		} else {
			ll.removeView(tv);
			ll.removeView(lv);
			ll.addView(lv);
			setAdapter(strings);
		}
	}

	public void setAdapter(String[] strings) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				resource, textViewResourceId, strings);
		lv.setAdapter(adapter);
	}

	public void setAnim(ListView lv, int resource) {
		Animation animation = (Animation) AnimationUtils.loadAnimation(context,
				resource);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setDelay(0.4f); // 设置动画间隔时间
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 设置列表的显示顺序
		lv.setLayoutAnimation(lac);
	}

	private String[] list2string(List<Score> scores) {
		String[] scoreStrings = new String[scores.size()];
		StringBuffer tmp = null;
		Score score = null;
		for (int i = 0; i < scores.size(); i++) {
			tmp = new StringBuffer();
			score = scores.get(i);
			if (score.name != null && !"".equals(score.name)) {
				tmp.append("\n课程名称:" + score.name);
			}
			if (score.year != null & !"".equals(score.year)) {
				tmp.append("\n学年:" + score.year);
			}
			if (score.term != null & !"".equals(score.term)) {
				tmp.append("\n学期:" + score.term);
			}
			if (score.property != null & !"".equals(score.property)) {
				tmp.append("\n课程性质:" + score.property);
			}
			if (score.credit != null & !"".equals(score.credit)) {
				tmp.append("\n学分:" + score.credit);
			}
			if (score.point != null & !"".equals(score.point)) {
				tmp.append("\n绩点:" + score.point);
			}
			if (score.score != null & !"".equals(score.score)) {
				tmp.append("\n成绩:" + score.score);
			}
			if (score.reScore != null & !"".equals(score.reScore)) {
				tmp.append("\n补考成绩:" + score.reScore);
			}
			if (score.rebuildScore != null & !"".equals(score.rebuildScore)) {
				tmp.append("\n重修成绩:" + score.rebuildScore);
			}
			if (score.highestScore != null & !"".equals(score.highestScore)) {
				tmp.append("\n最高成绩值:" + score.highestScore);
			}
			scoreStrings[i] = tmp.append("\n").toString();
			// System.out.println("scoreStrings===" + scoreStrings[i]);
		}
		return scoreStrings;
	}
}
