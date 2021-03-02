package com.myspring.market.heart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.myspring.market.heart.dao.HeartDAO;
import com.myspring.market.heart.vo.HeartVO;

@Service("heartService")
public class HeartService {
	@Autowired
	private HeartDAO heartDAO;
	
	public void addMemberHeart(HeartVO heartVO) throws DataAccessException {
		int heart_id = heartDAO.selectNewMemberHeartNO();
		heartVO.setHeart_id(heart_id);
		heartDAO.insertMemberHeart(heartVO);
	}
	
	public void removeMemberHeart(HeartVO heartVO) throws DataAccessException {
		heartDAO.deleteMemberHeart(heartVO);
	}
	
	public List<HeartVO> getMemberHeartList(String member_id) throws DataAccessException {
		List<HeartVO> memberHeartList = heartDAO.selectMemberHeartList(member_id);
		return memberHeartList;
	}
}
