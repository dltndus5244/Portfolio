<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
body {
	margin-top : 100px;
}
</style>
<link rel="preconnect" href="https://fonts.gstatic.com">
<link rel="stylesheet" href="${contextPath}/resources/css/Main.css">
<link rel="stylesheet" type="text/css" href="http://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" />
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var session_id = "${memberInfo.member_id}";

$(document).ready(function() {
	//로그인 한 경우 LoginHandshakeInterceptor로 로그인 한 회원의 아이디를 보냄
	if (session_id) {
		var sock = new SockJS('http://3.130.107.103:8080/market_aws/notice');
		sock.onmessage = onMessage;
		sock.onopen = function(e) {
			sock.send(session_id);
		}
	}
});

//메시지 알림창 띄우기
function onMessage(evt) {
	var data = evt.data;
	toastr.info(data, {timeOut: 3000});
}

</script>
</head>
<body>
	<div id="outer_wrap">
		<div id="wrap">
			<header>
				   <tiles:insertAttribute name="header" />
			</header>
			<div class="clear"></div>
			<article>
			 	<tiles:insertAttribute name="body" />
			</article>
			<div class="clear"></div>
			<footer>
        		<tiles:insertAttribute name="footer" />
        	</footer>
		</div>
    </div>        	
</body>
</html>