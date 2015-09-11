package com.nit.bean;

public class RollCallMenber {
	public String id;
	public String name;
	public RollCallMenber(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "RollCallMenber [id=" + id + ", name=" + name + "]";
	}

}
