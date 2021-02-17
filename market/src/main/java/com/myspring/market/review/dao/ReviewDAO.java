package com.myspring.market.review.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.market.review.vo.ReviewVO;

@Repository("reviewDAO")
public class ReviewDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public int selectNewReviewNO() throws DataAccessException {
		int review_id = sqlSession.selectOne("mapper.review.selectNewReviewNO");
		return review_id + 1;
	}
	
	public void insertReview(ReviewVO reviewVO) throws DataAccessException {
		sqlSession.insert("mapper.review.insertReview", reviewVO);
	}
	
	public ReviewVO selectReview(Map<String, Object> reviewMap) throws DataAccessException {
		ReviewVO reviewVO = sqlSession.selectOne("mapper.review.selectReview", reviewMap);
		return reviewVO;
	}
	
	public void writeReview(ReviewVO reviewVO) throws DataAccessException {
		sqlSession.update("mapper.review.writeReview", reviewVO);
	}
	
	public List<ReviewVO> selectReviewList(String member_id) throws DataAccessException {
		List<ReviewVO> reviewList = sqlSession.selectList("mapper.review.selectReviewList", member_id);
		return reviewList;
	}
	
	public void deleteReview(int goods_id) throws DataAccessException {
		sqlSession.delete("mapper.review.deleteReview", goods_id);
	}
}
