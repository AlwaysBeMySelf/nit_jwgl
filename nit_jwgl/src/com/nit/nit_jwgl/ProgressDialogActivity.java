package com.nit.nit_jwgl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class ProgressDialogActivity extends Dialog {
	private String message;
	private TextView tv_loading;
	
	public ProgressDialogActivity(Context context,String message) {
		super(context);
		// TODO Auto-generated constructor stub
		this.message=message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		LayoutParams lp=getWindow().getAttributes();
		lp.alpha=0.5f;
		getWindow().setAttributes(lp);*/
		setContentView(R.layout.loading);
		tv_loading=(TextView) findViewById(R.id.tv_loading);
		tv_loading.setText(message);
	}

}
