package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	
	@Autowired
	public CashService cashService;
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session,Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		//Calendar를 이용한 오늘 날짜
		//오늘 날짜를 구해서 원하는 문자열 형태로 변경
		Date today = new Date();
		//Calendar today = Calendar.getInstance(); //"yyyy-MM-dd" 형식
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2=new SimpleDateFormat("E");
		SimpleDateFormat sdf3=new SimpleDateFormat("yyyy-MM-dd-E");
		System.out.println(today+"<--today");
		String strToday=sdf.format(today);
		String strToday2=sdf3.format(today);
		String day1=strToday2+"요일";
		System.out.println(strToday+"<--strToday");
		System.out.println(strToday2+"<--strToday2");
		System.out.println(day1+"<--day");
		
		Cash cash = new Cash(); // +id, +date("yyyy-MM-dd")
		cash.setMemberId(loginMemberId);
		cash.setCashDate(strToday);
		
		System.out.println(loginMemberId+"<-- 현재 login 아이디");
		System.out.println(strToday+"<--strToday ");
		
		List<Cash> cashlist = cashService.getCashListByDate(cash);
		System.out.println(cash.toString()+"<-cash.toString()");
		model.addAttribute("cashList", cashlist);	
		model.addAttribute("day",day1);
		
		if(cashlist.size()==0) {
			System.out.println("cashlist가 비었습니다.");
		} else {
		System.out.println(cashlist.size()+" <--cashlist 갯수");
		}
		
		for(Cash c:cashlist) {
			System.out.println(c);
		}
		
		return "getCashListByDate";
	}
	
	
	
}
