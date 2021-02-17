package com.myspring.market.chat.vo;

import org.springframework.stereotype.Component;

@Component("chatroomVO")
public class ChatroomVO {
	private int chatroom_id;
	private int goods_id;
	private String buyer_id;
	private String seller_id;
	private String buyer_market_image;
	private String seller_market_image;
	
	public ChatroomVO() {
		
	}

	public int getChatroom_id() {
		return chatroom_id;
	}

	public void setChatroom_id(int chatroom_id) {
		this.chatroom_id = chatroom_id;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getBuyer_market_image() {
		return buyer_market_image;
	}

	public void setBuyer_market_image(String buyer_market_image) {
		this.buyer_market_image = buyer_market_image;
	}

	public String getSeller_market_image() {
		return seller_market_image;
	}

	public void setSeller_market_image(String seller_market_image) {
		this.seller_market_image = seller_market_image;
	}
}
