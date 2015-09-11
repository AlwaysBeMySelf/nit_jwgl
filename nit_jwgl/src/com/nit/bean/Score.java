package com.nit.bean;

public class Score {
	public String name;//�γ�����
	public String year;//ѧ��
	public String term;//ѧ��
	public String property;//�γ�����
	public String credit;//ѧ��
	public String point;//����
	public String score;//�ɼ�
	public String reScore;//�����ɼ�
	public String rebuildScore;//���޳ɼ�
	public String highestScore;//��߳ɼ�ֵ
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
