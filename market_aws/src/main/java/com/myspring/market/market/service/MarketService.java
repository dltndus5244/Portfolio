package com.myspring.market.market.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.myspring.market.chat.vo.ChatroomVO;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.market.dao.MarketDAO;
import com.myspring.market.market.vo.MarketVO;

@Service("marketService")
public class MarketService {
	@Autowired
	private MarketDAO marketDAO;
	
	public Map<String, Object> myMarketInfo(String member_id) throws DataAccessException {
		Map<String, Object> marketMap = new HashMap<String, Object>();
		
		MarketVO marketInfo = marketDAO.selectMyMarketInfo(member_id);
		List<GoodsVO> myGoodsList = marketDAO.selectMyMarketGoodsList(member_id);
		
		marketMap.put("marketInfo", marketInfo);
		marketMap.put("myGoodsList", myGoodsList);
		
		return marketMap;
	}
	
	public MarketVO getMarketInfo(String member_id) throws DataAccessException {
		MarketVO marketInfo = marketDAO.selectMyMarketInfo(member_id);
		return marketInfo;
	}
	
	public void modifyMarketName(MarketVO marketVO) throws DataAccessException {
		marketDAO.updateMarketName(marketVO);
	}
	
	public void modifyMarketImage(Map<String, String> marketImageMap) throws DataAccessException {
		marketDAO.updateMarketImage(marketImageMap);
	}
	
}
