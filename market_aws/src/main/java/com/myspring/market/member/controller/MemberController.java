package com.myspring.market.member.controller;

import java.io.PrintWriter;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.market.member.service.MemberService;
import com.myspring.market.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;
	@Autowired
	private JavaMailSender mailSender;
	
	@RequestMapping(value="/*Form.do", method=RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	//멤버 추가
	@RequestMapping(value="/addMember.do", method=RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("memberVO") MemberVO memberVO,
						  HttpServletRequest request, HttpServletResponse response) throws Exception {
		memberService.addMember(memberVO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main/main.do");
		
		return mav;
	}
	
	//아이디 중복 검사
	@RequestMapping(value="/overlapped.do", method=RequestMethod.POST)
	@ResponseBody
	public String overlappedID(@RequestParam("id") String id,
							   HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = memberService.overlappedID(id);
		return result;
	}
	
	//로그인
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
	
	//로그아웃
	@RequestMapping(value="/logout.do", method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("isLogOn");
		session.removeAttribute("memberInfo");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main/main.do");
		
		return mav;
	}
	
	//회원 정보 수정
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
	
	//비밀번호 수정
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
	
	//인증 번호 메일 전송
	@RequestMapping(value="/sendAuthMail.do", method=RequestMethod.POST)
	public void sendAuthMail(@RequestParam("email") String email,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		Random r = new Random();
		String authCode = "";
		
		for (int i=0; i<15; i++) {
			int num = (int) 48 + (int) (r.nextDouble() * 74);
			authCode += (char) num;
		}
		
		String setFrom = "dltndus5244@gmail.com";
		String toMail = email;
		String title = "[USED MARKET] 회원가입 인증 이메일 입니다.";
		String content = "인증번호는 " + authCode + "입니다.";
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			messageHelper.setFrom(setFrom);
			messageHelper.setTo(toMail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("authCode", authCode);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkAuthCode.do", method=RequestMethod.POST)
	public String checkAuthCode(@RequestParam("authCode") String authCode,
							  HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String s_authCode = (String) session.getAttribute("authCode");
	
		if (s_authCode != null && s_authCode.equals(authCode)) {
			session.removeAttribute("authCode");
			return "success";
		}
		else
			return "false";
		
	}
}
