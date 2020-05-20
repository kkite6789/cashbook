package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;

@Service
@Transactional
public class CashService {
	
	@Autowired 
	private CashMapper cashMapper;
	
	public int selectCashKindSumMonth(Cash cash) {
		return cashMapper.selectCashKindSumMonth(cash);
	}
	
	public List<DayAndPrice> getCashAndPriceList(String memberId,int year, int month) {
		Map<String,Object>map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		map.put("month", month);
		return cashMapper.selectDayAndPriceList(map);
	}
	
	public int addCash(Cash cash) {
		return cashMapper.insertCash(cash);
	}
	
	public Map<String, Object> getCashListByDate(Cash cash) {
		System.out.println("getCashListByDate service 시작");
		System.out.println("getCashListByDate service -- selectCashListByDate");
		List<Cash> cashList = cashMapper.selectCashListByDate(cash);
		
		System.out.println("getCashListByDate service -- selectCashKindSum");
		int cashKindSum = cashMapper.selectCashKindSum(cash);
		
		Map<String, Object>map = new HashMap();
		map.put("cashList", cashList);
		map.put("cashKindSum", cashKindSum);
		System.out.println("getCashListByDate service 종료");
		return map;
	}
	
}
