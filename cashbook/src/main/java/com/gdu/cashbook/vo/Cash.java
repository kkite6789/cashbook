package com.gdu.cashbook.vo;

import java.time.LocalDate;

public class Cash {
	private int cashNo;
	private String memberId;
	private String cashDate;
	private String cashKind;
	private String categoryName;
	private String cashPrice;
	private String cashPlace;
	private String cashMemo;
	
	
	
	public String getCashDate() {
		return cashDate;
	}
	public void setCashDate(String cashDate) {
		this.cashDate = cashDate;
	}
	public int getCashNo() {
		return cashNo;
	}
	public void setCashNo(int cashNo) {
		this.cashNo = cashNo;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCashKind() {
		return cashKind;
	}
	public void setCashKind(String cashKind) {
		this.cashKind = cashKind;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCashPrice() {
		return cashPrice;
	}
	public void setCashPrice(String cashPrice) {
		this.cashPrice = cashPrice;
	}
	public String getCashPlace() {
		return cashPlace;
	}
	public void setCashPlace(String cashPlace) {
		this.cashPlace = cashPlace;
	}
	public String getCashMemo() {
		return cashMemo;
	}
	public void setCashMemo(String cashMemo) {
		this.cashMemo = cashMemo;
	}
	@Override
	public String toString() {
		return "Cash [cashNo=" + cashNo + ", memberId=" + memberId + ", cashDate=" + cashDate + ", cashKind=" + cashKind
				+ ", categoryName=" + categoryName + ", cashPrice=" + cashPrice + ", cashPlace=" + cashPlace
				+ ", cashMemo=" + cashMemo + "]";
	}
	
	
}
