package com.myspring.market.chat.vo;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component("socketVO")
public class SocketVO {
	private WebSocketSession session;
	private String member_id;
	private int chatroom_id;
	
	public SocketVO() {
		
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getChatroom_id() {
		return chatroom_id;
	}

	public void setChatroom_id(int chatroom_id) {
		this.chatroom_id = chatroom_id;
	}
	
}
