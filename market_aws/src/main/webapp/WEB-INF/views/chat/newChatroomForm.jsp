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
<title>Insert title here</title>
<style type="text/css">
body {
	overflow-x:hidden; 
	overflow-y:auto;
}
</style>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	//websocket을 지정한 URL로 연결
	var sock = new SockJS('http://3.130.107.103:8080/market_aws/newChat');
	//websocket 서버에서 메시지를 보내면 자동으로 실행된다.
	sock.onmessage = onMessage;
	//websocket과 연결을 끊고 싶을 때 실행하는 메소드
	
	var count = 0;
	$(function() {
		$("#sendBtn").click(function() {
			var message = document.getElementById("message").value;
			if (message) {
				count++;
				if (count == 1) {
					addChatroom();
					addMessage();
					sendMessage();
				} else {
					addMessage();
					sendMessage();
				}	
			}
		});
	});     
	
	var chatroom_id = 0;
	function addChatroom() {
		var goods_id = "${chatroom.goods_id}";
		
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/chat/addChatroom.do",
			data : {
				goods_id : goods_id
			},
			success : function(data, textStatus) {
				chatroom_id = data;
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			},
		});
	}
	
	var message_receiver;
	function addMessage() {
		var message_sender = "${memberInfo.member_id}";
		var message_contents = $("#message").val();
		
		var buyer_id = "${chatroom.buyer_id}";
		var seller_id = "${chatroom.seller_id}";
			
		if (message_sender == buyer_id)
			message_receiver = seller_id;
		else
			message_receiver = buyer_id;
		
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/chat/addMessage.do",
			data : {
				chatroom_id : chatroom_id,
				message_sender : message_sender,
				message_receiver : message_receiver,
				message_contents : message_contents
			},
			success : function(data, textStatus) {

			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			},
		});
	}
	
	function sendMessage() {
		//websocket으로 메시지를 보내겠다.
		var member_id = "${memberInfo.member_id}";
		var message = $("#message").val();
		var data = {
				member_id : member_id,
				message : message,
				message_receiver : message_receiver,
				chatroom_id : String(chatroom_id)
		}
		
		sock.send(JSON.stringify(data));
	}
	
	//evt 파라미터는 websocket이 보내준 데이터이다.
	function onMessage(evt) {
		var data = evt.data;
		var d = JSON.parse(data);
		var member_id = d.member_id;
		var session_id = "${memberInfo.member_id}";
		var message = d.message;

		var buyer_id = "${chatroom.buyer_id}";
		var seller_id = "${chatroom.seller_id}";
		var youId = null;
		
		if (session_id == buyer_id)
			youId = seller_id;
		else if (session_id == seller_id)
			youId = buyer_id;
		
		var buyer_market_image = "${chatroom.buyer_market_image}";
		var seller_market_image = "${chatroom.seller_market_image}";
				
		var printHTML;
		if (member_id == session_id) {
			printHTML = "<div id='myChat'>";
			printHTML += "<div class='my_ballon'>";
			printHTML += message;
			printHTML += "</div>";
			printHTML += "</div>";
		} else {
			printHTML = "<div id='yourChat'>";
			if (youId == buyer_id) {
				if (!buyer_market_image) {
					printHTML += "<img style='width:30px; height:30px;' class='profile' src='${contextPath}/resources/image/shop.png'/>";
				}
				else {
					printHTML += "<img style='width:30px; height:30px;' class='profile' src='${contextPath}/file/thumbMarketImage_s3.do?member_id="+buyer_id+"&market_image="+buyer_market_image+"'/>";
				}		
			}
			else if (youId == seller_id) {
				if (!seller_market_image) {
					printHTML += "<img style='width:30px; height:30px;' class='profile' src='${contextPath}/resources/image/shop.png'/>";
				}
				else {
					printHTML += "<img style='width:30px; height:30px;' class='profile' src='${contextPath}/file/thumbMarketImage_s3.do?member_id="+seller_id+"&market_image="+seller_market_image+"'/>";
				}	
			}
			printHTML += youId + "<br>";
			printHTML += "<div class='your_balloon'>";
			printHTML += message;
			printHTML += "</div>";
			printHTML += "</div>";
		}
		
		$("#newChatList").append(printHTML);
		$("#message").val("");
	}

</script>
</head>
<body>
	<div id="newChatList" class="chatData">
		<div class="chatHead">
			<div class="goChatMain">
				<a href="${contextPath}/chat/getChatroomList.do"><img width="30" height="30" src="${contextPath}/resources/image/left-arrow.png" /></a>
				<img style="margin-left:145px" width="30" height="30" src="${contextPath}/resources/image/chat.png" />
			</div>
			<div class="chatHeadImage">
				<img width="75" height="75" src="${contextPath}/file/thumbGoodsImage_s3.do?goods_id=${goodsVO.goods_id}&fileName=${goodsVO.filename}" >
			</div>
			<div class="chatHeadRemain">
				<h3 class="chat_title">${goodsVO.goods_title}</h3>
				<fmt:formatNumber value="${goodsVO.goods_price}" type="number" />원
			</div>
		</div>
		<div class="clear"></div>
	</div>	
	<div id="sendMessage">
		<textarea style="border:none" id="message" rows="5" cols="45"></textarea>
		<input type="submit" value="전송" id="sendBtn"/>
	</div>
</body>
</html>