package com.myspring.market.chat.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.myspring.market.chat.controller.ChatController;
import com.myspring.market.chat.service.ChatService;
import com.myspring.market.chat.vo.MessageVO;
import com.myspring.market.chat.vo.SocketVO;


public class ChatHandler extends TextWebSocketHandler {
	//웹 소켓 세션을 저장할 리스트
	public static List<SocketVO> chatSessionList = new ArrayList<SocketVO>();
	
	@Autowired
	private ChatService chatService;
	@Autowired
	private MessageVO messageVO;
	@Autowired
	private NewChatHandler newChatHandler;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		SocketVO socketVO = new SocketVO();
		socketVO.setSession(session);
		
		Map<String, Object> map = session.getAttributes();
		String member_id = (String) map.get("member_id");
		int chatroom_id = (int) map.get("chatroom_id");
		
		socketVO.setMember_id(member_id);
		socketVO.setChatroom_id(chatroom_id);
		
		chatSessionList.add(socketVO);
		
		System.out.println("[기존 채팅방 입장 멤버 리스트]");
		for (int i=0; i<chatSessionList.size(); i++) {
			SocketVO socket = chatSessionList.get(i);
			System.out.println("채팅방"+ socket.getChatroom_id() + " : " + socket.getMember_id());
		}
	}
	
	//웹 소켓 서버로 데이터를 전송했을 경우
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String msg = message.getPayload();
		JSONObject obj = jsonToObjectParser(msg);
		
		String message_sender = (String) obj.get("member_id");
		String message_receiver = (String) obj.get("message_receiver");
		String s_chatroom_id = (String) obj.get("chatroom_id");
		int chatroom_id = Integer.parseInt(s_chatroom_id);
					
		//같은 채팅방에 있는 사람들한테만 메시지를 보냄
		for (SocketVO sess : chatSessionList) {
			if (chatroom_id == sess.getChatroom_id()) {
				sess.getSession().sendMessage(new TextMessage(obj.toJSONString()));
			}
		}
		
		//새로운 채팅방에 있는 사람들과도 채팅을 할 수 있도록 메시지를 보냄
		for (SocketVO sess : NewChatHandler.newChatSessionList) {
			if (chatroom_id == sess.getChatroom_id()) {
				newChatHandler.sendMessageToNewChatroom(chatroom_id, obj);
			}
		}
		
		System.out.println("[기존 채팅방에서 참조하는 뉴 챗 리스트]");
		for (SocketVO sess : NewChatHandler.newChatSessionList) {
			System.out.println("채팅방"+sess.getChatroom_id() + ":" + sess.getMember_id());
		}
		
		boolean isExisted = false;
		//메시지를 받는 사람이 현재 채팅방에 접속중이 아닐 때만 알림을 보냄 & 접속중이면 메시지를 읽음 상태로 변경
		for (SocketVO sess : chatSessionList) {
			if (message_receiver.equals(sess.getMember_id()) && chatroom_id == sess.getChatroom_id()) { //같을 경우 -> 읽음 상태
				isExisted = true;
	
				messageVO.setChatroom_id(chatroom_id);
				messageVO.setMessage_receiver(message_receiver);
		
				chatService.modifyMessageRead(messageVO);
			} 
		}
		
		for (SocketVO sess : NewChatHandler.newChatSessionList) {
			if (message_receiver.equals(sess.getMember_id()) && chatroom_id == sess.getChatroom_id()) { //같을 경우 -> 읽음 상태
				isExisted = true;
	
				messageVO.setChatroom_id(chatroom_id);
				messageVO.setMessage_receiver(message_receiver);
		
				chatService.modifyMessageRead(messageVO);
			} 
		}
		
		if (isExisted == false) {
			NoticeHandler noticeHandler = new NoticeHandler();
			noticeHandler.notice(message_sender, message_receiver);
		}
		
	}

	//클라이언트와 연결이 끊어진 경우
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//연결 끊어질 때 
		for (int i=0; i<chatSessionList.size(); i++) {
			if (chatSessionList.get(i).getSession().getId() == session.getId()) {
				chatSessionList.remove(i);
			}
		}
		
		System.out.println("[채팅방 입장 멤버 리스트]");
		for (int i=0; i<chatSessionList.size(); i++) {
			SocketVO socket = chatSessionList.get(i);
			System.out.println("채팅방"+ socket.getChatroom_id() + " : " + socket.getMember_id());
		}
	}
	
	private static JSONObject jsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public void sendMessageToChatroom(int chatroom_id, JSONObject obj) throws IOException {
		for (SocketVO sess : chatSessionList) {
			if (chatroom_id == sess.getChatroom_id()) {
				sess.getSession().sendMessage(new TextMessage(obj.toJSONString()));
			}
		}
	}
}
