package com.nit.bean;

public class Score {
	public String name;//课程名称
	public String year;//学年
	public String term;//学期
	public String property;//课程性质
	public String credit;//学分
	public String point;//绩点
	public String score;//成绩
	public String reScore;//补考成绩
	public String rebuildScore;//重修成绩
	public String highestScore;//最高成绩值
	public Score(String name, String property, String credit,
			String highestScore) {
		super();
		this.name = name;
		this.property = property;
		this.credit = credit;
		this.highestScore = highestScore;
	}
	public Score(String name, String year, String term, String property,
			String credit, String point, String score, String reScore,
			String rebuildScore) {
		super();
		this.name = name;
		this.year = year;
		this.term = term;
		this.property = property;
		this.credit = credit;
		this.point = point;
		this.score = score;
		this.reScore = reScore;
		this.rebuildScore = rebuildScore;
	}
	@Override
	public String toString() {
		return "Score [name=" + name + ", year=" + year + ", term=" + term
				+ ", property=" + property + ", credit=" + credit + ", point="
				+ point + ", score=" + score + ", reScore=" + reScore
				+ ", rebuildScore=" + rebuildScore + ", highestScore="
				+ highestScore + "]";
	}

}
