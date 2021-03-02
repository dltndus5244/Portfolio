package com.myspring.market.chat.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.myspring.market.chat.vo.SocketVO;


public class NoticeHandler extends TextWebSocketHandler {
		//웹 소켓 세션을 저장할 리스트
		public static List<SocketVO> loginSessionList = new ArrayList<SocketVO>();
		String session_id = null;

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			SocketVO socketVO = new SocketVO();
			
			Map<String, Object> map = session.getAttributes();
			String member_id = (String) map.get("member_id");
			
			socketVO.setSession(session);
			socketVO.setMember_id(member_id);
			
			loginSessionList.add(socketVO);
			System.out.println(member_id + "님이 로그인하셨습니다.");
		}
		
		//웹 소켓 서버로 데이터를 전송했을 경우
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			
		}

		//클라이언트와 연결이 끊어진 경우
		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			for (int i=0; i<loginSessionList.size(); i++) {
				if (loginSessionList.get(i).getSession().getId() == session.getId()) {
					loginSessionList.remove(i);
				}
			}
		}
		
		public void notice(String message_sender, String message_receiver) throws IOException {
			for (SocketVO socketVO : loginSessionList) {
				if (socketVO.getMember_id().equals(message_receiver)) {
					String msg = message_sender + "님으로부터 메시지가 도착했습니다.";
					socketVO.getSession().sendMessage(new TextMessage(msg));
				}
			}
		}
}
