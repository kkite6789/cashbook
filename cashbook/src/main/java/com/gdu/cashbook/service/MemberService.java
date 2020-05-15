package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.DeletedMember;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Service
@Transactional// 트랜잭션
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;


	
	public int getMemberPw(Member member) {
		UUID uuid=UUID.randomUUID();
		String memberPw = uuid.toString().substring(0, 8);
		member.setMemberPw(memberPw);
		int row = memberMapper.updateMemberPw(member);
		if(row == 1) {
			
			System.out.println(memberPw+"<--update memberPw");
			//메일로 update성공한 랜덤pw 전송
			//메일객체 new JavaMailSender();
		}
		return row;
	}
	
	
	public String getMemberIdByMember(Member member) {
		return memberMapper.selectMemberIdByMember(member);
	}
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck); // 결과물은 memberId나 null이 리턴된다
	}
	
	public LoginMember login(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	public List<Member> getMemberList(int currentPage,int rowPerPage){
		Map<String,Object> map = new HashMap<>();
		int beginRow = (currentPage-1)*rowPerPage;
		map.put("beginRow", beginRow);
		map.put("rowPerPage", rowPerPage);
		return memberMapper.selectMemberList(map);
	}
	public int AddMember(Member member) {
		return memberMapper.insertMember(member);
	}
	public int removeMember(String memberId) {
		return memberMapper.deleteMember(memberId);
	}
	public int addDeletedMember(String deletedMemberId) {
		return memberMapper.insertDeletedMember(deletedMemberId);
	}
	public int replaceMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
}
