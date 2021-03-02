package com.myspring.market.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.market.goods.dao.GoodsDAO;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.goods.vo.ImageFileVO;
import com.myspring.market.heart.dao.HeartDAO;
import com.myspring.market.market.dao.MarketDAO;
import com.myspring.market.market.vo.MarketVO;
import com.myspring.market.review.dao.ReviewDAO;

@Transactional(propagation=Propagation.REQUIRED)
@Service("goodsService")
public class GoodsService {
	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private MarketDAO marketDAO;
	@Autowired
	private HeartDAO heartDAO;
	@Autowired
	private ReviewDAO reviewDAO;
	
	public List<GoodsVO> listAllGoods(int start) throws DataAccessException {
		List<GoodsVO> goodsList = goodsDAO.selectAllGoods(start);
		return goodsList;
	}
	
	public List<GoodsVO> listCategoryGoods(Map<String, Object> sortMap) throws DataAccessException {
		List<GoodsVO> goodsList = goodsDAO.selectCategoryGoods(sortMap);
		return goodsList;
	}
	
	public Map<String, Object> goodsDetail(int goods_id) throws DataAccessException {
		Map<String, Object> goodsMap = new HashMap<String, Object>();
		
		GoodsVO goods = goodsDAO.selectGoodsDetail(goods_id);
		goodsMap.put("goods", goods);
		
		List<ImageFileVO> imageFileList = goodsDAO.selectGoodsImageDetail(goods_id);
		goodsMap.put("imageFileList", imageFileList);
		
		String member_id = goods.getMember_id();
		MarketVO marketInfo = marketDAO.selectMyMarketInfo(member_id);
		List<GoodsVO> marketGoodsList = marketDAO.selectMyMarketGoodsList(member_id);
		
		goodsMap.put("marketInfo", marketInfo);
		goodsMap.put("marketGoodsList", marketGoodsList);
		
		return goodsMap;
	}
	
	public GoodsVO selectGoodsDetailById(int goods_id) throws DataAccessException {
		GoodsVO goodsVO = goodsDAO.selectGoodsDetailById(goods_id);
		return goodsVO;
	}
	
	public int addNewGoods(Map<String, Object> newGoodsMap) throws DataAccessException {
		int goods_id = goodsDAO.selectNewGoodsNO();
		newGoodsMap.put("goods_id", goods_id);

		goodsDAO.insertNewGoods(newGoodsMap);
		
		List<ImageFileVO> imageFileList = (List<ImageFileVO>) newGoodsMap.get("imageFileList");
		int image_id = goodsDAO.selectNewGoodsImageNO();
		for (ImageFileVO imageFileVO : imageFileList) {
			imageFileVO.setGoods_id(goods_id);
			imageFileVO.setImage_id(image_id);
			goodsDAO.insertNewGoodsImage(imageFileVO);
			image_id++;
		}
		return goods_id;
	}
	
	public int upGoodsHeartNum(int goods_id) throws DataAccessException {
		goodsDAO.upGoodsHeartNum(goods_id);
		int heart_num = goodsDAO.selectGoodsHeartNum(goods_id);
		return heart_num;
	}
	
	public int downGoodsHeartNum(int goods_id) throws DataAccessException {
		goodsDAO.downGoodsHeartNum(goods_id);
		int heart_num = goodsDAO.selectGoodsHeartNum(goods_id);
		return heart_num;
	}
	
	public void deleteGoods(int goods_id) throws DataAccessException {
		goodsDAO.deleteGoods(goods_id);
		goodsDAO.deleteGoodsImage(goods_id);
		heartDAO.deleteMemberHeart(goods_id);
		reviewDAO.deleteReview(goods_id);
	}
	
	public void removeGoodsImageFile(int image_id) throws DataAccessException {
		goodsDAO.deleteGoodsImageFile(image_id);
	}
	
	public void addNewGoodsImage(ImageFileVO imageFileVO) throws DataAccessException {
		int image_id = goodsDAO.selectNewGoodsImageNO();
		imageFileVO.setImage_id(image_id);
		goodsDAO.insertNewGoodsImage(imageFileVO);
	}
	
	public void modifyGoodsImageFile(ImageFileVO imageFileVO) throws DataAccessException {
		goodsDAO.updateGoodsImageFile(imageFileVO);
	}
	
	public void modifyGoodsInfo(GoodsVO goodsVO) throws DataAccessException {
		goodsDAO.updateGoodsInfo(goodsVO);
	}
	
	public void modifyGoodsStatus(Map<String, Object> statusMap) throws DataAccessException {
		goodsDAO.updateGoodsStatus(statusMap);
	}
	
	public List<GoodsVO> getBuyerGoodsList(String buyer_id) throws DataAccessException {
		List<GoodsVO> buyerGoodsList = goodsDAO.selectBuyerGoodsList(buyer_id);
		return buyerGoodsList;
	}
	
	public List<GoodsVO> searchGoodsByNew(Map<String, Object> keywordMap) throws DataAccessException {
		List<GoodsVO> searchGoodsList = goodsDAO.searchGoodsByNew(keywordMap);
		return searchGoodsList;
	}
	
	public List<GoodsVO> searchGoodsByLow(Map<String, Object> keywordMap) throws DataAccessException {
		List<GoodsVO> searchGoodsList = goodsDAO.searchGoodsByLow(keywordMap);
		return searchGoodsList;
	}
	
	public List<GoodsVO> searchGoodsByHigh(Map<String, Object> keywordMap) throws DataAccessException {
		List<GoodsVO> searchGoodsList = goodsDAO.searchGoodsByHigh(keywordMap);
		return searchGoodsList;
	}
	
	public int getSearchGoodsSize(String keyword) throws DataAccessException {
		int size = goodsDAO.selectSearchGoodsSize(keyword);
		return size;
	}
	
}
