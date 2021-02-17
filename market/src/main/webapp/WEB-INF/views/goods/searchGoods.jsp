<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#sort {
	float : right;
	font-size : 13px;
	margin-top : 90px;
}
#result {
	margin-left : 90px;
}
#searchItemList {
	margin-top : 30px;
	margin-left : 90px;
}
.goods {
	margin-right : 30px;
}
</style>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
function sort(sort, keyword) {
	var form = document.createElement("form");
	
	var i_sort = document.createElement("input");
	i_sort.type = "hidden";
	i_sort.name = "sort";
	i_sort.value = sort;
	
	var i_keyword = document.createElement("input");
	i_keyword.type = "hidden";
	i_keyword.name = "keyword";
	i_keyword.value = keyword;
	
	document.body.appendChild(form);
	form.appendChild(i_sort);
	form.appendChild(i_keyword);
	
	form.method = "post"
	form.action = '${contextPath}/goods/searchGoods.do';
	form.submit();
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
</script>
</head>
<body>
	<div id="sort">
		<a href="javascript:void(0)" onclick="sort('new', '${keyword}')">최신순</a> |
		<a href="javascript:void(0)" onclick="sort('low', '${keyword}')">저가순</a> |
		<a href="javascript:void(0)" onclick="sort('high', '${keyword}')">고가순</a>
	</div>
	<div class="clear"></div>
	
	<div id="result">
		<span style="color:red">${keyword}</span><span>의 검색결과</span> <span style="color:#BDBDBD">${fn:length(searchGoodsList)}개</span>
	</div>
	<div id="searchItemList">
		<c:choose>
			<c:when test="${empty searchGoodsList}">
				<h3>상품이 없습니다.</h3>
			</c:when>
			<c:otherwise>
				<c:forEach var="goods" items="${searchGoodsList}">
						<div class="goods">
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
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>