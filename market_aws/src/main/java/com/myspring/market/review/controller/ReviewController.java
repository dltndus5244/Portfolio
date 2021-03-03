package com.myspring.market.review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.myspring.market.chat.service.ChatService;
import com.myspring.market.goods.service.GoodsService;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.market.vo.MarketVO;
import com.myspring.market.review.service.ReviewService;
import com.myspring.market.review.vo.ReviewVO;

@Controller("reviewController")
@RequestMapping("/review")
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private GoodsService goodsService;
	
	//구매자 선택 창
	@RequestMapping(value="/buyerSelectForm.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> buyerSelectForm(@RequestParam("goods_id") int goods_id,
											   @RequestParam("seller_id") String seller_id,
											   HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> chatMap = new HashMap<String, Object>();
		chatMap.put("goods_id", goods_id);
		chatMap.put("seller_id", seller_id);
		
		Map<String, Object> buyerMap = new HashMap<String, Object>();
		
		List<MarketVO> chatMemberList = chatService.getChatMembers(chatMap);
		GoodsVO goods = goodsService.selectGoodsDetailById(goods_id);
		
		buyerMap.put("chatMemberList", chatMemberList);
		buyerMap.put("goods", goods);
		
		return buyerMap;
	}
	
	//구매자 선택
	@RequestMapping(value="/selectBuyerId.do", method=RequestMethod.POST)
	public void selectBuyerId(@ModelAttribute("reviewVO") ReviewVO reviewVO,
							  HttpServletRequest request, HttpServletResponse response) throws Exception {
		reviewService.addReview(reviewVO);
	}
	
	//리뷰 모달 창
	@RequestMapping(value="/reviewModal.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> reviewModal(@RequestParam("goods_id") int goods_id,
										   @RequestParam("buyer_id") String buyer_id,
										   HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> _reviewMap = new HashMap<String, Object>();
		_reviewMap.put("goods_id", goods_id);
		_reviewMap.put("buyer_id", buyer_id);
		Map<String, Object> reviewMap = reviewService.getReview(_reviewMap);

		return reviewMap;
	}
	
	//리뷰 등록
	@RequestMapping(value="/writeReview.do", method=RequestMethod.POST)
	public void writeReview(@ModelAttribute("reviewVO") ReviewVO reviewVO,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		reviewService.writeReview(reviewVO);
	}
}
