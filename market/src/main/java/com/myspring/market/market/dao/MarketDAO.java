package com.myspring.market.market.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.market.vo.MarketVO;

@Repository("marketDAO")
public class MarketDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public MarketVO selectMyMarketInfo(String member_id) throws DataAccessException {
		MarketVO marketInfo = sqlSession.selectOne("mapper.market.selectMyMarketInfo", member_id);
		return marketInfo;
	}
	
	public List<GoodsVO> selectMyMarketGoodsList(String member_id) throws DataAccessException {
		List<GoodsVO> myGoodsList = sqlSession.selectList("mapper.goods.selectMyMarketGoodsList", member_id);
		return myGoodsList;
	}
	
	public void updateMarketName(MarketVO marketVO) throws DataAccessException {
		sqlSession.update("mapper.market.updateMarketName", marketVO);
	}
	
	public void updateMarketImage(Map<String, String> marketImageMap) throws DataAccessException {
		sqlSession.update("mapper.market.updateMarketImage", marketImageMap);
	}
}
