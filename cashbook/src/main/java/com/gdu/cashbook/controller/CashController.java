package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.ZoneId;

import javax.xml.crypto.Data;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	
	@Autowired
	public CashService cashService;
	
	@GetMapping("/getCashListByMonth")
	public String getCashListByMonth(HttpSession session,Model model,@RequestParam(value="day", required=false)@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println(day+"<--day");
		Calendar cDay = Calendar.getInstance();      //오늘날짜
	      // System.out.println(cDay.get(Calendar.MONTH)+1);
	      if(day == null) {
	    	day=LocalDate.now();
	    	
	      } else {
	    	  cDay.set(day.getYear(), day.getMonthValue()-1, day.getDayOfMonth()); // 오늘 날짜에서 day값과 동일한 값으로
	    	//day-->cDay 
	      }
	      System.out.println(cDay+"<--cDay");
	      /*
	       * 0. 오늘 LocalDate 타입
	       * 1. 오늘 calendar type
	       * 2. 이번달의 마지막 일
	       * 3. 이번달 1일의 요일
	       */
	       
	      System.out.println(day+"<--day");
	      String cashDate=(day.toString());
	      //일별 수입, 지출 총액
	      String memberId=((LoginMember)session.getAttribute("loginMember")).getMemberId();
	      int year=cDay.get(Calendar.YEAR);
	      int month=cDay.get(Calendar.MONTH)+1;
	      System.out.println(year+"<--year");
	      System.out.println(month+"<--month");
	      
	      //Cash cash = new Cash();
	      //cash.setCashDate(cashDate);
	      //cash.setMemberId(memberId);
	      
	      List<DayAndPrice> dayAndPriceList = cashService.getCashAndPriceList(memberId, year, month);
	      for(DayAndPrice dnp : dayAndPriceList) {
	    	  System.out.println(dnp);
	      }
	      //int cashSum=cashService.selectCashKindSumMonth(cash);
	      month= cDay.get(Calendar.MONTH)+1;
	      int lastDay= cDay.getActualMaximum(Calendar.DATE);
	      //System.out.println(cashSum+"<--cashSum");
	      
	     // model.addAttribute("cashSum", cashSum);
	      model.addAttribute("dayAndPriceList", dayAndPriceList);
	      model.addAttribute("day", day);
	      model.addAttribute("month", month);      //월을 넘겨줌
	      model.addAttribute("lastDay", lastDay);   //마지막 일 --> date의 제일 큰 값을 넘겨줌
	      
	      
	      
	      Calendar firstDay = cDay;   //오늘 날짜를 하나더구해서
	      firstDay.set(Calendar.DATE, 1);            // 일만 1로 변경
	      //firstDay.get(Calendar.DAY_OF_WEEK);      // 1 -> 일요일, 2 -> 월요일,.....7 -> 토요일
	      System.out.println(month+"<--month");
	      System.out.println(firstDay.get(Calendar.DAY_OF_WEEK)+"<--firstDay.get(Calendar.DAY_OF_WEEK)");
	      System.out.println(lastDay+"<--lastDay");
	      model.addAttribute("firstDayOfWeek", firstDay.get(Calendar.DAY_OF_WEEK));

	      System.out.println(firstDay.get(Calendar.YEAR)+","+(firstDay.get(Calendar.MONTH)+1)+","+firstDay.get(Calendar.DATE));
		
		return "getCashListByMonth";
	}
	
	
	@GetMapping("/addCash")
	public String addCash(HttpSession session) {
		System.out.println("add 컨트롤러 시작");
		
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		return "addCash";
	}
	@PostMapping("/addCash")
	public String addCash(HttpSession session,Cash cash) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println(memberId+"<--memberId");
		cash.setMemberId(memberId);
		System.out.println(cash.toString());
		cashService.addCash(cash);
		
		return "redirect:/getCashListByDate";
	}
	
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session,Model model,@RequestParam(value="day", required=false)@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		
		System.out.println(day+"<-- day");

		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		if(day==null) {
			day = LocalDate.now();
		}
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		//Calendar를 이용한 오늘 날짜
		//오늘 날짜를 구해서 원하는 문자열 형태로 변경
		//Date day2 = new Date();
		//Calendar today = Calendar.getInstance(); //"yyyy-MM-dd" 형식
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat sdf2=new SimpleDateFormat("E");
		//SimpleDateFormat sdf3=new SimpleDateFormat("yyyy-MM-dd-E");
		
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //LocalDate date = LocalDate.parse(string, formatter);
		
		
		
		System.out.println(day+"<--day");
		
		//String strToday=sdf.format(day);
		//System.out.println(strToday+"<--strToday");
		
		/*
		String strToday2=sdf2.format(day);
		String day1=strToday+" "+strToday2+"요일";
		
		System.out.println(strToday2+"<--strToday2");
		System.out.println(day1+"<--day");
		*/
		
		Cash cash = new Cash(); // +id, +date("yyyy-MM-dd")
		cash.setMemberId(loginMemberId);
		cash.setCashDate(day.toString());
		
		System.out.println(loginMemberId+"<-- 현재 login 아이디");
		//System.out.println(strToday+"<--strToday ");
	
		Map<String, Object> map = cashService.getCashListByDate(cash);
		System.out.println(cash.toString()+"<-cash.toString()");
		
		model.addAttribute("cashKindSum", map.get("cashKindSum"));
		model.addAttribute("cashList", map.get("cashList"));
		model.addAttribute("day", day);
		//model.addAttribute("day", strToday);
		//model.addAttribute("day",day1);
		/*
		if(cashlist.size()==0) {
			System.out.println("cashlist가 비었습니다.");
		} else {
		System.out.println(cashlist.size()+" <--cashlist 갯수");
		}
		
		for(Cash c:cashlist) {
			System.out.println(c);
		}
		*/
		return "getCashListByDate";
		
	}
	
	
	
}
