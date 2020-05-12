package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Member;
@Mapper
public interface MemberMapper {

	public List<Member> selectMemberList(Map<String,Object> map);
	public int insertMember(Member member);
}
