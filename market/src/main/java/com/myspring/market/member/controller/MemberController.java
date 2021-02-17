package com.myspring.market.member.controller;

import java.io.PrintWriter;

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

import com.myspring.market.heart.service.HeartService;
import com.myspring.market.member.service.MemberService;
import com.myspring.market.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;
	
	@RequestMapping(value="/*Form.do", method=RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@RequestMapping(value="/addMember.do", method=RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("memberVO") MemberVO memberVO,
						  HttpServletRequest request, HttpServletResponse response) throws Exception {
		memberService.addMember(memberVO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main/main.do");
		
		return mav;
	}
	
	@RequestMapping(value="/overlapped.do", method=RequestMethod.POST)
	@ResponseBody
	public String overlappedID(@RequestParam("id") String id,
							   HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = memberService.overlappedID(id);
		return result;
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public void login(@ModelAttribute("memberVO") MemberVO memberVO,
							   HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=utf-8");
		
		MemberVO memberInfo = memberService.login(memberVO);
		
		if (memberInfo != null) {
			HttpSession session = request.getSession();
			session.setAttribute("isLogOn", true);
			session.setAttribute("memberInfo", memberInfo);
			
			String message = "<script>";
			message +=" location.href='"+request.getContextPath()+"/main/main.do';";
			message +=("</script>");
			
			out.println(message);
			out.flush();
		} else {			
			String message= "<script>";
			message += " alert('아이디 또는 비밀번호를 잘못 입력하셨습니다.');";
			message +=" location.href='"+request.getContextPath()+"/main/main.do';";
			message += "</script>";
			
			out.println(message);
			out.flush();
		}
	}
	
	@RequestMapping(value="/logout.do", method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("isLogOn");
		session.removeAttribute("memberInfo");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main/main.do");
		
		return mav;
	}
	
	@RequestMapping(value="/modifyMyInfo.do", method=RequestMethod.POST)
	@ResponseBody
	public String modifyMyInfo(@ModelAttribute("memberVO") MemberVO memberVO,
							   HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO s_memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = s_memberVO.getMember_id();
		String member_name = s_memberVO.getMember_name();
		
		memberVO.setMember_id(member_id);
		memberVO.setMember_name(member_name);

		
		memberService.modifyMyInfo(memberVO);
		session.removeAttribute("memberInfo");
		session.setAttribute("memberInfo", memberVO);
		
		return "mod_success";
	}
	
	@RequestMapping(value="/modifyMyPw.do", method=RequestMethod.POST)
	@ResponseBody
	public String modifyMyPw(@RequestParam("member_pw") String member_pw,
							 HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO s_memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = s_memberVO.getMember_id();
		
		memberVO.setMember_id(member_id);
		memberVO.setMember_pw(member_pw);
		
		memberService.modifyMyPw(memberVO);
		
		return "mod_success";	
	}
	
}
