package com.nit.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nit.bean.BorrowedBook;
import com.nit.bean.Path;
import com.nit.nit_jwgl.R;
import com.nit.service.LibraryService;
import com.nit.util.HttpUtils;
import com.nit.util.MD5;

public class BorrowBookAdapter extends BaseAdapter {
	List<BorrowedBook> data;
	int listItem;
	File cache;
	LayoutInflater inflater;
	Context context;
	String cookie;
	LibraryService service;
	BorrowedBook book;

	public BorrowBookAdapter(Context context, List<BorrowedBook> data,
			int listItem, File cache, String cookie, LibraryService service) {

		this.context = context;
		this.data = data;
		this.listItem = listItem;
		this.cache = cache;
		this.cookie = cookie;
		this.service = service;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView iv;
		TextView tv;
		Button bt;
		if (convertView == null) {
			convertView = inflater.inflate(listItem, null);
			iv = (ImageView) convertView
					.findViewById(com.nit.nit_jwgl.R.id.iv_lib_current_image);
			tv = (TextView) convertView
					.findViewById(com.nit.nit_jwgl.R.id.tv_lib_current_info);
			bt = (Button) convertView
					.findViewById(com.nit.nit_jwgl.R.id.bt_lib_current_renew);
			convertView.setTag(new DataWrapper(iv, tv, bt));
		} else {
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			iv = dataWrapper.imageView;
			tv = dataWrapper.textView;
			bt = dataWrapper.button;
		}
		book = data.get(position);
		tv.setText(change2string(book));
		
		if (!book.image.isEmpty()) {
			asyncImageLoad(iv, book.image);
		}
		if ("true".equals(book.isRenew)) {
			bt.setClickable(false);
			bt.setText("已续借过");
			bt.setBackgroundColor(Color.TRANSPARENT);
		} else {
			bt.setClickable(true);
			bt.setText("续借");
			bt.setBackgroundResource(R.drawable.btn_style_green);
			setListener(bt, book);
		}

		return convertView;
	}

	private void setListener(Button bt, final BorrowedBook book) {
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("警告").setMessage("您确定续借吗？");
				builder.setPositiveButton("续借",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Map<String, String> values = new HashMap<String, String>();
								values.put("bookID", book.code);
								values.put("check", book.check);
								service.renew(values, Path.LIBRARY_RENEW,
										cookie);
							}
						});
				builder.setNegativeButton("取消",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.show();

			}
		});

	}

	private String change2string(BorrowedBook book) {
		return "条形码:" + book.code + "\n书名:" + book.title + "\n借阅日期:"
				+ book.borrowDate + "\n应还日期:" + book.returnDate;
	}

	private void asyncImageLoad(final ImageView imageView, final String path) {
		HttpUtils.get(path, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Uri uri = null;
				// http://img3.douban.com/mpic/s6106663.jpg
				File localFile = new File(cache, MD5.getMD5(path)
						+ path.substring(path.lastIndexOf(".")));
				if (localFile.exists()) {
					uri = Uri.fromFile(localFile);
				} else {
					try {
						FileOutputStream stream = new FileOutputStream(
								localFile);
						stream.write(arg2, 0, arg2.length);
						stream.close();
						uri = Uri.fromFile(localFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != uri && null != imageView) {
					imageView.setImageURI(uri);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	private final class DataWrapper {
		public ImageView imageView;
		public TextView textView;
		public Button button;

		public DataWrapper(ImageView imageView, TextView textView, Button button) {
			this.imageView = imageView;
			this.textView = textView;
			this.button = button;
		}
	}

}
