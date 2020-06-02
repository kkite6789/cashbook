package com.gdu.cashbook.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.mapper.DeletedMemberMapper;
import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.Admin;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

@Service
@Transactional// 트랜잭션
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private DeletedMemberMapper deletedMemberMapper;
	@Autowired
	private CashMapper cashMapper;
	@Autowired
	private JavaMailSender javaMailSender;//@Component
	@Value("C:\\kkt\\sts_work\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload\\")
	private String path;

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
	public int getConfirmMemberCount(Member member) {
		return memberMapper.selectConfirmMemberCount(member);
	}
	
	public String getMemberIdByMember(Member member) {
		System.out.println("id찾기 서비스 시작");
		System.out.println(member+"<--member");
		return memberMapper.selectMemberIdByMember(member);
	}
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	public Admin getAdminOne(LoginMember loginMember) {
		return memberMapper.selectAdminOne(loginMember);
	}
	
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck); // 결과물은 memberId나 null이 리턴된다
	}
	
	public LoginMember loginAdmin(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);	
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
	public int addMember(MemberForm memberForm) {
		
		MultipartFile mf = memberForm.getMemberPic();
		//확장자 필요
		String originName = mf.getOriginalFilename();
		/*
		if((mf.getContentType().equals("image/png"))||(mf.getContentType().equals("image/jpeg"))){
			//업로드
		} else {
			//실패
		}
		*/
		System.out.println(originName);
		//사진을 안넣을때는 default.png가 출려되게
		//그 외에는 밑에 문 실행
		int lastDot=originName.lastIndexOf("."); //  xxx.PNG
		String extension = originName.substring(lastDot);
		//새로운 이름을 생성
		String memberPic = memberForm.getMemberId()+extension;
		// 1.db에 저장
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberPic(memberPic);
		System.out.println(member+"<--memberService.addMember:member");
		//memberForm->member
		System.out.println("addMember");
		//return memberMapper.insertMember(member);
		int row = memberMapper.insertMember(member);
		// 2.파일저장
		//String path ="C:\\kkt\\sts_work\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload";
		// /는 리눅스  \는 윈도우
		File file = new File(path+memberPic);
		try {
			mf.transferTo(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
			/*자바의 예외에는 2가지가 있다
			 * 1. 예외처리를 해야만 문법적으로 이상없는 예외
			 * 2. 예외처리를 코드에서 구현하지 않아도 아무문제없는 예외 ex)RuntimeException()
			 */
		}
		return row;
		// 2.service보내기
		//3.
		
		
	}
	public int selectTotalRow() {
		return memberMapper.selectTotalRow();
	}
	
	public int removeMemberId(String memberId) {
		
		//1. 멤버 이미지 파일 삭제
				// 1_1 파일이름 select member_pic from member
				String memberPic = memberMapper.selectMemberPic(memberId);
				System.out.println(memberPic+"<---memberPic");
				//1_2. 파일 삭제
				//String path2 ="C:\\kkt\\sts_work\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload\\";
				File file = new File(path+memberPic);//사진은 memberId와 이름이 동일하다.
				System.out.println(file+"<---file");
				if(file.exists()) {
					file.delete();
				}
				int result =0;
				//2. 트랜잭션 처리
				int deleteCashResult=cashMapper.deleteCashName(memberId);
				if(deleteCashResult == 1) {
					System.out.println("캐시내역 삭제 성공");
				} else {
					System.out.println("캐시내역 삭제 실패");
				}
				Member member = new Member();
				result = memberMapper.deleteMemberId(memberId);
				if(result==1) {
				member.setMemberId(memberId);
				deletedMemberMapper.insertDeletedMember(memberId);
					System.out.println("멤버 삭제 성공");
				}
				else {
					System.out.println("멤버 삭제 실패");
				}
				
				return result;
		
	}
	
	public int removeMember(LoginMember loginMember) {
		
		//1. 멤버 이미지 파일 삭제
		// 1_1 파일이름 select member_pic from member
		String memberPic = memberMapper.selectMemberPic(loginMember.getMemberId());
		System.out.println(memberPic+"<---memberPic");
		//1_2. 파일 삭제
		//String path2 ="C:\\kkt\\sts_work\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload\\";
		File file = new File(path+memberPic);//사진은 memberId와 이름이 동일하다.
		System.out.println(file+"<---file");
		if(file.exists()) {
			file.delete();
		}
		int result =0;
		//2. 트랜잭션 처리
		Member member = new Member();
		result = memberMapper.deleteMember(loginMember);
		if(result==1) {
		member.setMemberId(loginMember.getMemberId());
		deletedMemberMapper.insertDeletedMember(member.getMemberId());
		}
		return result;
		
	}
	
	public int addDeletedMember(String deletedMemberId) {
		return deletedMemberMapper.insertDeletedMember(deletedMemberId);
	}
	public int replaceMember(MemberForm memberForm) {
		
		String oldMemberPic = memberMapper.selectMemberPic(memberForm.getMemberId());
		System.out.println(oldMemberPic+"<--oldMemberPictue");
		//1. 원래 있던 memberPic를 삭제한다.
		File file = new File(path+oldMemberPic);//사진은 memberId와 이름이 동일하다.
		System.out.println(file+"<---file");
		if(file.exists()) {
			file.delete();
			System.out.println("사진삭제성공.");
		}
		
		//2. 수정한 memberPic를 삽입
		MultipartFile mf = memberForm.getMemberPic();
		//확장자 필요
		String originName = mf.getOriginalFilename();
		/*
		if((mf.getContentType().equals("image/png"))||(mf.getContentType().equals("image/jpeg"))){
			//업로드
		} else {
			//실패
		}
		*/
		System.out.println(originName);
		//사진을 안넣을때는 default.png가 출려되게
		//그 외에는 밑에 문 실행
		int lastDot=originName.lastIndexOf("."); //  xxx.PNG
		String extension = originName.substring(lastDot);
		//새로운 이름을 생성
		String memberPic = memberForm.getMemberId()+extension;
		// 1.db에 저장
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberPic(memberPic);
		System.out.println(member+"<--memberService.addMember:member");
		//memberForm->member
		System.out.println("replaceMember");
		//return memberMapper.updateMember(member);
		int row = memberMapper.updateMember(member);
		// 2.파일저장
		//String path ="C:\\kkt\\sts_work\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload";
		// /는 리눅스  \는 윈도우
		File file2 = new File(path+memberPic);
		try {
			mf.transferTo(file2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
			/*자바의 예외에는 2가지가 있다
			 * 1. 예외처리를 해야만 문법적으로 이상없는 예외
			 * 2. 예외처리를 코드에서 구현하지 않아도 아무문제없는 예외 ex)RuntimeException()
			 */
		}
		return row;
		// 2.service보내기
		//3.
	}
	
}
