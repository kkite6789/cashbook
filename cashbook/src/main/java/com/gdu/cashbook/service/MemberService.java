package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.DeletedMember;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Service
@Transactional// 트랜잭션
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private JavaMailSender javaMailSender;//@Component

	
	public int getMemberPw(Member member) {
		UUID uuid=UUID.randomUUID();
		String memberPw = uuid.toString().substring(0, 8);
		member.setMemberPw(memberPw);
		int row = memberMapper.updateMemberPw(member);
		
		if(row == 1) {
			
			System.out.println(memberPw+"<--update memberPw");
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			System.out.println(member.getMemberEmail()+"<--recipient memberEmail");
			simpleMailMessage.setTo(member.getMemberEmail());
			simpleMailMessage.setFrom("kkite6789@gmail.com");
			simpleMailMessage.setSubject("cashbook 비밀번호 찾기 메일");
			simpleMailMessage.setText("변경된 비밀번호 :"+ memberPw +"입니다.");
			
			javaMailSender.send(simpleMailMessage);
			//메일로 update성공한 랜덤pw 전송
			//메일객체 new JavaMailSender();
		}
		return row;
	}
	
	
	public String getMemberIdByMember(Member member) {
		System.out.println("id찾기 서비스 시작");
		System.out.println(member+"<--member");
		return memberMapper.selectMemberIdByMember(member);
	}
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck); // 결과물은 memberId나 null이 리턴된다
	}
	
	public LoginMember login(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	public List<Member> getMemberList(int currentPage,int rowPerPage){
		Map<String,Object> map = new HashMap<>();
		int beginRow = (currentPage-1)*rowPerPage;
		map.put("beginRow", beginRow);
		map.put("rowPerPage", rowPerPage);
		return memberMapper.selectMemberList(map);
	}
	public int AddMember(Member member) {
		return memberMapper.insertMember(member);
	}
	public int removeMember(String memberId) {
		return memberMapper.deleteMember(memberId);
	}
	public int addDeletedMember(String deletedMemberId) {
		return memberMapper.insertDeletedMember(deletedMemberId);
	}
	public int replaceMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
}
