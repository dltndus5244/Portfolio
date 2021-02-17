<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
h3 {
	margin-left : 50px;
	margin-bottom : 20px;
}
</style>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
var msg = "${msg}";
if (msg)
	alert(msg);

var start = 1;
$(document).scroll(function() {
	var maxHeight = $(document).height();
	var currentScroll = $(window).scrollTop() + $(window).height();
	
	var goodsList = null;
	if (maxHeight <= currentScroll + 50) {
		start = start + 12;
		$.ajax({
			type:"post",
			async:false,
			url:"${contextPath}/main/scroll.do",
			data: {
				start : start
			},
			success:function(data, textStatus) {
				goodsList = data;
			},
			error:function(data, textStatus) {
				alert("에러가 발생했습니다." + data);
			},
		});
		
		for (var i=0; i<goodsList.length; i++) {
			var goods = goodsList[i];
			var goods_price = goods.goods_price.replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
			var goods_id = goods.goods_id;

			var html = "<div class='goods' id='main_goods'>";
			html += "<a href='javascript:void(0)' onclick='detailGoods("+goods_id+")'>";
			html += "<img width='180' height='180' src='${contextPath}/download.do?goods_id="+goods.goods_id+"&fileName="+goods.filename+"'>";
			html += "</a>";
			html += "<div class='title'>"+goods.goods_title+"</div>";
			html += "<div class='price'>"+goods_price+"원</div>";
			html += "</div>";
			
			$("#itemList").append(html);
		}
	}
});

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
</script>
</head>
<body>
	<div id="itemList">
		<h3>전체 상품</h3>
		<c:forEach var="goods" items="${goodsList}">
			<div class="goods" id="main_goods">
					<a href="javascript:void(0)" onclick="detailGoods(${goods.goods_id})">
						<img width="180" height="180" src="${contextPath}/download.do?
							goods_id=${goods.goods_id}&fileName=${goods.filename}" >
					</a>
					<div class="title">${goods.goods_title}</div>
					<div class="price">
						<fmt:formatNumber value="${goods.goods_price}" type="number" var="goods_price" />${goods_price}원
					</div>
			</div>
		</c:forEach>
	</div>
</body>
</html>