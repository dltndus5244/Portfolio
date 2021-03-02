package com.myspring.market.market.vo;

import org.springframework.stereotype.Component;

@Component("marketVO")
public class MarketVO {
	private String member_id;
	private String market_name;
	private String market_image;
	private String market_intro;
	private int heart_num; //내가 찜한 상품 수, 둘 다 필요없을듯? 쿼리날리면되서
	private int market_review_num; //내 마켓에 달린 리뷰 수
	
	public MarketVO() {
		
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getMarket_name() {
		return market_name;
	}

	public void setMarket_name(String market_name) {
		this.market_name = market_name;
	}

	public String getMarket_image() {
		return market_image;
	}

	public void setMarket_image(String market_image) {
		this.market_image = market_image;
	}

	public String getMarket_intro() {
		return market_intro;
	}

	public void setMarket_intro(String market_intro) {
		this.market_intro = market_intro;
	}

	public int getHeart_num() {
		return heart_num;
	}

	public void setHeart_num(int heart_num) {
		this.heart_num = heart_num;
	}

	public int getMarket_review_num() {
		return market_review_num;
	}

	public void setMarket_review_num(int market_review_num) {
		this.market_review_num = market_review_num;
	}
	
	
}
