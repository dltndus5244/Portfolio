package com.myspring.market.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.myspring.market.member.dao.MemberDAO;
import com.myspring.market.member.vo.MemberVO;

@Service("memberService")
public class MemberService {
	@Autowired
	private MemberDAO memberDAO;
	
	public void addMember(MemberVO memberVO) throws DataAccessException {
		memberDAO.insertMember(memberVO);
		
		String member_id = memberVO.getMember_id();
		memberDAO.insertMarket(member_id);
	}
	
	public String overlappedID(String id) throws DataAccessException {
		String result = memberDAO.selectOverlappedID(id);
		return result;
	}
	
	public MemberVO login(MemberVO memberVO) throws DataAccessException {
		MemberVO memberInfo = memberDAO.login(memberVO);
		return memberInfo;
	}
	
	public void modifyMyInfo(MemberVO memberVO) throws DataAccessException {
		memberDAO.updateMyInfo(memberVO);
	}
	
	public void modifyMyPw(MemberVO memberVO) throws DataAccessException {
		memberDAO.updateMyPw(memberVO);
	}

}
