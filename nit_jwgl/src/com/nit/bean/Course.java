package com.nit.bean;

import java.util.Arrays;

public class Course {
	public String course;
	public int week;
	public int[] jies;
	public String during;
	public String teacher;
	public String room;
	
	
	
	public Course(String course, int week, int[] jies, String during,
			String teacher, String room) {
		super();
		this.course = course;
		this.week = week;
		this.jies = jies;
		this.during = during;
		this.teacher = teacher;
		this.room = room;
	}



	@Override
	public String toString() {
		return "course=" + course + "\nweek=" + week + "\njies="
				+ Arrays.toString(jies) + "\n during=" + during + "\n teacher="
				+ teacher + "\n room=" + room + "\n\n\n";
	}
	
}
