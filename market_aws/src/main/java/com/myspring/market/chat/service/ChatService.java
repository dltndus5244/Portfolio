package com.myspring.market.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.market.chat.dao.ChatDAO;
import com.myspring.market.chat.vo.ChatroomVO;
import com.myspring.market.chat.vo.MessageVO;
import com.myspring.market.market.dao.MarketDAO;
import com.myspring.market.market.vo.MarketVO;
@Transactional(propagation=Propagation.REQUIRED)
@Service("chatService")
public class ChatService {
	@Autowired
	private ChatDAO chatDAO;
	@Autowired
	private MarketDAO marketDAO;
	
	public int addChatroom(ChatroomVO chatroomVO) throws DataAccessException {
		ChatroomVO chatroom = chatDAO.overlapChatroom(chatroomVO);
		int chatroom_id = 0;
		
		if (chatroom == null) {
			chatroom_id = chatDAO.selectNewChatroomNO();
			chatroomVO.setChatroom_id(chatroom_id);
			chatDAO.insertChatroom(chatroomVO);
		}
		return chatroom_id;
	}
	
	public List<ChatroomVO> getChatroomList(String id) throws DataAccessException {
		List<String> chatroomIdList = chatDAO.selectChatroomList(id);
		List<ChatroomVO> chatroomList = new ArrayList<ChatroomVO>();
		
		for (String s_chatroom_id : chatroomIdList) {
			ChatroomVO chatroomVO = new ChatroomVO();
			
			int chatroom_id = Integer.parseInt(s_chatroom_id);
			chatroomVO.setChatroom_id(chatroom_id);
			
			ChatroomVO c = chatDAO.selectChatroom(chatroomVO.getChatroom_id());
			
			chatroomVO.setGoods_id(c.getGoods_id());
			chatroomVO.setBuyer_id(c.getBuyer_id());
			chatroomVO.setSeller_id(c.getSeller_id());
			
			String buyer_id = chatroomVO.getBuyer_id();
			String seller_id = chatroomVO.getSeller_id();
			
			MarketVO buyer_market = marketDAO.selectMyMarketInfo(buyer_id);
			MarketVO seller_market = marketDAO.selectMyMarketInfo(seller_id);
			
			String buyer_maket_image = buyer_market.getMarket_image();
			String seller_market_image = seller_market.getMarket_image();
			
			chatroomVO.setBuyer_market_image(buyer_maket_image);
			chatroomVO.setSeller_market_image(seller_market_image);
			
			chatroomList.add(chatroomVO);
		}

		return chatroomList;
	}
	
	public void addMessage(MessageVO messageVO) throws DataAccessException {
		int message_id = chatDAO.selectNewMessageNO();
		messageVO.setMessage_id(message_id);
		chatDAO.insertMessage(messageVO);
	}
	
	public List<MessageVO> getMessageList(int chatroom_id) throws DataAccessException {
		List<MessageVO> messageList = chatDAO.selectMessageList(chatroom_id);
		return messageList;
	}
	
	public ChatroomVO getChatroom(int chatroom_id) throws DataAccessException {
		ChatroomVO chatroom = chatDAO.selectChatroom(chatroom_id);
		
		String buyer_id = chatroom.getBuyer_id();
		String seller_id = chatroom.getSeller_id();
		
		MarketVO buyer_market = marketDAO.selectMyMarketInfo(buyer_id);
		MarketVO seller_market = marketDAO.selectMyMarketInfo(seller_id);
		
		String buyer_maket_image = buyer_market.getMarket_image();
		String seller_market_image = seller_market.getMarket_image();
		
		chatroom.setBuyer_market_image(buyer_maket_image);
		chatroom.setSeller_market_image(seller_market_image);
		
		return chatroom;
	}
	
	public List<MessageVO> getNoReadMessage(MessageVO messageVO) throws DataAccessException {
		List<MessageVO> noreadMessageList = chatDAO.selectNoReadMessage(messageVO);
		return noreadMessageList;
	}
	
	public void modifyMessageRead(MessageVO messageVO) throws DataAccessException {
		chatDAO.updateMessageRead(messageVO);
	}
	
	public List<MarketVO> getChatMembers(Map<String, Object> chatMap) throws DataAccessException {
		List<MarketVO> chatMemberList = new ArrayList<MarketVO>();
		List<String> chatMemberIdList = chatDAO.selectChatMembers(chatMap);
		
		for (String member_id : chatMemberIdList) {
			MarketVO marketInfo = marketDAO.selectMyMarketInfo(member_id);
			String market_image = marketInfo.getMarket_image();
			
			MarketVO marketVO = new MarketVO();
			marketVO.setMember_id(member_id);
			marketVO.setMarket_image(market_image);
			
			chatMemberList.add(marketVO);
		}
		return chatMemberList;
	}
	
	public ChatroomVO existedChatroom(ChatroomVO chatroomVO) throws DataAccessException {
		ChatroomVO chatroom = chatDAO.existedChatroom(chatroomVO);
		return chatroom;
	}
	
	public void removeChatroom(int goods_id) throws DataAccessException {
		int[] chatroomIdList = chatDAO.selectChatroomListByGoodsId(goods_id);
		if (chatroomIdList != null) {
			for (int i=0; i<chatroomIdList.length; i++) {
				int chatroom_id = chatroomIdList[i];
				if (chatroom_id != 0) {
					System.out.println("시작");
					chatDAO.deleteMessage(chatroom_id);
					System.out.println(chatroom_id + "메시지 삭제 완료");
				}
			}
		}
		chatDAO.deleteChatroom(goods_id);
	}
}
