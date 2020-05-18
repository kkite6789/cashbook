package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeletedMemberMapper {

	public int insertDeletedMember(String deletedMemberId);
}
