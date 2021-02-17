package com.myspring.market.chat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.market.chat.vo.ChatroomVO;
import com.myspring.market.chat.vo.MessageVO;

@Repository("chatDAO")
public class ChatDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public int selectNewChatroomNO() throws DataAccessException {
		int chatroom_id = sqlSession.selectOne("mapper.chat.selectNewChatroomNO");
		return chatroom_id + 1;
	}
	
	public void insertChatroom(ChatroomVO chatroomVO) throws DataAccessException {
		sqlSession.insert("mapper.chat.insertChatroom", chatroomVO);
	}
	
	public ChatroomVO overlapChatroom(ChatroomVO chatroomVO) throws DataAccessException {
		ChatroomVO chatroom = sqlSession.selectOne("mapper.chat.overlapChatroom", chatroomVO);
		return chatroom;
	}
	
	public List<String> selectChatroomList(String id) throws DataAccessException {
		List<String> chatroomIdList = sqlSession.selectList("mapper.chat.selectChatroomList", id);
		return chatroomIdList;
	}
	
	public int selectNewMessageNO() throws DataAccessException {
		int message_id = sqlSession.selectOne("mapper.chat.selectNewMessageNO");
		return message_id + 1;
	}
	
	public void insertMessage(MessageVO messageVO) throws DataAccessException {
		sqlSession.insert("mapper.chat.insertMessage", messageVO);
	}
	
	public List<MessageVO> selectMessageList(int chatroom_id) throws DataAccessException {
		List<MessageVO> messageList = sqlSession.selectList("mapper.chat.selectMessageList", chatroom_id);
		return messageList;
	}
	
	public ChatroomVO selectChatroom(int chatroom_id) throws DataAccessException {
		ChatroomVO chatroom = sqlSession.selectOne("mapper.chat.selectChatroom", chatroom_id);
		return chatroom;
	}
	
	public List<MessageVO> selectNoReadMessage(MessageVO messageVO) throws DataAccessException {
		List<MessageVO> noreadMessageList = sqlSession.selectList("mapper.chat.selectNoReadMessage", messageVO);
		return noreadMessageList;
	}
	
	public void updateMessageRead(MessageVO messageVO) throws DataAccessException {
		sqlSession.update("mapper.chat.updateMessageRead", messageVO);
	}
	
	public List<String> selectChatMembers(Map<String, Object> chatMap) throws DataAccessException {
		List<String> chatMemberList = sqlSession.selectList("mapper.chat.selectChatMemberList", chatMap);
		return chatMemberList;
	}
	
	public ChatroomVO existedChatroom(ChatroomVO chatroomVO) throws DataAccessException {
		ChatroomVO chatroom = sqlSession.selectOne("mapper.chat.existedChatroom", chatroomVO);
		return chatroom;
	}
}
