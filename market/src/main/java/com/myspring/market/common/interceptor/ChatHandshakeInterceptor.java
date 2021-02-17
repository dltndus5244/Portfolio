package com.myspring.market.common.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.myspring.market.member.vo.MemberVO;

public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		ServletServerHttpRequest ss_request = (ServletServerHttpRequest) request;
		HttpServletRequest req = ss_request.getServletRequest();
		HttpSession session = req.getSession();
		
		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberInfo.getMember_id();
		
		int chatroom_id = (int) session.getAttribute("chatroom_id");
		
		attributes.put("member_id", member_id);
		attributes.put("chatroom_id", chatroom_id);
		
		System.out.println("기존 채팅 인터셉터 : " + member_id + "/" + chatroom_id);
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}


	
}
