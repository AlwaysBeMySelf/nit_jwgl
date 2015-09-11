package com.nit.bean;

public class Cet46 {
	public String name;
	public String school;
	public String item;
	public String id;
	public String time;
	public String score;
	public String listening;
	public String reading;
	public String writing;

	public Cet46(String name, String school, String item, String id,
			String time, String score, String listening, String reading,
			String writing) {
		super();
		this.name = name;
		this.school = school;
		this.item = item;
		this.id = id;
		this.time = time;
		this.score = score;
		this.listening = listening;
		this.reading = reading;
		this.writing = writing;
	}

	@Override
	public String toString() {
		return "Cet46 [name=" + name + ", school=" + school + ", item=" + item
				+ ", id=" + id + ", time=" + time + ", score=" + score
				+ ", listening=" + listening + ", reading=" + reading
				+ ", writing=" + writing + "]";
	}

}
