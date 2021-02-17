package com.myspring.market.review.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.myspring.market.goods.dao.GoodsDAO;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.review.dao.ReviewDAO;
import com.myspring.market.review.vo.ReviewVO;

@Service("reviewService")
public class ReviewService {
	@Autowired
	private ReviewDAO reviewDAO;
	@Autowired
	private GoodsDAO goodsDAO;
	
	public void addReview(ReviewVO reviewVO) throws DataAccessException {
		int review_id = reviewDAO.selectNewReviewNO();
		reviewVO.setReview_id(review_id);
		reviewDAO.insertReview(reviewVO);
	}
	
	public Map<String, Object> getReview(Map<String, Object> _reviewMap) throws DataAccessException {
		ReviewVO reviewVO = reviewDAO.selectReview(_reviewMap);
		
		int goods_id = (int) _reviewMap.get("goods_id");
		GoodsVO goodsVO = goodsDAO.selectGoodsDetailById(goods_id);
		
		Map<String, Object> reviewMap = new HashMap<String, Object>();
		reviewMap.put("reviewVO", reviewVO);
		reviewMap.put("goodsVO", goodsVO);
		
		return reviewMap;
	}
	
	public void writeReview(ReviewVO reviewVO) throws DataAccessException {
		reviewDAO.writeReview(reviewVO);
	}
	
	public List<ReviewVO> selectReviewList(String member_id) throws DataAccessException {
		List<ReviewVO> reviewList = reviewDAO.selectReviewList(member_id);
		return reviewList;
	}
}
