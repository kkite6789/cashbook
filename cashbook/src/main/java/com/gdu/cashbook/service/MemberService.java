package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Service
@Transactional// 
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	
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
	
}
