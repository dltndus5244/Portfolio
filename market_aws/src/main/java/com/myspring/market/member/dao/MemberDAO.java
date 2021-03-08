package com.myspring.market.member.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.market.member.vo.MemberVO;

@Repository("memberDAO")
public class MemberDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public void insertMember(MemberVO memberVO) throws DataAccessException {
		sqlSession.insert("mapper.member.insertMember", memberVO);
	}
	
	public String selectOverlappedID(String id) throws DataAccessException {
		String result = sqlSession.selectOne("mapper.member.selectOverlappedID", id);
		return result;
	}
	
	public MemberVO login(MemberVO memberVO) throws DataAccessException {
		MemberVO memberInfo = sqlSession.selectOne("mapper.member.login", memberVO);
		return memberInfo;
	}
	
	public void insertMarket(String member_id) throws DataAccessException {
		sqlSession.insert("mapper.market.insertMarket", member_id);
	}
	
	public void updateMyInfo(MemberVO memberVO) throws DataAccessException {
		sqlSession.update("mapper.member.updateMyInfo", memberVO);
	}
	
	public void updateMyPw(MemberVO memberVO) throws DataAccessException {
		sqlSession.update("mapper.member.updateMyPw", memberVO);
	}
	
}
