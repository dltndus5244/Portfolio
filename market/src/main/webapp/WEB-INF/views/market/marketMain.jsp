<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<c:set var="marketInfo" value="${marketMap.marketInfo}" />
<c:set var="myGoodsList" value="${marketMap.myGoodsList}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마켓</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
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
	form.action = '${contextPath}/market/marketMain.do';
	form.submit();
}
</script>
</head>
<body>
	<div class="market_main">
		<div class="d_market_image">
			<c:choose>
				<c:when test="${not empty marketInfo.market_image}">
					<img class="profile" src="${contextPath}/downloadMarketImage.do?member_id=${marketInfo.member_id}&market_image=${marketInfo.market_image}">
				</c:when>
				<c:otherwise>
					<img class="profile" src="${contextPath}/resources/image/shop.png" />
				</c:otherwise>
			</c:choose>
		</div>
		<div class="d_market_name">
			<input type="text" class="market_name" style="font-weight:bold" value="${marketInfo.market_name}" disabled/>
		</div>
	</div>
	<div class="clear"></div>
	
	<div class="tabs_menu">
	    <input id="tab1" type="radio" name="tabs" class="tabs" checked>
	    <label for="tab1">상품 ${fn:length(myGoodsList)}</label>
	
	    <input id="tab2" type="radio" name="tabs" class="tabs">
	    <label for="tab2">마켓 후기  ${fn:length(reviewList)}</label>

	    <section id="content1" class="tabs_content">
	        <h3><span>상품</span> <span style="color:red">${fn:length(myGoodsList)}</span></h3>
	        <br>
	        <c:forEach var="goods" items="${myGoodsList}">
	        	<div class="goods" style="margin-right:20px">
		        	<a href="javascript:void(0)" onclick="detailGoods(${goods.goods_id})">
			        	<img width="180" height="180"
			        		src="${contextPath}/download.do?goods_id=${goods.goods_id}&fileName=${goods.filename}">
			        </a>
			        <div class="title">${goods.goods_title}</div>
					<div class="price">
						<fmt:formatNumber value="${goods.goods_price}" type="number" var="goods_price" />${goods_price}원
					</div>
		        </div>
	        </c:forEach>
	    </section>
	
	    <section id="content2" class="tabs_content">
	        <h3><span>마켓 후기</span> <span style="color:red">${fn:length(reviewList)}</span></h3>
	        <br>
	        <c:forEach var="review" items="${reviewList}">
	        	<div id="review">
	        		<div id="review_image">
		        		<a href="javascript:void(0)" onclick="marketMain('${review.buyer_id}')">
		        			<c:choose>
		        				<c:when test="${empty review.market_image}">
		        					<img style="width:40px; height:40px;" class="profile" src="${contextPath}/resources/image/shop.png" />
		        				</c:when>
		        				<c:otherwise>
		        					<img style="width:40px; height:40px;" class="profile" 
		        						src="${contextPath}/downloadMarketImage.do?member_id=${review.buyer_id}&market_image=${review.market_image}">
		        				</c:otherwise>
		        			</c:choose>
			        	</a>
		        	</div>
		        	<h5>${review.buyer_id}</h5>
		        	<c:choose>
		        		<c:when test="${review.review_star == 1}">
							    <span style="color:#FFE400">★</span>
							    <span style="color:#aaaaaa">★</span>
							    <span style="color:#aaaaaa">★</span>
							    <span style="color:#aaaaaa">★</span>
							    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 2}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#aaaaaa">★</span>
						    <span style="color:#aaaaaa">★</span>
						    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 3}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#aaaaaa">★</span>
						    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 4}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 5}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
		        		</c:when>
		        	</c:choose>
		        	<br><br>
		        	<div id="review_contents">
			        	<a href="javascript:void(0)" onclick="detailGoods(${review.goods_id})"><p>${review.goods_title} 〉</p></a>
			        	<h5 style="font-size:12px;">${review.review_contents}</h5>
		        	</div>
		        </div>
	        </c:forEach>
	    </section>
	</div>
</body>
</html>