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
import com.gdu.cashbook.vo.MonthAndPrice;

@Controller
public class CashController {
	
	@Autowired
	public CashService cashService;
	
	@GetMapping("/getCashListByMonthToCompare")
	public String getCashListByMonthToCompare(HttpSession session,Model model,@RequestParam(value="day", required=false)@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		Calendar cDay = Calendar.getInstance();
		
		if(day == null) {
	    	day=LocalDate.now();
	    	
	      } else {
	    	  cDay.set(day.getYear(), day.getMonthValue()-1, day.getDayOfMonth()); // 오늘 날짜에서 day값과 동일한 값으로
	    	//day-->cDay 
	      }
		 System.out.println(cDay+"<--cDay");
		
		String memberId=((LoginMember)session.getAttribute("loginMember")).getMemberId();
		int year=cDay.get(Calendar.YEAR);

	   
		System.out.println(memberId+"<--memberId");
		System.out.println(year+"<--year");

		int totalPrice=0;

		List<MonthAndPrice> monthAndPriceList = cashService.getCashAndPriceListByMonth(memberId, year);
	      for(MonthAndPrice dnp : monthAndPriceList) {
	    	  System.out.println(dnp);
	    	  System.out.println(dnp.getPrice()+"<--dnp.getPrice");
	    	  System.out.println(dnp.getMonth()+"<--dnp.getMonth");
	    	  totalPrice += dnp.getPrice(); // 달의 총 입금/지출 금액
	      }
	      
	      System.out.println(monthAndPriceList.toString());
	      System.out.println(totalPrice+"<--totalPrice");
		
		
		
		//List<DayAndPrice> dayAndPriceList = cashService.getCashAndPriceList(memberId, year, month); 일일
		
		
		model.addAttribute("year", year);
		//model.addAttribute("month", month);
		model.addAttribute("monthAndPriceList", monthAndPriceList);
		model.addAttribute("totalPrice",totalPrice);
		return "getCashListByMonthToCompare";
	}
	
	
	
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
	      int totalPrice=0;
	      List<DayAndPrice> dayAndPriceList = cashService.getCashAndPriceList(memberId, year, month);
	      for(DayAndPrice dnp : dayAndPriceList) {
	    	  System.out.println(dnp);
	    	  System.out.println(dnp.getPrice()+"<--dnp.getPrice");
	    	  totalPrice+=dnp.getPrice(); // 달의 총 입금/지출 금액
	      }
	      
	      System.out.println(totalPrice+"<--totalPrice");
	      //int cashSum=cashService.selectCashKindSumMonth(cash);
	      month= cDay.get(Calendar.MONTH)+1;
	      int lastDay= cDay.getActualMaximum(Calendar.DATE);
	      //System.out.println(cashSum+"<--cashSum");
	      
	     // model.addAttribute("cashSum", cashSum);
	      model.addAttribute("totalPrice", totalPrice);
	      model.addAttribute("dayAndPriceList", dayAndPriceList);
	      model.addAttribute("day", day);
	      model.addAttribute("year", year);      //년(year)을 넘겨줌
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
	
	//가계부 수정 Get
	@GetMapping("/replaceCash")
	public String replaceCash(HttpSession session,Cash cash,Model model) {
		
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("replace Get시작");
		System.out.println(cash.toString());
		System.out.println(cash.getCashNo()+"<--cashNo");
		System.out.println(cash.getCashDate()+"<--cashDate");
		model.addAttribute("cashNo", cash.getCashNo());
		model.addAttribute("cashDate",cash.getCashDate());
		
		return "replaceCash";
	}
	
	//가계부 수정 post
	@PostMapping("/replaceCash")
	public String replaceCash(HttpSession session,Cash cash) {
		System.out.println("replace post시작1");
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("replace post시작2");
		System.out.println(cash.toString());
		cashService.replaceCash(cash);
		System.out.println("수정성공");
		return "redirect:/getCashListByDate?day=" + cash.getCashDate();
		
	}
	
	//가계부 삭제 Get
	@GetMapping("/removeCash")
	public String removeCash(HttpSession session,Cash cash,Model model) {
		
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println(cash.toString());
		System.out.println(cash.getCashNo()+"<--cashNo");
		System.out.println(cash.getCashDate()+"<--cashDate");
		//model.addAttribute("day", cash.getCashDate());
		cashService.removeCash(cash);
		return "redirect:/getCashListByDate?day=" + cash.getCashDate();
	}
	
	//가계부 추가 Get
	@GetMapping("/addCash")
	public String addCash(HttpSession session,Model model, Cash cash) {
		System.out.println("add 컨트롤러 시작");
		
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println(cash.getCashDate()+"<--cashDate");
		model.addAttribute("cashDate", cash.getCashDate());
		return "addCash";
	}
	//날짜값 보내서 해당 날짜로  들어가게
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
		//model.addAttribute("day", cash.getCashDate());
		return "redirect:/getCashListByDate?day=" + cash.getCashDate();
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
