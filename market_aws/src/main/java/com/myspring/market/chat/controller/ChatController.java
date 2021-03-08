package com.myspring.market.chat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.WebSocketSession;

import com.myspring.market.chat.service.ChatService;
import com.myspring.market.chat.vo.ChatroomVO;
import com.myspring.market.chat.vo.MessageVO;
import com.myspring.market.goods.service.GoodsService;
import com.myspring.market.goods.vo.GoodsVO;
import com.myspring.market.market.service.MarketService;
import com.myspring.market.market.vo.MarketVO;
import com.myspring.market.member.vo.MemberVO;

@Controller("chatController")
@RequestMapping("/chat")
public class ChatController {
	@Autowired
	private ChatService chatService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ChatroomVO chatroomVO;
	@Autowired
	private MessageVO messageVO;
	@Autowired
	private MarketService marketService;
	@Autowired
	private MarketVO seller_market_info;
	@Autowired
	private MarketVO buyer_market_info;
	
	@RequestMapping(value="/newChatroomForm.do", method=RequestMethod.POST)
	public ModelAndView newChatroomForm(@RequestParam("goods_id") int goods_id,
								 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		
		Map<String, Object> goodsMap = goodsService.goodsDetail(goods_id);
		GoodsVO goods = (GoodsVO) goodsMap.get("goods");
		String seller_id = goods.getMember_id();
		
		HttpSession session = request.getSession();
		MemberVO buyerInfo = (MemberVO) session.getAttribute("memberInfo");
		String buyer_id = buyerInfo.getMember_id();
		
		chatroomVO.setGoods_id(goods_id);
		chatroomVO.setSeller_id(seller_id);
		chatroomVO.setBuyer_id(buyer_id);
		
		ChatroomVO existedRoom = chatService.existedChatroom(chatroomVO);
		
		//이미 생성돼 있는 채팅방이 있을 경우 기존 채팅방으로 redirect
		if (existedRoom != null) {
			return chatroomForm(existedRoom.getChatroom_id(), request, response);
		}
		else { //없을 경우 채팅방 정보 mav에 add
			mav.setViewName(viewName);
			seller_market_info = marketService.getMarketInfo(seller_id);
			chatroomVO.setSeller_market_image(seller_market_info.getMarket_image());
			
			buyer_market_info = marketService.getMarketInfo(buyer_id);
			chatroomVO.setBuyer_market_image(buyer_market_info.getMarket_image());
					
			mav.addObject("chatroom", chatroomVO);
			
			GoodsVO goodsVO = goodsService.selectGoodsDetailById(goods_id);
			mav.addObject("goodsVO", goodsVO);
			
			return mav;
		}
	}
	
	@RequestMapping(value="/chatroomForm.do", method = RequestMethod.POST)
	public ModelAndView chatroomForm(@RequestParam("chatroom_id") int chatroom_id,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		
		if (viewName.equals("/chat/newChatroomForm"))
			viewName = "/chat/chatroomForm";
		
		ModelAndView mav = new ModelAndView(viewName);

		chatroomVO = chatService.getChatroom(chatroom_id);
		List<MessageVO> messageList = chatService.getMessageList(chatroom_id);

		int goods_id = chatroomVO.getGoods_id();
		GoodsVO goodsVO = goodsService.selectGoodsDetailById(goods_id);
		
		mav.addObject("chatroom", chatroomVO);
		mav.addObject("messageList", messageList);
		mav.addObject("goodsVO", goodsVO);
		
		HttpSession session = request.getSession();
		session.setAttribute("chatroom_id", chatroom_id);
		
		return mav;
	}
	
	@RequestMapping(value="/getChatroomList.do", method=RequestMethod.POST)
	public ModelAndView chatList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberInfo.getMember_id();
		
		List<ChatroomVO> chatroomList = chatService.getChatroomList(member_id); //멤버의 채팅방 리스트 가져옴
		
		List<MessageVO> lastMessageList = new ArrayList<MessageVO>(); //채팅방의 마지막 메시지를 담을 리스트
		int[] noreadSizeList = new int[chatroomList.size()]; //읽지 않은 메시지의 개수를 담을 배열
		
		for (int i=0; i<chatroomList.size(); i++) {
			int chatroom_id = chatroomList.get(i).getChatroom_id();
			List<MessageVO> messageList = chatService.getMessageList(chatroom_id);
			lastMessageList.add(messageList.get(messageList.size()-1)); //마지막 메시지
			
			messageVO.setChatroom_id(chatroom_id);
			messageVO.setMessage_receiver(member_id);
			List<MessageVO> noreadMessageList = chatService.getNoReadMessage(messageVO);
			
			noreadSizeList[i] = noreadMessageList.size();
		}
			
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		mav.addObject("chatroomList", chatroomList);
		mav.addObject("lastMessageList", lastMessageList);
		mav.addObject("noreadSizeList", noreadSizeList);
	
		return mav;
	}

	
	@RequestMapping(value="/addChatroom.do", method=RequestMethod.POST)
	@ResponseBody
	public int addChatroom(@RequestParam("goods_id") int goods_id,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> goodsMap = goodsService.goodsDetail(goods_id);
		GoodsVO goods = (GoodsVO) goodsMap.get("goods");
		String seller_id = goods.getMember_id();
		
		HttpSession session = request.getSession();
		MemberVO buyerInfo = (MemberVO) session.getAttribute("memberInfo");
		String buyer_id = buyerInfo.getMember_id();
		
		chatroomVO.setGoods_id(goods_id);
		chatroomVO.setSeller_id(seller_id);
		chatroomVO.setBuyer_id(buyer_id);
		
		int chatroom_id = chatService.addChatroom(chatroomVO);
		
		return chatroom_id;
	}
	
	@RequestMapping(value="/addMessage.do", method=RequestMethod.POST)
	public void addMessage(@ModelAttribute("messageVO") MessageVO messageVO,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		chatService.addMessage(messageVO);
	}
	
	@RequestMapping(value="/getNoReadMessage.do", method=RequestMethod.POST)
	@ResponseBody
	public List<MessageVO> getNoReadMessage(@ModelAttribute("messageVO") MessageVO messageVO) throws Exception {
		List<MessageVO> noreadMessageList = chatService.getNoReadMessage(messageVO);
		return noreadMessageList;
	}
	
	@RequestMapping(value="/modifyMessageRead.do", method=RequestMethod.POST)
	public void modifyMessageRead(@ModelAttribute("messageVO") MessageVO messageVO, 
								  HttpServletRequest request, HttpServletResponse response) throws Exception {
		chatService.modifyMessageRead(messageVO);
	}
}
