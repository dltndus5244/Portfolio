package com.myspring.market.goods.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component("goodsVO")
public class GoodsVO {
	private int goods_id;
	private String member_id;
	private String goods_sort;
	private String goods_title;
	private String goods_price;
	private String goods_contents;
	private String goods_location1;
	private String goods_location2;
	private int heart_num; //찜 개수!(다른 사람이 이 상품을 찜한 개수)
	private Date goods_credate;
	private String filename; //메인 화면에서 받아야해서 추가함
	private String goods_status;
	private int rnum;
	
	public GoodsVO() {
		
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getGoods_sort() {
		return goods_sort;
	}

	public void setGoods_sort(String goods_sort) {
		this.goods_sort = goods_sort;
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

	public String getGoods_contents() {
		return goods_contents;
	}

	public void setGoods_contents(String goods_contents) {
		this.goods_contents = goods_contents;
	}

	public String getGoods_location1() {
		return goods_location1;
	}

	public void setGoods_location1(String goods_location1) {
		this.goods_location1 = goods_location1;
	}

	public String getGoods_location2() {
		return goods_location2;
	}

	public void setGoods_location2(String goods_location2) {
		this.goods_location2 = goods_location2;
	}

	public int getHeart_num() {
		return heart_num;
	}

	public void setHeart_num(int heart_num) {
		this.heart_num = heart_num;
	}

	public Date getGoods_credate() {
		return goods_credate;
	}

	public void setGoods_credate(Date goods_credate) {
		this.goods_credate = goods_credate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getGoods_status() {
		return goods_status;
	}

	public void setGoods_status(String goods_status) {
		this.goods_status = goods_status;
	}
	
}
