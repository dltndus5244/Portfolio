<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
body {
	overflow-x:hidden; 
	overflow-y:auto;
}
</style>
<title>채팅</title>
<link rel="stylesheet" href="${contextPath}/resources/css/Main.css">
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">

//chatroom_id에 해당하는 채팅방으로 이동
function fn_chat_form(chatroom_id) {
	var form = document.createElement("form");
	
	var i_chatroom_id = document.createElement("input");
	i_chatroom_id.type = "hidden";
	i_chatroom_id.name = "chatroom_id";
	i_chatroom_id.value = chatroom_id;
	
	document.body.appendChild(form);
	form.appendChild(i_chatroom_id);
	
	form.method = "post"
	form.action = '${contextPath}/chat/chatroomForm.do';
	form.submit();
}
</script>
</head>
<body>
	<div id="chatroomList">
		<div id="chatMainHead">
			<img width="30" height="30" src="${contextPath}/resources/image/chat.png" />
		</div>
		<c:choose>
			<c:when test="${chatroomList == null}">
				<h4>채팅방이 없습니다.</h4>
			</c:when>
			<c:otherwise>
				<table>
					<c:forEach var="chatroom" items="${chatroomList}" varStatus="status">
						<tr onclick="fn_chat_form(${chatroom.chatroom_id})" onmouseover="this.style.background='#F8F8F8'" onmouseout="this.style.background='white'">
							<td style="width:50px;">
								<c:choose>
									<c:when test="${memberInfo.member_id == chatroom.buyer_id}">
										<c:choose>
											<c:when test="${empty chatroom.seller_market_image}">
												<img style="width:40px; height:40px;" class="profile" src="${contextPath}/resources/image/shop.png" />
											</c:when>
											<c:otherwise>
												<img class="profile" style="width:40px; height:40px;"
													src="${contextPath}/file/thumbMarketImage_s3.do?member_id=${chatroom.seller_id}&market_image=${chatroom.seller_market_image}">
											</c:otherwise>
										</c:choose>
										
									</c:when>
									<c:when test="${memberInfo.member_id == chatroom.seller_id}">
										<c:choose>
											<c:when test="${empty chatroom.buyer_market_image}">
												<img class="profile" style="width:40px; height:40px;" src="${contextPath}/resources/image/shop.png" />
											</c:when>
											<c:otherwise>
												<img class="profile" style="width:40px; height:40px;"
													src="${contextPath}/file/thumbMarketImage_s3.do?member_id=${chatroom.buyer_id}&market_image=${chatroom.buyer_market_image}">
											</c:otherwise>
										</c:choose>
									</c:when>
								</c:choose>
							</td>
							<td style="width:210px">
								<c:choose>
									<c:when test="${memberInfo.member_id == chatroom.buyer_id}">
										<p style="font-weight:bold">${chatroom.seller_id}</p>
										<p style="color:#737373">${lastMessageList[status.index].message_contents}
									</c:when>
									<c:when test="${memberInfo.member_id == chatroom.seller_id}">
										<p style="font-weight:bold">${chatroom.buyer_id}</p>
										<p style="color:#737373">${lastMessageList[status.index].message_contents}
									</c:when>
								</c:choose>
							</td>
							<td style="text-align:right">
								<jsp:useBean id="now" class="java.util.Date"/>
								<fmt:parseNumber value="${now.time / (1000*60*60*24)}" integerOnly="true" var="nowNumber"/>
								<fmt:parseNumber value="${lastMessageList[status.index].message_senderTime.time / (1000*60*60*24)}" integerOnly="true" var="messageNumber"/>
								<p style="color:#BDBDBD; font-size:8px">
									<c:choose>
										<c:when test="${(nowNumber - messageNumber) == 0}">
											<fmt:formatDate value="${lastMessageList[status.index].message_senderTime}" pattern="HH:mm" var="messageTime" />
											${messageTime}
										</c:when>
										<c:when test="${(nowNumber - messageNumber) == 1}">
											어제
										</c:when>
										<c:when test="${(nowNumber - messageNumber) > 1}">
											<fmt:formatDate value="${lastMessageList[status.index].message_senderTime}" pattern="yyyy-MM-dd" var="messageDate" />
											${messageDate}
										</c:when>
									</c:choose>
								</p>
								<c:if test="${noreadSizeList[status.index] != 0}">
									<div style="float:right; margin-top:3px" class="noreadCircle">${noreadSizeList[status.index]}</div>
								</c:if>
							</td>
						</tr>
				</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>