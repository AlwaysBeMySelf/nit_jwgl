package com.nit.bean;

public class Resit {
	public String name;
	public String time;
	public String place;
	public String seat;
	
	public Resit() {
		super();
	}
	public Resit(String name, String time, String place, String seat) {
		super();
		this.name = name;
		this.time = time;
		this.place = place;
		this.seat = seat;
	}
	@Override
	public String toString() {
		return "Resit [name=" + name + ", time=" + time + ", place="
				+ place + ", seat=" + seat + "]";
	}
}
