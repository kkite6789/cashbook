package com.gdu.cashbook.controller;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
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
