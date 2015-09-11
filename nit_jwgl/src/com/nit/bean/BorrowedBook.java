package com.nit.bean;

public class BorrowedBook {
	public String code;//条码号
	public String title;//书名
	public String author;//作者
	public String borrowDate;//借阅时间
	public String returnDate;//应还时间
	public String isRenew;//是否续借
	public String check;//校验码
	public String image;//图片地址

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
