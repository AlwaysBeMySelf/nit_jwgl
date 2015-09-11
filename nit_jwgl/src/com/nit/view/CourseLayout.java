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
    private int width;//布局宽度
    private int height;//布局高度
    private int sectionHeight;//每节课高度
    private int sectionWidth;//每节课宽度
    private int sectionNumber = 12;//一天的节数
    private int dayNumber = 7;//一周的天数
    private int divideWidth = 2;//分隔线宽度,dp
    private int divideHeight = 2;//分隔线高度,dp
    public CourseLayout(Context context) {
        this(context, null);
    }
 
    public CourseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    public CourseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        width = getScreenWidth();//默认宽度全屏
        height = dip2px(700);//默认高度700dp
        divideWidth = dip2px(2);//默认分隔线宽度2dp
        divideHeight = dip2px(2);//默认分隔线高度2dp
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        sectionHeight = (getMeasuredHeight() - divideWidth * sectionNumber)/ sectionNumber;//计算每节课高度
        sectionWidth = (getMeasuredWidth() - divideWidth * dayNumber)/ dayNumber;//计算每节课宽度
 
        int count = getChildCount();//获得子控件个数
        for (int i = 0; i < count; i++) {
            CourseView child = (CourseView) getChildAt(i);
            courses.add(child);//增加到list中
             
 
            int week = child.getWeek();//获得周几
            int startSection = child.getStartSection();//开始节数
            int endSection = child.getEndSection();//结束节数
 
            int left = sectionWidth * (week - 1) + (week) * divideWidth;//计算左边的坐标
            int right = left + sectionWidth;//计算右边坐标
            int top = sectionHeight * (startSection - 1) + (startSection) * divideHeight;//计算顶部坐标
            int bottom = top + (endSection - startSection + 1) * sectionHeight+ (endSection - startSection) * divideHeight;//计算底部坐标
 
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

