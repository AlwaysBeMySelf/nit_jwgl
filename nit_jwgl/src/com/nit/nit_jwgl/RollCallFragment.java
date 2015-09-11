package com.nit.nit_jwgl;

import com.nit.service.RollCallService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RollCallFragment extends Fragment {
	private ListView lv;
	private String[] items = { "点名", "历史记录" };
	private Button bt_login;
	private RollCallService service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.rollcall_fragment, null);
		init(v);
		setAdapter();
		setListener();
		return v;
	}

	private void init(View v) {
		lv = (ListView) v.findViewById(R.id.lv_rollcall_main);
		bt_login = (Button) v.findViewById(R.id.bt_login_rc);
		service = new RollCallService(null);
		service.setContext(getActivity());
	}

	private void setAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.child_layout, R.id.child_text, items);
		lv.setAdapter(adapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (10 == requestCode && 11 == resultCode
				&& data.getBooleanExtra("isLogin", false)) {
			String name = data.getStringExtra("name");
			bt_login.setText(name + "/注销");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void setListener() {

		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = bt_login.getText().toString().trim();
				String isLogin = text.substring(text.length() - 2);
				if (isLogin.equals("注销")) {
					service.delSpfCookie();
					bt_login.setText("登录");
				} else {
					Intent intent = new Intent("rollcalllogin");
					RollCallFragment.this.startActivityForResult(intent, 10);
				}
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				jump(position);
			}
		});
	}

	private void jump(int position) {
		Intent intent = null;
		switch (items[position]) {
		case "点名":
			intent = new Intent("rollcallmain");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_left_out);
			break;
		case "历史记录":
			intent = new Intent("rollcallhis");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_left_out);
			break;
		default:
			break;
		}
	}
}
