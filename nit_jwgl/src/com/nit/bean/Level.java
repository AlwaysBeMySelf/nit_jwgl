package com.nit.bean;

public class Level {
	public String name;
	public String identify;
	public String time;
	public String sumScore;
	public String listenScore;
	public String readScore;
	public String writeScore;
	public String integrateScore;
	public Level(String name, String identify, String time, String sumScore,
			String listenScore, String readScore, String writeScore,
			String integrateScore) {
		super();
		this.name = name;
		this.identify = identify;
		this.time = time;
		this.sumScore = sumScore;
		this.listenScore = listenScore;
		this.readScore = readScore;
		this.writeScore = writeScore;
		this.integrateScore = integrateScore;
	}
	@Override
	public String toString() {
		return "Level [name=" + name + ", identify=" + identify + ", time="
				+ time + ", sumScore=" + sumScore + ", listenScore="
				+ listenScore + ", readScore=" + readScore + ", writeScore="
				+ writeScore + ", integrateScore=" + integrateScore + "]";
	}
}
