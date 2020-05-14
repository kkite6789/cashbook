package com.gdu.cashbook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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
	
	
	@PostMapping("/checkMemberId")
	public String checkMemberId(Model model,@RequestParam("memberIdCheck") String memberIdCheck, HttpSession session) {
		//로그인 중일 때
				if(session.getAttribute("loginMember") != null){
					return "redirect/";
				}
		String confirmMemberId = memberService.checkMemberId(memberIdCheck);
		System.out.println(confirmMemberId+" <- confirmMemberId");
		if(confirmMemberId != null) {
			//아이디를 사용할 수 있다
			model.addAttribute("msgCheck", "시용중인 아이디입니다.");
		} else if(memberIdCheck.length()<4){
			
			model.addAttribute("msgCheck", "4자 이상 입력하세요.");
		} else {
			// 없다
			model.addAttribute("memberIdCheck", memberIdCheck);
		}
		return "addMember";
	}
	
	@GetMapping("/forgotId")
	public String forgotId(HttpSession session) {
		if(session.getAttribute("loginMember") != null){//로그인 중일 때
			return "redirect/";
		}
		//로그인 안됐을 때 
		return  "forgotId";
	}
	
	@GetMapping("/login")// 로그인 폼으로 보냄
	public String login(HttpSession session) {//로그인 안됐을때만 나오게하려고 매개변수로 세션을 넣는다.
		//로그인 중일 때
		if(session.getAttribute("loginMember") != null){
			return "redirect/";
		}
		//로그인 안됐을 때 
		return "login";
	}
	@PostMapping("/login")// 로그인 폼에서 받은 정보를 받고 액션을 취한다.
	public String login(Model model,LoginMember loginMember, HttpSession session) {//session = request.getSession()
		if(session.getAttribute("loginMember") != null){
			return "redirect/";
		}
		
		System.out.println(loginMember.toString());
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println(returnLoginMember+"<--returnLoginMember");
		if(returnLoginMember == null) { // null : 로그인 실패하면 다시 돌림
			model.addAttribute("msgLogin", "ID와 PW를 확인하세요.");
			return "login";
		} else { //로그인 성공할때 그대로 진행
			
			session.setAttribute("loginMember", returnLoginMember);
			return "redirect:/home";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute("loginMember") != null){
			return "redirect/";
		}
		session.invalidate();
		return "redirect:/";
	}	
	
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		if(session.getAttribute("loginMember") != null){
			return "redirect/";
		}
		return "addMember";
	}
	@PostMapping("/addMember") 
	public String addMember(Member member,HttpSession session) {//commender
		if(session.getAttribute("loginMember") != null){
			return "redirect/";
		}
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
