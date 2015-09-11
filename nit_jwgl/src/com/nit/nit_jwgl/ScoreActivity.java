package com.nit.nit_jwgl;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nit.bean.Flag;
import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.ScoreService;

public class ScoreActivity extends BackGesture_Activity {
	private ListView lv_score;
	private LinearLayout ll_score;
	private TextView tv_score;
	private ScoreService service;
	private String cookie;
	private Spinner sp_year;
	private Spinner sp_term;
	private Dialog loading;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.score);
		init();
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ScoreActivity.this.finish();
			}
		});
	}

	private void init() {
		((TextView) findViewById(R.id.tv_lable)).setText("³É¼¨²éÑ¯ ");
		ll = (LinearLayout) findViewById(R.id.ll_title_back);
		ll.setVisibility(ViewGroup.VISIBLE);

		cookie = getSharedPreferences("cookie", this.MODE_PRIVATE).getString(
				"cookie_jwgl", "");

		loading = new Dialog(ScoreActivity.this, R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);
		((TextView) loading.findViewById(R.id.tv_loading)).setText("ÕýÔÚ²éÑ¯...");

		lv_score = (ListView) findViewById(R.id.lv_score);
		ll_score = (LinearLayout) findViewById(R.id.ll_score);
		tv_score = (TextView) findViewById(R.id.tv_score);
		sp_term = (Spinner) findViewById(R.id.sp_term);
		sp_year = (Spinner) findViewById(R.id.sp_year);
		service = new ScoreService(ScoreActivity.this, lv_score, ll_score,
				tv_score, R.layout.score_item, R.id.tv_score_item, loading);

		service.setAnim(lv_score, R.anim.list_item_anim);
	}

	public void getHighest(View v) {
		if (cookie.isEmpty()) {
			Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼ÔÙ³¢ÊÔ",
					Toast.LENGTH_SHORT).show();
		} else {
			service.setFlag(Flag.HIGHEST_PATH);
			service.getInfoHistory(Path.SCORE_HIGHEST, cookie);
		}
	}

	public void getUnpass(View v) {
		if (cookie.isEmpty()) {
			Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼ÔÙ³¢ÊÔ",
					Toast.LENGTH_SHORT).show();
		} else {
			service.setFlag(Flag.UNPASS_PATH);
			service.getInfoHistory(Path.SCORE_UNPASS, cookie);
		}
	}

	public void getquery(View v) {
		if (cookie.isEmpty()) {
			Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼ÔÙ³¢ÊÔ",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String year = sp_year.getSelectedItem().toString();
		String term = sp_term.getSelectedItem().toString();
		Map<String, String> values = new HashMap<String, String>();
		if ("".equals(year)) {
			service.setFlag(Flag.HISTORY_PATH);
			service.getInfoHistory(Path.SCORE_HISTORY, cookie);
		} else {
			if ("".equals(term)) {
				values.put("year", year);
				service.setFlag(Flag.YEAR_PATH);
				service.getInfoYearOrTerm(Path.SCORE_YEAR, values, cookie);
			} else {
				values.put("year", year);
				values.put("term", term);
				service.setFlag(Flag.TERM_PATH);
				service.getInfoYearOrTerm(Path.SCORE_TERM, values, cookie);
			}
		}

		// service.getInfo(HISTORY_PATH, cookie);
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

}
