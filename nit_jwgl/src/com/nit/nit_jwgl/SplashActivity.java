package com.nit.nit_jwgl;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		jump();
	}

	private void jump() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent("main");
				intent.putExtra("isNetworkAvailable", isNetworkAvailable());
				startActivity(intent);

				SplashActivity.this.finish();
			}
		}, 500);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo[] netwoInfos = manager.getAllNetworkInfo();
		if (netwoInfos != null && netwoInfos.length > 0) {
			for (int i = 0; i < netwoInfos.length; i++) {
				if (netwoInfos[i].getState() == NetworkInfo.State.CONNECTED)
					return true;
			}
		}
		return false;
	}

}
