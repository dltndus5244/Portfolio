<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<c:set var="goods" value="${goodsMap.goods}" />
<c:set var="imageFileList" value="${goodsMap.imageFileList}" />
<c:set var="marketInfo" value="${goodsMap.marketInfo}" />
<c:set var="marketGoodsList" value="${goodsMap.marketGoodsList}" />
<c:set var="goodsNum" value="${fn:length(marketGoodsList)}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    $('.bxslider').bxSlider( {
	        mode: 'horizontal',
	        pager: true, 
	        moveSlides: 1,
	        slideWidth: 400,
	        auto: false, 
	        controls: true,
	        onSliderLoad: function(){ 
	        	$("#goods_image").css("visibility", "visible").animate({opacity:1}); 
	        }
	    });
	});

	function fn_heart_up(goods_id) {
		var member_id = "${memberInfo.member_id}";
		
		if (member_id == "") {
			alert("로그인이 필요한 서비스입니다.");
			return;
		}

		var heart_up_btn = document.getElementById("heart_up_btn");
		var heart_down_btn = document.getElementById("heart_down_btn");
		
		var heart_num;
		
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/goods/upGoodsHeartNum.do",
			data : { 
				goods_id : goods_id 
			},
			dataType : "text",
			success : function(data, textStatus) {
					heart_num = data;	
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus) {
				//alert("작업을완료 했습니다");
				
			}
		}); //end ajax
		
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/heart/addMemberHeart.do",
			data : { 
				goods_id : goods_id 
			},
			success : function(data, textStatus) {
				
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus) {
				//alert("작업을완료 했습니다");
				
			}
		}); //end ajax
		
		heart_up_btn.style.display = "none";
		heart_down_btn.style.display = "inline";
		heart_down_btn.value = "❤ 찜 " + heart_num;
		
	}
	
	function fn_heart_down(goods_id) {
		var heart_up_btn = document.getElementById("heart_up_btn");
		var heart_down_btn = document.getElementById("heart_down_btn");
		
		var heart_num;
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/goods/downGoodsHeartNum.do",
			data : { 
				goods_id : goods_id 
			},
			dataType : "text",
			success : function(data, textStatus) {
					heart_num = data;	
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus) {
				//alert("작업을완료 했습니다");
				
			}
		}); //end ajax
		
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/heart/removeMemberHeart.do",
			data : { 
				goods_id : goods_id 
			},
			success : function(data, textStatus) {
			
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus) {
				//alert("작업을완료 했습니다");
				
			}
		}); //end ajax
		
		heart_up_btn.style.display = "inline"; 
		heart_up_btn.value = "❤ 찜 " + heart_num;
		heart_down_btn.style.display = "none";
	}
	
	function fn_chat_form() {
		var member_id = "${memberInfo.member_id}";
		if (member_id == "") {
			alert("로그인이 필요한 서비스입니다.");
			return;
		}
		
		var goods_id = "${goods.goods_id}";
		window.open("${contextPath}/chat/newChatroomForm.do?goods_id="+goods_id, "newChatroom",  "width=410, height=600, left=300, top=50");

	}
	
	function detailGoods(goods_id) {
		var form = document.createElement("form");
		
		var i_goods_id = document.createElement("input");
		i_goods_id.type = "hidden";
		i_goods_id.name = "goods_id";
		i_goods_id.value = goods_id;
		
		document.body.appendChild(form);
		form.appendChild(i_goods_id);
		
		form.method = "post"
		form.action = '${contextPath}/goods/goodsDetail.do';
		form.submit();
	}
	
	function marketMain(member_id) {
		var form = document.createElement("form");
		
		var i_member_id = document.createElement("input");
		i_member_id.type = "hidden";
		i_member_id.name = "member_id";
		i_member_id.value = member_id;
		
		document.body.appendChild(form);
		form.appendChild(i_member_id);
		
		form.method = "post"
		
		var marketId = "${marketInfo.member_id}";
		var sessionId = "${memberInfo.member_id}";
		
		if (marketId != sessionId) {
			form.action = '${contextPath}/market/marketMain.do';
		} else {
			form.action = '${contextPath}/market/myMarketMain.do';
		}
				
		form.submit();
	}
</script>
</head>
<body>
	<div id="goods_detail">
		<div id="goods_image">
			<ul class="bxslider">
				<c:forEach var="i" begin="0" end="${fn:length(imageFileList)-1}">
					<li>
						<img width="400" height="400" 
							src="${contextPath}/file/downloadGoodsImage_s3.do?goods_id=${goods.goods_id}&fileName=${imageFileList[i].filename}">
					</li>
				</c:forEach>
			</ul>
		</div>
		<div id="goods_remain">
			<h2 style="font-size:24px;">${goods.goods_title}</h2><br>
			<h2 style="font-size:24px"><fmt:formatNumber value="${goods.goods_price}" type="number" />원</h2><br><br>
			<h4 style="font-size:15px">거래지역&nbsp;&nbsp;&nbsp;&nbsp;${goods.goods_location1}&nbsp;${goods.goods_location2}</h4><br>
			<c:choose>
					<c:when test="${goods.goods_status == '거래완료'}">
						<h4 id="goods_status" style="color:#b4b4b4">${goods.goods_status}</h4>
					</c:when>
					<c:otherwise>
						<h4 id="goods_status" style="color:#8AE634">${goods.goods_status}</h4>
					</c:otherwise>
				</c:choose>
			<c:choose>
				<c:when test="${marketInfo.member_id != memberInfo.member_id && goods.goods_status != '거래완료'}">
					<div id="goods_btn">
						<c:forEach var="heart" items="${memberHeartList}">
							<c:if test="${heart.goods_id == goods.goods_id }">
								<c:set var="heartListIn" value="있음" />
							</c:if>
						</c:forEach>
						<c:choose>
							<c:when test="${heartListIn == '있음' }">
								<input id="heart_up_btn" type="button" value="❤ 찜 ${goods.heart_num}" onclick="fn_heart_up(${goods.goods_id})"  style="display:none"/>
								<input id="heart_down_btn" type="button" value="❤  찜 ${goods.heart_num}" onclick="fn_heart_down(${goods.goods_id})"/>
							</c:when>
							<c:otherwise>
								<input id="heart_up_btn" type="button" value="❤ 찜 ${goods.heart_num}" onclick="fn_heart_up(${goods.goods_id})"/>
								<input id="heart_down_btn" type="button" value="❤  찜 ${goods.heart_num}" onclick="fn_heart_down(${goods.goods_id})" style="display:none"/>
							</c:otherwise>
						</c:choose>
						<input id="chat_btn" type="button" value="채팅하기" onclick="fn_chat_form()"/>
					</div>
				</c:when>
			</c:choose>
		</div>
		<div class="clear"></div>
		
		<div id="goods_contents">
			<h3>상품 정보</h3><br>
			${goods.goods_contents}
		</div>
		
		<div id="member_market">
			<h3>마켓 정보</h3>
			<a href="javascript:void(0)" onclick="marketMain('${marketInfo.member_id}')">
			<c:choose>
				<c:when test="${not empty marketInfo.market_image}">
					<img style="width:40px; height:40px;" class="profile" src="${contextPath}/file/thumbMarketImage_s3.do?member_id=${marketInfo.member_id}&market_image=${marketInfo.market_image}">
				</c:when>
				<c:otherwise>
					<img style="width:40px; height:40px;" class="profile" src="${contextPath}/resources/image/shop.png" />
				</c:otherwise>
			</c:choose>
			</a>
			${marketInfo.market_name}<br>
			<span>상품</span> <span style="color:red">${goodsNum}</span>
			<br><br>
			<c:choose>
				<c:when test="${goodsNum <= 2}">
					<c:forEach var="item" items="${marketGoodsList}">
						<a href="javascript:void(0)" onclick="detailGoods(${item.goods_id})">
							<img src="${contextPath}/file/thumbGoodsImage_s3.do?goods_id=${item.goods_id}&fileName=${item.filename}">
						</a>
					</c:forEach>
				</c:when>
				<c:when test="${goodsNum > 2}">
					<c:forEach var="i" begin="0" end="1">
						<a href="javascript:void(0)" onclick="detailGoods(${marketGoodsList[i].goods_id})">
							<img src="${contextPath}/file/thumbGoodsImage_s3.do?goods_id=${marketGoodsList[i].goods_id}&fileName=${marketGoodsList[i].filename}">
						</a>
					</c:forEach>
					<br><br>
					<a id="more" href="javascript:void(0)" onclick="marketMain('${marketInfo.member_id}')">${goodsNum-2}개 상품 더보기 〉</a>
				</c:when>
			</c:choose>
		</div>	
	</div>
</body>
</html>