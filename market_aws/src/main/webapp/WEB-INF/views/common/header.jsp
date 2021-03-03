<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${contextPath}/resources/css/Main.css">

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	category();
});

function category() {
	$("#category>a").click(function() {
		if($("#menu").is(":visible")){
               $("#menu").slideUp();
           }
           else{
               $("#menu").slideDown();
           }
	});
}

function fn_chat_list() {
	window.open("${contextPath}/chat/getChatroomList.do", "chatroomList", "width=410, height=600, left=500, top=50");
}

function listCategoryGoods(goods_sort) {
	var form = document.createElement("form");
	
	var i_goods_sort = document.createElement("input");
	i_goods_sort.type = "hidden";
	i_goods_sort.name = "goods_sort";
	i_goods_sort.value = goods_sort;
	
	document.body.appendChild(form);
	form.appendChild(i_goods_sort);
	
	form.method = "post"
	form.action = '${contextPath}/main/listCategoryGoods.do';
	form.submit();
}

function openForm(form) {
	 $('#'+form).toggleClass('open');
}

function closeForm(form) {
	$('#'+form).toggleClass('open');
}

function login() {
	var form = document.getElementById("goLogin");
	var member_id = document.getElementById("member_id").value;
	var member_pw = document.getElementById("member_pw").value;
	
	if (!member_id || !member_pw) {
		alert('아이디와 비밀번호를 입력해주세요');
	} else {
		form.submit();
	}
	
}

</script>
</head>
		<div id="logo">
			<a href="${contextPath}/main/main.do">
				<img width="60" height="50" style="margin-left:50px" src="${contextPath}/resources/image/market_logo.png">
			</a>
		</div>
		
		<div id="search">
			<form action="${contextPath}/goods/searchGoods.do" method="post">
				<input name="keyword" class="main_input" type="text"/>
				<input type="submit" class="btn1" value="검색" />
			</form>
		</div>
		
		<div id="head_link">
			<ul>
				<c:choose>
					<c:when test="${isLogOn == true and not empty memberInfo }">
						<li><a href="${contextPath}/goods/newGoodsForm.do">판매하기</a></li>
						<li><a href="${contextPath}/market/myMarketMain.do">마이페이지</a></li>
						<li><a href="javascript:void(0)" onclick="fn_chat_list()">채팅</a></li>
						<li><a href="${contextPath}/member/logout.do">로그아웃</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="javascript:void(0)" onclick="openForm('loginForm')">로그인</a></li>
						<li><a href="${contextPath}/member/memberForm.do">회원가입</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		
		<div class="clear"></div>

		<div id="category">
			<a><img src="${contextPath}/resources/image/menu.png" width="20" height="20"/></a>
			<ul id="menu">
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('woman')">여성의류</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('man')">남성의류</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('stuff')">패션잡화</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('digital')">디지털/가전</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('furniture')">가구/소품</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('house')">생활용품</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('beauty')">뷰티/미용</a></li>
				<li><a href="javascript:void(0)" onclick="listCategoryGoods('pet')">애완용품</a></li>
			</ul>
		</div>
		
		<!-- 로그인 모달창 -->
		<div class="modal-wrapper" id="loginForm">
		  <div class="modal">
		    <div class="modal-head">
		      <a id="close_btn" class="btn-close" href="javascript:void(0)" onclick="closeForm('loginForm')">
		      	<img width="15" height="15" src="${contextPath}/resources/image/close.png" />
		      </a>
		    </div>
		    <div class="content">
		        <div id="login">
					<form action="${contextPath}/member/login.do" method="post" id="goLogin">
						<table>
							<tbody>
								<tr class="dot_line">
									<td class="fixed_join">아이디</td>
									<td><input id="member_id" name="member_id" type="text" size="20" /></td>
								</tr>
								<tr class="solid_line">
									<td class="fixed_join">비밀번호</td>
									<td><input id="member_pw" name="member_pw" type="password" size="20" /></td>
								</tr>
							</tbody>
						</table>
						<input id="loginBtn" class="btn" type="submit" value="로그인" onclick="login()"> 
					</form>		
				</div>
		    </div>
		  </div>
		</div>
</body>
</html>