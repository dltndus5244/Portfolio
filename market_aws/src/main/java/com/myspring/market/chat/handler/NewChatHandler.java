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

public class NewChatHandler extends TextWebSocketHandler {
	// �� ���� ������ ������ ����Ʈ
	public static List<SocketVO> newChatSessionList = new ArrayList<SocketVO>();
	@Autowired
	private ChatService chatService;
	@Autowired
	private MessageVO messageVO;
	@Autowired
	private ChatHandler chatHandler;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		SocketVO socketVO = new SocketVO();
		socketVO.setSession(session);

		Map<String, Object> map = session.getAttributes();
		String member_id = (String) map.get("member_id");

		socketVO.setMember_id(member_id);
		newChatSessionList.add(socketVO);

		System.out.println("[�� ä�ù� ���� ��� ����Ʈ]");
		for (int i = 0; i < newChatSessionList.size(); i++) {
			SocketVO socket = newChatSessionList.get(i);
			System.out.println("ä�ù�" + socket.getChatroom_id() + " : " + socket.getMember_id());
		}
	}

	// �� ���� ������ �����͸� �������� ���
	@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			String msg = message.getPayload();
			JSONObject obj = jsonToObjectParser(msg);
			
			String message_sender = (String) obj.get("member_id");
			String message_receiver = (String) obj.get("message_receiver");
			String s_chatroom_id = (String) obj.get("chatroom_id");
			int chatroom_id = Integer.parseInt(s_chatroom_id);
			
			for (SocketVO sess : newChatSessionList) {
				if (sess.getMember_id().equals(message_sender))
					sess.setChatroom_id(chatroom_id);
			}
						
			//���� ä�ù濡 �ִ� ��������׸� �޽����� ����
			for (SocketVO sess : newChatSessionList) {
				if (chatroom_id == sess.getChatroom_id()) {
					sess.getSession().sendMessage(new TextMessage(obj.toJSONString()));
				}	
			}
			
			//���� ä�ù濡 �ִ� �������� ä���� �� �� �ֵ��� �޽����� ����
			for (SocketVO sess : ChatHandler.chatSessionList) {
				if (chatroom_id == sess.getChatroom_id()) {
					chatHandler.sendMessageToChatroom(chatroom_id, obj);
				}
			}
			
			System.out.println("[�� ä�ù� ���� ��� ����Ʈ]");
			for (int i=0; i<newChatSessionList.size(); i++) {
				SocketVO socket = newChatSessionList.get(i);
				System.out.println("ä�ù�"+ socket.getChatroom_id() + " : " + socket.getMember_id());
			}
			
			System.out.println("[�� ä�ù濡�� �����ϴ� ���� ����Ʈ]");
			for (SocketVO sess : ChatHandler.chatSessionList) {
				System.out.println("ä�ù�"+sess.getChatroom_id() + ":" + sess.getMember_id());
			}
			
			boolean isExisted = false;
			//�޽����� �޴� ����� ���� ä�ù濡 �������� �ƴ� ���� �˸��� ���� & �������̸� �޽����� ���� ���·� ����
			for (SocketVO sess : ChatHandler.chatSessionList) {
				if (message_receiver.equals(sess.getMember_id()) && chatroom_id == sess.getChatroom_id()) { //���� ��� -> ���� ����
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

	// Ŭ���̾�Ʈ�� ������ ������ ���
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// ���� ������ ��
		for (int i = 0; i < newChatSessionList.size(); i++) {
			if (newChatSessionList.get(i).getSession().getId() == session.getId()) {
				newChatSessionList.remove(i);
			}
		}

		System.out.println("[�� ä�ù� ���� ��� ����Ʈ]");
		for (int i = 0; i < newChatSessionList.size(); i++) {
			SocketVO socket = newChatSessionList.get(i);
			System.out.println("ä�ù�" + socket.getChatroom_id() + " : " + socket.getMember_id());
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
	
	public void sendMessageToNewChatroom(int chatroom_id, JSONObject obj) throws IOException {
		for (SocketVO sess : newChatSessionList) {
			if (chatroom_id == sess.getChatroom_id()) {
				sess.getSession().sendMessage(new TextMessage(obj.toJSONString()));
			}
		}
	}
}
