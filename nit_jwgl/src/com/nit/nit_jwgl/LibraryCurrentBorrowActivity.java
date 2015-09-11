package com.nit.nit_jwgl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nit.adapter.BorrowBookAdapter;
import com.nit.bean.BorrowedBook;
import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.LibraryService;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class LibraryCurrentBorrowActivity extends BackGesture_Activity {
	private TextView tv_alert;
	private ListView lv;
	private LinearLayout ll_title;
	private LinearLayout ll_current;
	private Dialog loading;
	private String cookie;
	private LibraryService service;
	private File cache;
	private List<BorrowedBook> books;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_borrow);
		init();
		setListener();
	}

	private void setListener() {
		ll_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LibraryCurrentBorrowActivity.this.finish();
			}
		});
	}

	private void init() {
		type = getIntent().getIntExtra("type", 0);

		tv_alert = (TextView) findViewById(R.id.tv_lib_alert);
		lv = (ListView) findViewById(R.id.lv_lib_current);
		ll_current = (LinearLayout) findViewById(R.id.ll_lib_current);

		TextView tv = (TextView) findViewById(R.id.tv_lable);
		if (0 == type) {
			tv.setText("当前借阅");
		} else {
			tv.setText("续借");
		}

		ll_title = (LinearLayout) findViewById(R.id.ll_title_back);
		ll_title.setVisibility(ViewGroup.VISIBLE);

		loading = new Dialog(LibraryCurrentBorrowActivity.this,
				R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);

		service = new LibraryService();
		service.setContext(getApplicationContext());
		service.setLoading(loading);
		cookie = service.getCookie();
		if (cookie.isEmpty()) {
			Toast.makeText(LibraryCurrentBorrowActivity.this, "请登录再尝试",
					Toast.LENGTH_SHORT).show();
		} else {
			getCurrentBorrowBook(cookie, Path.LIBRARY_CURRENT_BORROW);
		}
	}

	private void getCurrentBorrowBook(final String cookie,
			final String BORROW_PATH) {

		HttpUtils.post(BORROW_PATH, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				if (loading != null) {
					((TextView) loading.findViewById(R.id.tv_loading))
							.setText("正在读取...");
					loading.show();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				if (0 == statusCode) {
					Toast.makeText(getApplicationContext(), "连接超时",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				if (loading.isShowing()) {
					loading.dismiss();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				try {
					Map<String, Object> values = JsonTool
							.getCurrentBorrowBook(response);
					putData(values);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	private void putData(Map<String, Object> values) {
		String message = (String) values.get("message");
		books = (List<BorrowedBook>) values.get("data");

		if ("当前无借阅记录".equals(message)) {
			ll_current.removeView(tv_alert);
			ll_current.removeView(lv);
			ll_current.addView(tv_alert);
			tv_alert.setText(message);
		} else {
			ll_current.removeView(tv_alert);
			ll_current.removeView(lv);
			ll_current.addView(lv);

			cache = new File(Environment.getExternalStorageDirectory()
					+ "/nit_jwgl", "cache");
			if (!cache.exists()) {
				cache.mkdirs();
			}
			if (1 == type) {
				List<BorrowedBook> tmpBooks = new ArrayList<BorrowedBook>();
				for (int i = 0; i < books.size(); i++) {
					boolean isRenew = Boolean
							.parseBoolean(books.get(i).isRenew);
					if (Boolean.FALSE == isRenew) {
						tmpBooks.add(books.get(i));
					}
				}
				books.clear();
				books = tmpBooks;
			}
			BorrowBookAdapter adapter = new BorrowBookAdapter(
					LibraryCurrentBorrowActivity.this, books,
					R.layout.lib_current_item, cache, cookie, service);
			lv.setAdapter(adapter);
		}
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
