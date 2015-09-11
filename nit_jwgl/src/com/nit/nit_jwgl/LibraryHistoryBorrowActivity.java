package com.nit.nit_jwgl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nit.bean.BorrowedBook;
import com.nit.bean.Path;
import com.nit.gesture.BackGesture_Activity;
import com.nit.service.LibraryService;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;

public class LibraryHistoryBorrowActivity extends BackGesture_Activity {
	private TextView tv_alert;
	private ListView lv;
	private LinearLayout ll_title;
	private LinearLayout ll_current;
	private Dialog loading;
	private String cookie;
	private LibraryService service;
	private List<String> books;
	private boolean loadfinish = true;
	View footer;
	private int currentPage;
	ArrayAdapter<String> adapter;

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
				LibraryHistoryBorrowActivity.this.finish();
			}
		});
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		finish();
	}

	private void init() {
		footer = getLayoutInflater().inflate(R.layout.lv_footer, null);
		tv_alert = (TextView) findViewById(R.id.tv_lib_alert);
		lv = (ListView) findViewById(R.id.lv_lib_current);
		ll_current = (LinearLayout) findViewById(R.id.ll_lib_current);
		lv.setOnScrollListener(new ScrollListener());
		lv.addFooterView(footer);

		((TextView) findViewById(R.id.tv_lable)).setText("历史记录");

		ll_title = (LinearLayout) findViewById(R.id.ll_title_back);
		ll_title.setVisibility(ViewGroup.VISIBLE);

		loading = new Dialog(LibraryHistoryBorrowActivity.this,
				R.style.MyDialogStyle);
		loading.setContentView(R.layout.loading);
		loading.setCanceledOnTouchOutside(false);

		books = new ArrayList<String>();

		service = new LibraryService();
		service.setContext(LibraryHistoryBorrowActivity.this);
		service.setLoading(loading);
		cookie = "PHPSESSID=" + service.getCookie();
		currentPage = 1;
		if ("PHPSESSID=".equals(cookie)) {
			Toast.makeText(LibraryHistoryBorrowActivity.this, "请登录再尝试",
					Toast.LENGTH_SHORT).show();
		} else {
			getHistoryBookByPage(cookie, Path.LIBRARY_HISTORY_PATH, currentPage);
		}
	}

	private void getHistoryBookByPage(String cookie, String path,
			final int currentPage) {
		// TODO Auto-generated method stub
		if (loading != null&&1==currentPage) {
			((TextView) loading.findViewById(R.id.tv_loading))
					.setText("正在读取...");
			loading.show();
		}
		RequestParams params = new RequestParams();
		params.add("page", currentPage + "");
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
				if (loading.isShowing()) {
					loading.dismiss();
				}
				try {
					Map<String, Object> data = JsonTool
							.getHistoryBorrowBook(response);
					if (1 == currentPage) {
						putData(data, 0);
					} else {
						putData(data, 1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	private void putData(Map<String, Object> values, int what) {
		String message = (String) values.get("message");
		if (0 == what) {
			if ("当前无借阅记录".equals(message)) {
				ll_current.removeView(tv_alert);
				ll_current.removeView(lv);
				ll_current.addView(tv_alert);
				tv_alert.setText(message);
			} else {
				ll_current.removeView(tv_alert);
				ll_current.removeView(lv);
				ll_current.addView(lv);

				addBookString((List<BorrowedBook>) values.get("data"));
				setAdapter();

			}
		}
		if (1 == what) {
			if (lv.getFooterViewsCount() > 0)
				lv.removeFooterView(footer);
			if ("当前无借阅记录".equals(message)) {
				Toast.makeText(LibraryHistoryBorrowActivity.this, "所有记录加载完毕",
						Toast.LENGTH_SHORT).show();
				return;
			}
			List<BorrowedBook> dataBooks = (List<BorrowedBook>) values
					.get("data");
			addBookString(dataBooks);
			adapter.notifyDataSetChanged();

			if (dataBooks.size() < 20) {
				Toast.makeText(LibraryHistoryBorrowActivity.this, "所有记录加载完毕",
						Toast.LENGTH_SHORT).show();
				loadfinish = false;
			} else {
				loadfinish = true;
			}
		}
	}

	private void setAdapter() {
		adapter = new ArrayAdapter<String>(LibraryHistoryBorrowActivity.this,
				R.layout.lib_history_item, R.id.tv_lib_history_item, books);
		lv.setAdapter(adapter);
	}

	private void addBookString(List<BorrowedBook> list) {
		for (int i = 0; i < list.size(); i++) {
			BorrowedBook book = list.get(i);
			books.add("条形码:  " + book.code + "\n书名:  " + book.title + "\n作者:  "
					+ book.author + "\n借阅日期：  " + book.borrowDate + "\n应还日期：  "
					+ book.returnDate);
		}

	}

	private final class ScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			int lastItemid = lv.getLastVisiblePosition();// 获取当前屏幕最后Item的ID
			if ((lastItemid + 1) == totalItemCount) {// 达到数据的最后一条记录
				if (totalItemCount > 0 && loadfinish) {
					// 当前页
					loadfinish = false;
					lv.addFooterView(footer);
					currentPage++;
					getHistoryBookByPage(cookie,
							Path.LIBRARY_HISTORY_PATH, currentPage);

				}

			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}
	}
}
