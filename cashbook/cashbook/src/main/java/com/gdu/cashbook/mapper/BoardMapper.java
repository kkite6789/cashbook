package com.gdu.cashbook.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Comment;
@Mapper
public interface BoardMapper {

	public List<Board> selectBoardList(int beginRow, int rowPerPage);
	public Board selectBoardOne(int boardNo);
	public List<Comment> selectCommentList(int boardNo);
	
	
	public List<Comment> selectCommentList2(int boardNo, int beginRow, int rowPerPage);
	
	public int insertBoard(Board board);
	public int updateBoard(Board board);
	public int deleteBoard(Board board);
	
	
	public int insertComment(Comment comment);
	public int updateComment(Comment comment);
	public int deleteComment(Comment comment);
	public int selectTotalRow();
}
