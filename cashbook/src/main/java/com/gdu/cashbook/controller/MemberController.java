package com.gdu.cashbook.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;

	
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		return "findMemberPw";
	}
	
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session, Model model, Member member) {
		int row = memberService.getMemberPw(member);
		String msg="아이디와 메일을 확인하세요.";
		if(row==1) {
			msg="비밀번호를 이메일로 전송하였습니다.";
		}
		model.addAttribute("msg", msg);
		return "memberPwView";
	}
	//아이디 찾기 get
	@GetMapping("/findMemberId")
	public String findMemberId(HttpSession session) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		return "findMemberId";
	}
	//아이디 찾기 post
	@PostMapping("/findMemberId")
	public String findMemberId(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		
		System.out.println(member.toString()+"<--member");
		String memberIdPart = memberService.getMemberIdByMember(member);
		System.out.println(memberIdPart+"<--memberIdPart");
		model.addAttribute("memberIdPart", memberIdPart);
		return "memberIdView";
	}
	@GetMapping("/replaceMember")
	public String replaceMember(Model model,HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		String memberId=member.getMemberId();
		String memberPic=member.getMemberPic();
		System.out.println("memberId -> "+ memberId);
		System.out.println("memberPic->" + memberPic);
		model.addAttribute("memberId", memberId);
		model.addAttribute("memberPic", memberPic);
		return "replaceMember";
	}
	@PostMapping("/replaceMember")
	public String replaceMember(MemberForm memberForm,HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		System.out.println(memberForm.toString() +"<-- memberForm");
		if(memberForm.getMemberPic()!=null) {
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif") ) {
				System.out.println("이미지 파일이 아닙니다.");
				return "redirect:/addMember?msg=이미지파일만 업로드 가능";
			}
		}
		memberService.replaceMember(memberForm);
		return "redirect:/index";
		

		
	}
	
	
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		
		
		/*
		Member member = memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		String memberId=member.getMemberId();
		String deletedMemberId = memberId;
		System.out.println(memberId+"<--memberId");
		System.out.println(deletedMemberId+"<--deletedMemberId");
		LoginMember loginMember= new LoginMember();
		loginMember = member;
		session.invalidate();
		memberService.removeMember(member);
		 */
		return "removeMember";
	}
	
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session, Model model, @RequestParam("memberPw") String memberPw) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember"); 
		loginMember.setMemberPw(memberPw); // removeMember.html에서 입력한 비밀번호로 변경
		System.out.println(loginMember.getMemberId());
		System.out.println(loginMember.getMemberPw());

		int result = memberService.removeMember(loginMember);
		System.out.println(result);
		if(result!=1) {
			System.out.println("비밀번호 틀림");
			//model.addAttribute("msg", "비밀번호를 다시 확인하세요."); 
			return "removeMember";
		} else {
			System.out.println("비밀번호 일치");
		}
		
		memberService.removeMember(loginMember); // 삭제 메서드 호출
		session.invalidate();
		return "redirect:/home";
	}
	
	@GetMapping("/memberInfo")
	public String memberInfo(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		System.out.println(member+"<--member");
		model.addAttribute("member",member);
		return "memberInfo";
	}
	
	@PostMapping("/checkMemberId")
	public String checkMemberId(Model model,@RequestParam("memberIdCheck") String memberIdCheck, HttpSession session) {
		//로그인 중일 때
				if(session.getAttribute("loginMember") != null){
					return "redirect:/";
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
			return "redirect:/";
		}
		//로그인 안됐을 때 
		return  "forgotId";
	}
	
	@GetMapping("/login")// 로그인 폼으로 보냄
	public String login(HttpSession session) {//로그인 안됐을때만 나오게하려고 매개변수로 세션을 넣는다.
		//로그인 중일 때
		if(session.getAttribute("loginMember") != null){
			return "redirect:/";
		}
		//로그인 안됐을 때 
		return "login";
	}
	@PostMapping("/login")// 로그인 폼에서 받은 정보를 받고 액션을 취한다.
	public String login(Model model,LoginMember loginMember, HttpSession session) {//session = request.getSession()
		if(session.getAttribute("loginMember") != null){
			return "redirect:/";
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
		if(session.getAttribute("loginMember") == null){
			return "redirect:/";
		}
		session.invalidate();
		return "redirect:/";
	}	
	
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		if(session.getAttribute("loginMember") != null){
			return "redirect:/";
		}
		return "addMember";
	}
	@PostMapping("/addMember") 
	public String addMember(MemberForm memberForm,HttpSession session) {//commender
		if(session.getAttribute("loginMember") != null){
			return "redirect:/";
		}
	//	memberService.AddMember(member);
		System.out.println(memberForm.toString() +"<-- memberForm");
		if(memberForm.getMemberPic()!=null) {
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif") ) {
				System.out.println("이미지 파일이 아닙니다.");
				return "redirect:/addMember?msg=이미지파일만 업로드 가능";
			}
		}
		memberService.addMember(memberForm);
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
