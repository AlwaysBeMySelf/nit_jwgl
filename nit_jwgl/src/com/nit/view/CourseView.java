package com.nit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CourseView extends Button {

	private int startSection;
	private int endSection;
	private int weekDay;

	public CourseView(Context context) {
		this(context, null);
	}

	public CourseView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CourseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public int getStartSection() {
		return startSection;
	}

	public void setStartSection(int startSection) {
		this.startSection = startSection;
	}

	public int getEndSection() {
		return endSection;
	}

	public void setEndSection(int endSection) {
		this.endSection = endSection;
	}

	public int getWeek() {
		return weekDay;
	}

	public void setWeek(int week) {
		this.weekDay = week;
	}
}