package com.gdu.cashbook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")// 로그인 폼으로 보냄
	public String login() {
		return "login";
	}
	@PostMapping("/login")// 로그인 폼에서 받은 정보를 받고 액션을 취한다.
	public String login(Model model,LoginMember loginMember, HttpSession session) {//session = request.getSession()
		System.out.println(loginMember.toString());
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println(returnLoginMember+"<--returnLoginMember");
		String msgLogin="ID와 PW를 확인하세요.";
		if(returnLoginMember == null) { // null : 로그인 실패하면 다시 돌림
			model.addAttribute("msgLogin", msgLogin);
			return "login";
		} else { //로그인 성공할때 그대로 진행
			
			session.setAttribute("loginMember", returnLoginMember);
			return "redirect:/";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}	
	
	@GetMapping("/addMember")
	public String addMember() {
		
		return "addMember";
	}
	@PostMapping("/addMember") 
	public String addMember(Member member) {//commender
		memberService.AddMember(member);
		System.out.println(member.toString());
		return "redirect:/index";
	}
	@GetMapping("/getMember")
	public String MemberList(Model model,@RequestParam(value="currentPage", defaultValue="1")int currentPage){
		final int rowPerPage=5;
		List<Member>list = memberService.getMemberList(currentPage, rowPerPage);
		model.addAttribute("list",list);
		model.addAttribute("currentPage",currentPage);
		model.addAttribute("rowPerPage", rowPerPage);
		return "memberList";
	}
}
