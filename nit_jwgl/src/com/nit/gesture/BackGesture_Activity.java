package com.nit.gesture;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BackGesture_Activity extends Activity {
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 1 ��ʼ�� ����ʶ����
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {// e1: ��һ�ΰ��µ�λ��
																// e2 �����뿪��Ļ
																// ʱ��λ��
																// velocityX ��x
																// ����ٶ�
																// velocityY��
																// ��Y�᷽����ٶ�
						// �ж���ֱ�����ƶ��Ĵ�С
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
							// Toast.makeText(getApplicationContext(), "�������Ϸ�",
							// 0).show();
							return true;
						}
						if (Math.abs(velocityX) < 150) {
							// Toast.makeText(getApplicationContext(), "�ƶ���̫��",
							// 0).show();
							return true;
						}

//						if ((e1.getRawX() - e2.getRawX()) > 200) {// ��ʾ
//																	// ���һ�����ʾ��һҳ
//							// ��ʾ��һҳ
//							next(null);
//							return true;
//						}

						if ((e2.getRawX() - e1.getRawX()) > 200) { // ���󻬶� ��ʾ
																	// ��һҳ
							// ��ʾ��һҳ
							pre(null);
							return true;// ���ѵ���ǰ�¼� ���õ�ǰ�¼��������´���
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});

	}

//	/**
//	 * ��һ��ҳ��
//	 * 
//	 * @param view
//	 */
//	public abstract void next(View view);

	/**
	 * ��һ��ҳ��
	 * 
	 * @param view
	 */
	public abstract void pre(View view);

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	
}
