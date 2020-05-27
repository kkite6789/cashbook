package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Comment;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class BoardController {
	
	@Autowired
	public BoardService boardService;

	@PostMapping("/addComment")
	public String addComment(HttpSession session,Comment comment,Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println("add comment Post 시작");
		comment.setCommentWriter(memberId);
		System.out.println(comment.toString());
		
		boardService.addComment(comment);
		
		return "redirect:/getBoardOne?boardNo=" + comment.getBoardNo();
	}
	
	@GetMapping("/removeComment")
	public String removeComment(HttpSession session,Comment comment) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("delete comment get 시작");
		
		boardService.removeComment(comment);
		return "redirect:/getBoardOne?boardNo=" + comment.getBoardNo();
	}
	
	@GetMapping("/removeBoard")
	public String removeBoard(HttpSession session,Board board) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("delete board get 시작");
		
		System.out.println(board.toString());
		boardService.removeBoard(board);
		System.out.println("삭제 성공!");
		return "redirect:/getBoardList";
	}
	@GetMapping("/replaceComment")
	public String replaceComment(HttpSession session,Board board,Model model,Comment comment) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("replace board get 시작");
		System.out.println(comment.getCommentNo()+"<--commentNo");
		System.out.println(board.getBoardNo()+"<--boardNo");
		model.addAttribute("commentNo", comment.getCommentNo());
		model.addAttribute("boardNo", board.getBoardNo());
		return "replaceComment";
	}
	@PostMapping("/replaceComment")
	public String replaceComment(HttpSession session,Comment comment) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("replacecomment 시작");
		System.out.println(comment.toString());
		
		
		System.out.println(comment.getCommentContent()+"<--commentContent");
		System.out.println(comment.getBoardNo()+"<--boardNo");
		
		boardService.replaceComment(comment);
		
		return "redirect:/getBoardOne?boardNo=" + comment.getBoardNo();
	}
	
	@GetMapping("/replaceBoard")
	public String replaceBoard(HttpSession session,Board board,Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		
		LocalDate day=LocalDate.now();
		System.out.println("replace board get 시작");
		
		System.out.println(board.getBoardNo()+"<--boardNo");
		System.out.println(day+"<--day");
		
		model.addAttribute("day", day);
		model.addAttribute("memberId", memberId);
		model.addAttribute("boardNo", board.getBoardNo());
		return "replaceBoard";
	}
	
	@PostMapping("/replaceBoard")
	public String replaceBoard(HttpSession session,Model model,Board board) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("replace board post 시작");
		System.out.println(board.toString());
		boardService.replaceBoard(board);
		return "redirect:/getBoardOne?boardNo=" + board.getBoardNo();
	}
	
	@GetMapping("/addBoard")
	public String addBoard(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		return ("addBoard");
	}
	
	@PostMapping("/addBoard")
	public String addBoard(HttpSession session,Board board) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("addBoard controller 시작");
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		
		board.setBoardWriter(memberId);
		System.out.println(board.toString());
		
		boardService.addBoard(board);
		
		return "redirect:/getBoardList";
	}
	
	//보드중에 하나만
	@GetMapping("/getBoardOne")
	public String getBoardOne(HttpSession session,@RequestParam(value="boardNo")int boardNo,Board board,Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("getBoardOne controller 시작");
		System.out.println(board.toString());
		System.out.println(board.getBoardNo()+"<--해당 boardNo");
		
		board=boardService.getBoardOne(board.getBoardNo());
		System.out.println(boardNo+"<--boardNo");
		System.out.println(board.toString());
		System.out.println(board.getBoardNo()+"<--해당 boardNo  (댓글검색에 넣을 boardNo)");
		List<Comment>commentList=boardService.getCommentList(boardNo);
		
		System.out.println(board.toString());
		System.out.println(commentList.toString());
		model.addAttribute("commentList", commentList);
		model.addAttribute("board", board);
		return "getBoardOne";
	}
	
	//전체보드
	@GetMapping("/getBoardList")
	public String getBoardList(HttpSession session,Model model,@RequestParam(value="currentPage", defaultValue="1")int currentPage) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		System.out.println("getBoardList controller 시작");
		int rowPerPage=1;

		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		rowPerPage=10;
		Map<String, Object> boardList = boardService.getBoardList(currentPage,rowPerPage);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("memberId", memberId);
		model.addAttribute("lastPage", boardList.get("lastPage"));
		
		model.addAttribute("boardList", boardList.get("list"));
		
		return "getBoardList";
	}
	/*
	@GetMapping("/getBoardOne")
	public String detailView(Model model, HttpSession session, @RequestParam(value="boardNo") int boardNo,
			@RequestParam(value="currentPage", defaultValue="1") int currentPage) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		Map<String, Object> detailView = boardService.getBoardOne(boardNo, currentPage);
		model.addAttribute("memberId", memberId);
		model.addAttribute("boardOne", detailView.get("boardOne"));
		model.addAttribute("lastPage", detailView.get("lastPage"));
		model.addAttribute("commentList", detailView.get("commentList"));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("boardNo", boardNo);
		return "/detailView";
	}
	*/

}
