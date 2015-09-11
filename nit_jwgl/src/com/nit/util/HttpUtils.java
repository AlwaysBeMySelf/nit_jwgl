package com.nit.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;


public class HttpUtils {
	private static AsyncHttpClient client = new AsyncHttpClient();
	private static PersistentCookieStore myCookieStore = null;
	static {
		client.setMaxRetriesAndTimeout(3, 10000);
	}

	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}

	public static void post(String url, JsonHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}

	public static void post(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void setCookieStore(Context context) {
		if (null == myCookieStore) {
			myCookieStore = new PersistentCookieStore(context);
			client.setCookieStore(myCookieStore);
		}
	}

	public static PersistentCookieStore getCookieStore() {
		return myCookieStore;
	}

	public static void clearCookieStore() {
		if (null != myCookieStore) {
			myCookieStore.clear();
		}
	}
}
