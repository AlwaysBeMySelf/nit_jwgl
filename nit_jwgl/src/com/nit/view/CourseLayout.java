package com.nit.view;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
 
public class CourseLayout extends ViewGroup {
    private List<CourseView> courses = new ArrayList<CourseView>();
    private int width;//���ֿ��
    private int height;//���ָ߶�
    private int sectionHeight;//ÿ�ڿθ߶�
    private int sectionWidth;//ÿ�ڿο��
    private int sectionNumber = 12;//һ��Ľ���
    private int dayNumber = 7;//һ�ܵ�����
    private int divideWidth = 2;//�ָ��߿��,dp
    private int divideHeight = 2;//�ָ��߸߶�,dp
    public CourseLayout(Context context) {
        this(context, null);
    }
 
    public CourseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    public CourseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        width = getScreenWidth();//Ĭ�Ͽ��ȫ��
        height = dip2px(700);//Ĭ�ϸ߶�700dp
        divideWidth = dip2px(2);//Ĭ�Ϸָ��߿��2dp
        divideHeight = dip2px(2);//Ĭ�Ϸָ��߸߶�2dp
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        sectionHeight = (getMeasuredHeight() - divideWidth * sectionNumber)/ sectionNumber;//����ÿ�ڿθ߶�
        sectionWidth = (getMeasuredWidth() - divideWidth * dayNumber)/ dayNumber;//����ÿ�ڿο��
 
        int count = getChildCount();//����ӿؼ�����
        for (int i = 0; i < count; i++) {
            CourseView child = (CourseView) getChildAt(i);
            courses.add(child);//���ӵ�list��
             
 
            int week = child.getWeek();//����ܼ�
            int startSection = child.getStartSection();//��ʼ����
            int endSection = child.getEndSection();//��������
 
            int left = sectionWidth * (week - 1) + (week) * divideWidth;//������ߵ�����
            int right = left + sectionWidth;//�����ұ�����
            int top = sectionHeight * (startSection - 1) + (startSection) * divideHeight;//���㶥������
            int bottom = top + (endSection - startSection + 1) * sectionHeight+ (endSection - startSection) * divideHeight;//����ײ�����
 
            child.layout(left, top, right, bottom);
        }
    }
 
    public int dip2px(float dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
 
    public int getScreenWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
 
}

