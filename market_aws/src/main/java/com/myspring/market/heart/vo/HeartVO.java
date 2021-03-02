package com.myspring.market.heart.vo;

import org.springframework.stereotype.Component;

@Component("heartVO")
public class HeartVO {
	private int heart_id;
	private String member_id;
	private int goods_id;
	private String goods_title;
	private String goods_price;
	private String filename;
	
	HeartVO() {
		
	}

	public int getHeart_id() {
		return heart_id;
	}

	public void setHeart_id(int heart_id) {
		this.heart_id = heart_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getGoods_title() {
		return goods_title;
	}

	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
}
