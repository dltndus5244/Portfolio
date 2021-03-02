package com.myspring.market.member.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component("memberVO")
public class MemberVO {
	private String member_id;
	private String member_pw;
	private String member_name;
	private String member_gender;
	private String tel1;
	private String tel2;
	private String tel3;
	private String email1;
	private String email2;
	private String member_birth_y;
	private String member_birth_m;
	private String member_birth_d;
	private Date joindate;
	
	public MemberVO() {
		
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_pw() {
		return member_pw;
	}
	public void setMember_pw(String member_pw) {
		this.member_pw = member_pw;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMember_gender() {
		return member_gender;
	}
	public void setMember_gender(String member_gender) {
		this.member_gender = member_gender;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getTel3() {
		return tel3;
	}
	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getMember_birth_y() {
		return member_birth_y;
	}
	public void setMember_birth_y(String member_birth_y) {
		this.member_birth_y = member_birth_y;
	}
	public String getMember_birth_m() {
		return member_birth_m;
	}
	public void setMember_birth_m(String member_birth_m) {
		this.member_birth_m = member_birth_m;
	}
	public String getMember_birth_d() {
		return member_birth_d;
	}
	public void setMember_birth_d(String member_birth_d) {
		this.member_birth_d = member_birth_d;
	}
	public Date getJoindate() {
		return joindate;
	}
	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}
	
	
}
