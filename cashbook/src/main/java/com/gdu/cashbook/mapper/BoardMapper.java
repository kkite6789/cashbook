package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Comment;
@Mapper
public interface BoardMapper {

	public List<Board> selectBoardList(Map<String,Object> map);
	public Board selectBoardOne(int boardNo);
	public List<Comment> selectCommentList(Map<String,Object> map);
	
	public int insertBoard(Board board);
	public int updateBoard(Board board);
	public int deleteBoard(Board board);
	
	public int insertComment(Comment comment);
	public int updateComment(Comment comment);
	public int deleteComment(Comment comment);
	public int selectTotalRow();
	public int selectCommentTotalRow();
}
