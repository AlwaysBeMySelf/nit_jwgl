package com.nit.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nit.bean.Course;
import com.nit.db.DbOpenHelper;
import com.nit.util.HttpUtils;
import com.nit.util.JsonTool;
import com.nit.view.CircleRefreshLayout;
import com.nit.view.CourseLayout;
import com.nit.view.CourseView;

public class CourseService {

	private Context context;
	private Dialog loading;
	private CourseLayout layout;
	private DbOpenHelper dbOpenHelper;
	private int[] bg;
	private String userID;
	private CircleRefreshLayout mRefreshLayout;
	Timer timer = null;

	public void setmRefreshLayout(CircleRefreshLayout mRefreshLayout) {
		this.mRefreshLayout = mRefreshLayout;
	}

	public CourseService(Context context, Dialog loading, CourseLayout layout,
			int[] bg) {
		super();
		this.context = context;
		this.loading = loading;
		this.layout = layout;
		this.bg = bg;
		dbOpenHelper = new DbOpenHelper(context, "nit.db", null, 1);
		userID = context.getSharedPreferences("login", Context.MODE_PRIVATE)
				.getString("illegalUserID", "");
	}

	public void getInfo(final String path, final String cookie) {

		String userID = context.getSharedPreferences("login",
				Context.MODE_PRIVATE).getString("illegalUserID", "");
		List<Course> courses = getCoursesFromDb(userID);
		if (0 == courses.size()) {
			if (cookie.isEmpty()) {
				Toast.makeText(context, "ÇëµÇÂ¼ÔÙ³¢ÊÔ", Toast.LENGTH_SHORT).show();
				return;
			} else {
				getThroughWeb(path, cookie, false);
			}

		} else {
			putInfo(courses);
		}
	}

	public void getThroughWeb(final String path, final String cookie,
			final boolean flag) {
		if (!loading.isShowing() && !flag) {
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
					if (flag && mRefreshLayout != null) {
						if (null == timer) {
							timer = new Timer();
						}
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								mRefreshLayout.finishRefreshing();
							}
						}, 500);

					}
					Map<String, Object> info = JsonTool.getCourses(response);
					List<Course> courses = (List<Course>) info.get("data");
					addCourses(courses, userID);

					putInfo(courses);

				} catch (JSONException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	public void putInfo(List<Course> courses) {

		Course course = null;
		for (int i = 0; i < courses.size(); i++) {
			course = courses.get(i);
			CourseView view = new CourseView(context);
			view.setStartSection(course.jies[0]);
			view.setEndSection(course.jies[course.jies.length - 1]);
			view.setWeek(course.week);
			Random random = new Random();
			int bgRes = bg[(int) (Math.random() * bg.length)];
			// int bgRes = bg[random.nextInt(bg.length)];

			StringBuffer jie = new StringBuffer();
			for (int tmp : course.jies) {
				jie.append(tmp + ",");
			}
			jie.deleteCharAt(jie.length() - 1);
			view.setBackgroundResource(bgRes);
			view.setText(course.course + "@" + course.room + "@½Ú£º"
					+ jie.toString());
			view.setTextColor(Color.WHITE);
			view.setTextSize(12);
			view.setGravity(Gravity.CENTER);
			layout.addView(view);
		}

	}

	public String getCookie() {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		return preferences.getString("cookie_jwgl", "");
	}

	public void addCourses(List<Course> list, String userID) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = null;
		db.beginTransaction();
		for (Course course : list) {
			values = new ContentValues();
			values.put("course_name", course.course);
			values.put("week", course.week);
			values.put("start_", course.jies[0]);
			values.put("end_", course.jies[course.jies.length - 1]);
			values.put("during", course.during);
			values.put("teacher", course.teacher);
			values.put("room", course.room);
			values.put("userID", userID);
			db.insert("course", null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void delCourses() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("course", null, null);
		db.close();
	}

	public int getAllCourses() {
		int count = 0;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("course", new String[] { "count(*)" }, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}

	public List<Course> getCoursesFromDb(String userID) {
		List<Course> courses = new ArrayList<Course>();

		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("course", null, "userID=?",
				new String[] { userID }, null, null, null);
		while (cursor.moveToNext()) {

			String course_name = cursor.getString(cursor
					.getColumnIndex("course_name"));
			int week = cursor.getInt(cursor.getColumnIndex("week"));
			int start_ = cursor.getInt(cursor.getColumnIndex("start_"));
			int end_ = cursor.getInt(cursor.getColumnIndex("end_"));
			String during = cursor.getString(cursor.getColumnIndex("during"));
			String teacher = cursor.getString(cursor.getColumnIndex("teacher"));
			String room = cursor.getString(cursor.getColumnIndex("room"));

			int[] jies = new int[end_ - start_ + 1];
			for (int i = 0; i < jies.length; i++) {
				jies[i] = start_ + i;
			}
			courses.add(new Course(course_name, week, jies, during, teacher,
					room));
		}
		cursor.close();
		db.close();
		return courses;
	}

	public void delUsers() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("user", null, null);
		db.close();
	}

}
