package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.DeletedMember;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
@Mapper
public interface MemberMapper {
	public Member selectMemberOne(LoginMember loginMember);
	public String selectMemberId(String memberIdCheck);
	public LoginMember selectLoginMember(LoginMember loginMember);
	public List<Member> selectMemberList(Map<String,Object> map);
	public int insertMember(Member member);
	public int deleteMember(String memberId);
	public int updateMember(Member member);
	
	public String selectMemberIdByMember(Member member);
	
	public Member selectMemberByIdAndEmail(Member member);
	public int updateMemberPw(Member member);
	
	public int insertDeletedMember(String deletedMemberId);
}
