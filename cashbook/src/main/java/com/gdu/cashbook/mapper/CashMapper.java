package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;
@Mapper
public interface CashMapper {
	
	//해당 사용자의 해당날짜 cash 목록
	public List<Cash> selectCashListByDate(Cash cash);
	public int selectCashKindSum(Cash cash);
	public int insertCash(Cash cash);
	
}
