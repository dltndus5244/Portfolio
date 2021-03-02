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
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
function select_buyer_id(buyer_id) {
	var seller_id = "${memberInfo.member_id}";
	var goods_id = "${goods_id}";
	
	$.ajax({
		type : "post",
		async : false, 
		url : "${contextPath}/review/selectBuyerId.do",
		data : {
			buyer_id : buyer_id,
			seller_id : seller_id,
			goods_id : goods_id,
		},
		success : function(data, textStatus) {
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		}
	}); 
	
	window.close();
}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty chatMemberList}">
			<p>구매자를 찾을 수 없습니다!</p><br>
			<input type="button" value="거래확정하기" />
			<input type="button" value="취소하기" />
		</c:when>
		<c:otherwise>
			<p>거래가 완료되었습니다.</p>
			<p>구매자를 선택해주세요.</p>
			<c:forEach var="member_id" items="${chatMemberList}">
				<a href="javascript:select_buyer_id('${member_id}')">${member_id}</a>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>