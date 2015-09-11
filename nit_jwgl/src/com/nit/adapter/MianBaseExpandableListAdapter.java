package com.nit.adapter;

import com.nit.nit_jwgl.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MianBaseExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private String[][] child_text_array;
	private String[] group_title_arry;
	// һ����ǩ�ϵ�״̬ͼƬ����Դ
	int[] group_state_array = new int[] { R.drawable.group_down,
			R.drawable.group_up };
	// ��������������洢һ��item�ĵ�������ģ����ݵ����������һ����ǩ��ѡ�С�Ϊѡ��״̬
	public int[] group_checked = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public MianBaseExpandableListAdapter(Context context,
			String[][] child_text_array, String[] group_title_arry) {
		super();
		this.context = context;
		this.child_text_array = child_text_array;
		this.group_title_arry = group_title_arry;
	}

	// ��дExpandableListAdapter�еĸ�������
	/**
	 * ��ȡһ����ǩ����
	 */
	@Override
	public int getGroupCount() {
		return group_title_arry.length;
	}

	/**
	 * ��ȡһ����ǩ����
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return group_title_arry[groupPosition];
	}

	/**
	 * ��ȡһ����ǩ��ID
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * ��ȡһ����ǩ�¶�����ǩ������
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return child_text_array[groupPosition].length;
	}

	/**
	 * ��ȡһ����ǩ�¶�����ǩ������
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child_text_array[groupPosition][childPosition];
	}

	/**
	 * ��ȡ������ǩ��ID
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * ָ��λ����Ӧ������ͼ
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * ��һ����ǩ��������
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// Ϊ��ͼ����ָ������
		convertView = (LinearLayout) LinearLayout.inflate(context,
				R.layout.group_layout, null);

		RelativeLayout myLayout = (RelativeLayout) convertView
				.findViewById(R.id.group_layout);

		/**
		 * ������ͼ��Ҫ��ʾ�Ŀؼ�
		 */
		// �½�һ��TextView����������ʾһ����ǩ�ϵı�����Ϣ
		TextView group_title = (TextView) convertView
				.findViewById(R.id.group_title);
		// �½�һ��TextView����������ʾһ����ǩ�ϵĴ�����������Ϣ
		ImageView group_state = (ImageView) convertView
				.findViewById(R.id.group_state);
		/**
		 * ������Ӧ�ؼ�������
		 */
		// ���ñ����ϵ��ı���Ϣ
		group_title.setText(group_title_arry[groupPosition]);
		// �������������ϵ��ı���Ϣ

		if (group_checked[groupPosition] % 2 == 1) {
			// ����Ĭ�ϵ�ͼƬ��ѡ��״̬
			group_state.setBackgroundResource(group_state_array[1]);
			myLayout.setBackgroundResource(R.drawable.text_item_top_bg);
		} else {
			for (int test : group_checked) {
				if (test == 0 || test % 2 == 0) {
					myLayout.setBackgroundResource(R.drawable.text_item_bg);
					// ����Ĭ�ϵ�ͼƬ��δѡ��״̬
					group_state.setBackgroundResource(group_state_array[0]);
				}
			}
		}
		// ����һ�����ֶ���
		return convertView;
	}

	/**
	 * ��һ����ǩ�µĶ�����ǩ��������
	 */

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// Ϊ��ͼ����ָ������
		convertView = (RelativeLayout) RelativeLayout.inflate(context,
				R.layout.child_layout, null);
		/**
		 * ������ͼ��Ҫ��ʾ�Ŀؼ�
		 */
		// �½�һ��TextView����������ʾ��������
		TextView child_text = (TextView) convertView
				.findViewById(R.id.child_text);
		/**
		 * ������Ӧ�ؼ�������
		 */
		// ����Ҫ��ʾ���ı���Ϣ
		child_text.setText(child_text_array[groupPosition][childPosition]);
		// �ж�item��λ���Ƿ���ͬ������ͬ�����ʾΪѡ��״̬�������䱳����ɫ���粻��ͬ�������ñ���ɫΪ��ɫ
		// ����һ�����ֶ���
		if (childPosition == child_text_array[groupPosition].length - 1) {
			convertView.setBackgroundResource(R.drawable.text_item_bottom_bg);
		}
		return convertView;
	}

	/**
	 * ��ѡ���ӽڵ��ʱ�򣬵��ø÷���
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
