package com.myspring.market.heart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.myspring.market.heart.service.HeartService;
import com.myspring.market.heart.vo.HeartVO;
import com.myspring.market.member.vo.MemberVO;

@Controller("heartController")
@RequestMapping("/heart")
public class HeartController {
	@Autowired
	private HeartService heartService;
	@Autowired
	private HeartVO heartVO;
	
	//회원 찜 목록에 상품 추가
	@RequestMapping(value="/addMemberHeart.do", method=RequestMethod.POST)
	public void addMemberHeart(@RequestParam("goods_id") int goods_id,
							   HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		
		heartVO.setMember_id(member_id);
		heartVO.setGoods_id(goods_id);
		
		heartService.addMemberHeart(heartVO);
	}
	
	//회원 찜 목록에 상품 제거
	@RequestMapping(value="/removeMemberHeart.do", method=RequestMethod.POST)
	public void removeMemberHeart(@RequestParam("goods_id") int goods_id,
							   HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		
		heartVO.setMember_id(member_id);
		heartVO.setGoods_id(goods_id);
		
		heartService.removeMemberHeart(heartVO);
	}
	
	//회원 찜 목록 가져오기
	@RequestMapping(value="/getMemberHeartList.do", method=RequestMethod.POST)
	public void getMemberHeartList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		
		List<HeartVO> memberHeartList = heartService.getMemberHeartList(member_id);
		session.setAttribute("memberHeartList", memberHeartList);
	}
} 
