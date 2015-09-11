package com.nit.nit_jwgl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.nit.adapter.MianBaseExpandableListAdapter;
import com.nit.service.LoginService;

public class JwFragment extends Fragment {
	private String[][] child_text_array = new String[][] { { "修改登录密码" },
			{ "期末考试", "补考信息" }, { "课程成绩", "等级成绩" },
			{ "CET-4/6查询", "CET-4/6报名" }, { "本周课表" }, { "关于" } };
	private String[] group_title_arry = new String[] { "个人信息", "考试", "成绩查询",
			"CET4/6", "课表", "关于" };
	private ExpandableListView elv;
	private LoginService service;
	private Button bt_login;
	private final int RESIT = 0;
	private final int EXAM = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.jw_fragment, null);
		init(v);
		setListener();

		setAnim(getActivity(), R.anim.list_item_anim, elv);

		// 设置默认图标为不显示状态
		elv.setGroupIndicator(null);
		// 为列表绑定数据源
		final MianBaseExpandableListAdapter adapter = new MianBaseExpandableListAdapter(
				getActivity(), child_text_array, group_title_arry);
		elv.setAdapter(adapter);
		// 设置一级item点击的监听器
		elv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				adapter.group_checked[groupPosition] = adapter.group_checked[groupPosition] + 1;
				// 刷新界面
				((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
				return false;
			}
		});

		// 设置二级item点击的监听器
		elv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// System.out.println("点击了:"
				// + child_text_array[groupPosition][childPosition]);
				Intent intent = null;
				switch (child_text_array[groupPosition][childPosition]) {
				case "补考信息":
					intent = new Intent("resit");
					intent.putExtra("exam_type", RESIT);
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "修改登录密码":
					intent = new Intent("pwdmodify");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "期末考试":
					intent = new Intent("resit");
					intent.putExtra("exam_type", EXAM);
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "等级成绩":
					intent = new Intent("level");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "课程成绩":
					intent = new Intent("score");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "CET-4/6查询":
					intent = new Intent("cet46score");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "CET-4/6报名":
					intent = new Intent("cet46apply");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "本周课表":
					intent = new Intent("course");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				case "关于":
					intent = new Intent("appinfo");
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
					break;
				default:
					break;
				}

				// 刷新界面
				((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
				return true;
			}
		});

		return v;
	}

	private void init(View v) {
		elv = (ExpandableListView) v.findViewById(R.id.elv_jw);
		bt_login = (Button) v.findViewById(R.id.bt_login_jw);
		service = new LoginService(getActivity());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (8 == requestCode && 9 == resultCode
				&& data.getBooleanExtra("isLogin", false)) {
			String name = data.getStringExtra("name");
			bt_login.setText(name + "/注销");
		}
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
					Intent intent = new Intent("login");
					JwFragment.this.startActivityForResult(intent, 8);
					getActivity().overridePendingTransition(
							R.anim.activity_right_in, R.anim.activity_left_out);
				}
			}
		});

	}

	private void setAnim(Context context, int resource,
			ExpandableListView expandableListView) {
		Animation animation = (Animation) AnimationUtils.loadAnimation(context,
				resource);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setDelay(0.4f); // 设置动画间隔时间
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 设置列表的显示顺序
		expandableListView.setLayoutAnimation(lac);
	}

}
