<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<nav>
		<ul>
			<li><a href="${contextPath}/market/myMarketMain.do">나의 상품목록</a></li>
			<li><a href="#">나의 상점 관리</a></li>
			<li><a href="#">나의 찜 목록</a></li>
			<li><a href="#">상점후기</a></li>
			<li><a href="#">회원정보 수정</a></li>
		</ul>
	</nav>
</body>
</html>