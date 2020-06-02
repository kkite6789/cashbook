package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.MonthAndPrice;
@Mapper
public interface CashMapper {
	
	public List<DayAndPrice> selectDayAndPriceList(Map<String,Object> map);
	//해당 사용자의 해당날짜 cash 목록
	public List<MonthAndPrice> selectMonthPriceList(Map<String,Object> map);
	public List<Cash> selectCashListByDate(Cash cash);
	public int selectCashKindSumMonth(Cash cash);
	public int selectCashKindSum(Cash cash);
	public int insertCash(Cash cash);
	public int deleteCash(Cash cash);
	public int updateCash(Cash cash);
	public int deleteCashName(String MemberId);
}
