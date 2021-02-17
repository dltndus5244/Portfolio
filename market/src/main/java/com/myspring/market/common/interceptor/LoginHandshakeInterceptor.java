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

public class LoginHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		ServletServerHttpRequest ss_request = (ServletServerHttpRequest) request;
		HttpServletRequest req = ss_request.getServletRequest();
		HttpSession session = req.getSession();
		
		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberInfo.getMember_id();
		
		attributes.put("member_id", member_id);
		
		System.out.println("로그인 인터셉터 : " + member_id);
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}


	
}
