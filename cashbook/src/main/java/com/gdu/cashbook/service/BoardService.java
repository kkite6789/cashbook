package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Comment;

@Service
@Transactional
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	public Map<String,Object> getBoardList(int currentPage,int rowPerPage){
		Map<String,Object>map = new HashMap<>();
		int beginRow=(currentPage-1)*rowPerPage;
		int totalRow = boardMapper.selectTotalRow();
		System.out.println(totalRow+"<--totalRow");
		int lastPage = totalRow / rowPerPage;
		if(totalRow % rowPerPage != 0) {
			lastPage += 1;
		}
		
		List<Board> list = boardMapper.selectBoardList(beginRow, rowPerPage);
		map.put("lastPage", lastPage);
		map.put("list", list);
		return map;
	}
	public Board getBoardOne(int boardNo) {
		return boardMapper.selectBoardOne(boardNo);
	}
	public List<Comment> getCommentList(int boardNo){
		System.out.println("commentservice시작");
		List<Comment> commentList =boardMapper.selectCommentList(boardNo);
		System.out.println(commentList.toString());
		return commentList;
		
	
	}
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	public int replaceBoard(Board board) {
		return boardMapper.updateBoard(board);
	}
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
	}
	
	public int addComment(Comment comment) {
		return boardMapper.insertComment(comment);
	}
	public int replaceComment(Comment comment) {
		return boardMapper.updateComment(comment);
	}
	public int removeComment(Comment comment) {
		return boardMapper.deleteComment(comment);
	}
	
}
