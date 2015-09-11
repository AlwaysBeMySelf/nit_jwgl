package com.nit.service;

import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ListView;

import com.nit.util.HttpUtils;

public class LoginService {
	String cookieString = null;
	Context context = null;

	public LoginService(Context context) {
		super();
		this.context = context;
	}

	public String getCookie() {
		Cookie cookie = HttpUtils.getCookieStore().getCookies().get(0);
		
		return cookie.getValue();
	}


	public void setAnim(ListView lv, int resource) {
		Animation animation = (Animation) AnimationUtils.loadAnimation(context,
				resource);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setDelay(0.4f); // 设置动画间隔时间
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 设置列表的显示顺序
		lv.setLayoutAnimation(lac);
	}





	public void saveSpfUser(Map<String, String> values) {

		SharedPreferences preferences = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		if (values != null) {

			edit.putString("userID", values.get("userID"));
			edit.putString("userPwd", values.get("userPwd"));
			edit.putString("illegalUserID", values.get("userID"));
			edit.putString("illegalPwd", values.get("userPwd"));
		}
		edit.commit();
	}

	public void delSpfUser() {
		SharedPreferences preferences = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.remove("userID");
		edit.remove("userPwd");
		edit.commit();
	}

	public void saveSpfCookie(String cookie) {
		// System.out.println("cookie===" + cookie);
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString("cookie_jwgl", cookie);
		edit.commit();
	}

	public void delSpfCookie() {
		SharedPreferences preferences = context.getSharedPreferences("cookie",
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.remove("cookie_jwgl");
		edit.commit();
	}

	public void getSharePreferences(EditText et_sid, EditText et_passwd) {

		SharedPreferences preferences = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		et_sid.setText(preferences.getString("userID", null));
		et_passwd.setText(preferences.getString("userPwd", null));
	}
}
