package com.nit.nit_jwgl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nit.service.LibraryService;
import com.nit.service.RollCallService;

public class LibraryFragment extends Fragment {
	private ListView lv;
	private TextView tv;
	private String[] items = { "��ǰ����", "��ʷ����", "����" };
	private Button bt_login;
	private LibraryService service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.library_fragment, null);
		init(v);
		setAdapter();
		setListener();
		return v;
	}

	private void init(View v) {
		lv = (ListView) v.findViewById(R.id.lv_lib_main);
		tv = (TextView) v.findViewById(R.id.tv_lib_allLend);
		bt_login = (Button) v.findViewById(R.id.bt_login_lib);
		service = new LibraryService();
		service.setContext(getActivity());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (10 == requestCode && 11 == resultCode
				&& data.getBooleanExtra("isLogin", false)) {
			String name = data.getStringExtra("name");
			bt_login.setText(name + "/ע��");
			tv.setText("�ۼƽ��飺" + data.getStringExtra("count"));
			tv.setVisibility(View.VISIBLE);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.child_layout, R.id.child_text, items);
		lv.setAdapter(adapter);
	}

	public void setListener() {

		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = bt_login.getText().toString().trim();
				String isLogin = text.substring(text.length() - 2);
				if (isLogin.equals("ע��")) {
					service.delSpfCookie();
					tv.setVisibility(View.GONE);
					bt_login.setText("��¼");
				} else {
					Intent intent = new Intent("librarylogin");
					LibraryFragment.this.startActivityForResult(intent, 10);
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
		case "��ǰ����":
			intent = new Intent("libraryborrow");
			intent.putExtra("type", 0);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_left_out);
			break;
		case "��ʷ����":
			intent = new Intent("libraryhistory");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_left_out);
			break;
		case "����":
			intent = new Intent("libraryborrow");
			intent.putExtra("type", 1);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_left_out);
			break;
		default:
			break;
		}
	}

}
