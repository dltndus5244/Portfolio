package com.myspring.market.heart.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.market.heart.vo.HeartVO;

@Repository("heartDAO")
public class HeartDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public int selectNewMemberHeartNO() throws DataAccessException {
		int heart_id = sqlSession.selectOne("mapper.heart.selectNewMemberHeartNO");
		return heart_id + 1;
	}
	
	public void insertMemberHeart(HeartVO heartVO) throws DataAccessException {
		sqlSession.insert("mapper.heart.insertMemberHeart", heartVO);
	}
	
	public void deleteMemberHeart(HeartVO heartVO) throws DataAccessException {
		sqlSession.delete("mapper.heart.deleteMemberHeart", heartVO);
	}
	
	public List<HeartVO> selectMemberHeartList(String member_id) throws DataAccessException {
		List<HeartVO> memberHeartList = sqlSession.selectList("mapper.heart.selectMemberHeartList", member_id);
		return memberHeartList;
	}
	
	public void deleteMemberHeart(int goods_id) throws DataAccessException {
		sqlSession.delete("mapper.heart.deleteMemberHeartById", goods_id);
	}
}
