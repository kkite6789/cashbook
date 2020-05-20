package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;
@Mapper
public interface CashMapper {
	
	public List<DayAndPrice> selectDayAndPriceList(Map<String,Object> map);
	//해당 사용자의 해당날짜 cash 목록
	public List<Cash> selectCashListByDate(Cash cash);
	public int selectCashKindSumMonth(Cash cash);
	public int selectCashKindSum(Cash cash);
	public int insertCash(Cash cash);
	
}
