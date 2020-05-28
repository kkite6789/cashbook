package com.gdu.cashbook.vo;

public class DeletedMember {
	private int memberidNo;
	private int memberId;
	public int getMemberidNo() {
		return memberidNo;
	}
	public void setMemberidNo(int memberidNo) {
		this.memberidNo = memberidNo;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	@Override
	public String toString() {
		return "DeletedMember [memberidNo=" + memberidNo + ", memberId=" + memberId + "]";
	}
	
}
