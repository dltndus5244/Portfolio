package com.myspring.market.chat.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component("messageVO")
public class MessageVO {
	private int message_id;
	private int chatroom_id;
	private String message_contents;
	private String message_sender;
	private String message_receiver;
	private Date message_senderTime;
	private String message_read;
	
	public MessageVO() {
		
	}

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public int getChatroom_id() {
		return chatroom_id;
	}

	public void setChatroom_id(int chatroom_id) {
		this.chatroom_id = chatroom_id;
	}

	public String getMessage_sender() {
		return message_sender;
	}

	public void setMessage_sender(String message_sender) {
		this.message_sender = message_sender;
	}

	public String getMessage_receiver() {
		return message_receiver;
	}

	public void setMessage_receiver(String message_receiver) {
		this.message_receiver = message_receiver;
	}

	public Date getMessage_senderTime() {
		return message_senderTime;
	}

	public void setMessage_senderTime(Date message_senderTime) {
		this.message_senderTime = message_senderTime;
	}
	
	public String getMessage_read() {
		return message_read;
	}

	public void setMessage_read(String message_read) {
		this.message_read = message_read;
	}

	public String getMessage_contents() {
		return message_contents;
	}

	public void setMessage_contents(String message_contents) {
		this.message_contents = message_contents;
	}
}
