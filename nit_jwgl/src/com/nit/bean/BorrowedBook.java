package com.nit.bean;

public class BorrowedBook {
	public String code;//�����
	public String title;//����
	public String author;//����
	public String borrowDate;//����ʱ��
	public String returnDate;//Ӧ��ʱ��
	public String isRenew;//�Ƿ�����
	public String check;//У����
	public String image;//ͼƬ��ַ

	public BorrowedBook(String code, String title, String author,
			String borrowDate, String returnDate) {
		super();
		this.code = code;
		this.title = title;
		this.author = author;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
	}

	public BorrowedBook(String code, String title, String borrowDate,
			String returnDate, String isRenew, String check, String image) {
		super();
		this.code = code;
		this.title = title;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.isRenew = isRenew;
		this.check = check;
		this.image = image;
	}

	@Override
	public String toString() {
		return "BorrowedBook [code=" + code + ", title=" + title
				+ ", borrowDate=" + borrowDate + ", returnDate=" + returnDate
				+ ", isRenew=" + isRenew + ", check=" + check + ", image="
				+ image + "]";
	}
	
}
