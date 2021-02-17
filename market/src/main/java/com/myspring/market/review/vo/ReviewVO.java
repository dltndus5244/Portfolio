package com.myspring.market.review.vo;

import org.springframework.stereotype.Component;

@Component("reviewVO")
public class ReviewVO {
	private int review_id;
	private String seller_id;
	private String buyer_id;
	private int goods_id;
	private String review_contents;
	private String review_star;
	private String goods_title;
	private String market_image;
	
	public ReviewVO() {
		
	}

	public int getReview_id() {
		return review_id;
	}

	public void setReview_id(int review_id) {
		this.review_id = review_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getReview_contents() {
		return review_contents;
	}

	public void setReview_contents(String review_contents) {
		this.review_contents = review_contents;
	}

	public String getReview_star() {
		return review_star;
	}

	public void setReview_star(String review_star) {
		this.review_star = review_star;
	}

	public String getGoods_title() {
		return goods_title;
	}

	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}

	public String getMarket_image() {
		return market_image;
	}

	public void setMarket_image(String market_image) {
		this.market_image = market_image;
	}
	
	
}
