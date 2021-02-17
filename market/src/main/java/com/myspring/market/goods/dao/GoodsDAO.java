package com.myspring.market.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.goods.vo.ImageFileVO;
import com.myspring.market.market.vo.MarketVO;

@Repository("goodsDAO")
public class GoodsDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public List<GoodsVO> selectAllGoods(int start) throws DataAccessException {
		List<GoodsVO> goodsList = sqlSession.selectList("mapper.goods.selectAllGoods", start);
		return goodsList;
	}
	
	public List<GoodsVO> selectCategoryGoods(Map<String, Object> sortMap) throws DataAccessException {
		List<GoodsVO> goodsList = sqlSession.selectList("mapper.goods.selectCategoryGoods", sortMap);
		return goodsList;
	}
	
	public GoodsVO selectGoodsDetailById(int goods_id) throws DataAccessException {
		GoodsVO goodsVO = sqlSession.selectOne("mapper.goods.selectGoodsDetailById", goods_id);
		return goodsVO;
	}
	
	public GoodsVO selectGoodsDetail(int goods_id) throws DataAccessException {
		GoodsVO goods = sqlSession.selectOne("mapper.goods.selectGoodsDetail", goods_id);
		return goods;
	}
	
	public List<ImageFileVO> selectGoodsImageDetail(int goods_id) throws DataAccessException {
		List<ImageFileVO> imageFileList = sqlSession.selectList("mapper.goods.selectGoodsDetailImage", goods_id);
		return imageFileList;
	}

	public int selectNewGoodsNO() throws DataAccessException {
		int goods_id = sqlSession.selectOne("mapper.goods.selectNewGoodsNO");
		return goods_id+1;
	}
	
	public int selectNewGoodsImageNO() throws DataAccessException {
		int image_id = sqlSession.selectOne("mapper.goods.selectNewGoodsImageVO");
		return image_id+1;
	}
	
	public void insertNewGoods(Map<String, Object> newGoodsMap) throws DataAccessException {
		sqlSession.insert("mapper.goods.insertNewGoods", newGoodsMap);
	}
	
	public void insertNewGoodsImage(ImageFileVO imageFileVO) throws DataAccessException {
		sqlSession.insert("mapper.goods.insertNewGoodsImage", imageFileVO);
	}
	
	public void upGoodsHeartNum(int goods_id) throws DataAccessException {
		sqlSession.update("mapper.goods.upGoodsHeartNum", goods_id);
	}
	
	public void downGoodsHeartNum(int goods_id) throws DataAccessException {
		sqlSession.update("mapper.goods.downGoodsHeartNum", goods_id);
	}
	
	public int selectGoodsHeartNum(int goods_id) throws DataAccessException {
		int heart_num = sqlSession.selectOne("mapper.goods.selectGoodsHeartNum", goods_id);
		return heart_num;
	}
	
	public void deleteGoods(int goods_id) throws DataAccessException {
		sqlSession.delete("mapper.goods.deleteGoods", goods_id);
	}
	
	public void deleteGoodsImage(int goods_id) throws DataAccessException {
		sqlSession.delete("mapper.goods.deleteGoodsImage", goods_id);
	}
	
	public void deleteGoodsImageFile(int image_id) throws DataAccessException {
		sqlSession.delete("mapper.goods.deleteGoodsImageFile", image_id);
	}
	
	public void updateGoodsImageFile(ImageFileVO imageFileVO) throws DataAccessException {
		sqlSession.update("mapper.goods.updateGoodsImageFile", imageFileVO);
	}
	
	public void updateGoodsInfo(GoodsVO goodsVO) throws DataAccessException {
		sqlSession.update("mapper.goods.updateGoodsInfo", goodsVO);
	}
	
	public void updateGoodsStatus(Map<String, Object> statusMap) throws DataAccessException {
		sqlSession.update("mapper.goods.updateGoodsStatus", statusMap);
	}
	
	public List<GoodsVO> selectBuyerGoodsList(String buyer_id) throws DataAccessException {
		List<GoodsVO> buyerGoodsList = sqlSession.selectList("mapper.goods.selectBuyerGoodsList", buyer_id);
		return buyerGoodsList;
	}
	
	public List<GoodsVO> searchGoodsByNew(String keyword) throws DataAccessException {
		List<GoodsVO> searchGoodsList = sqlSession.selectList("mapper.goods.searchGoodsByNew", keyword);
		return searchGoodsList;
	}
	
	public List<GoodsVO> searchGoodsByLow(String keyword) throws DataAccessException {
		List<GoodsVO> searchGoodsList = sqlSession.selectList("mapper.goods.searchGoodsByLow", keyword);
		return searchGoodsList;
	}
	
	public List<GoodsVO> searchGoodsByHigh(String keyword) throws DataAccessException {
		List<GoodsVO> searchGoodsList = sqlSession.selectList("mapper.goods.searchGoodsByHigh", keyword);
		return searchGoodsList;
	}
	
}
