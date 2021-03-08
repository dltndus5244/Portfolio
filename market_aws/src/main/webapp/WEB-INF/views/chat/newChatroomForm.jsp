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
<title>채팅방</title>
<style type="text/css">
body {
	overflow-x:hidden; 
	overflow-y:auto;
}
</style>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	var sock = new SockJS('http://3.130.107.103:8080/market/newChat'); //소켓 생성
	sock.onmessage = onMessage;
	
	var count = 0;
	$(function() {
		$("#sendBtn").click(function() {
			var message = document.getElementById("message").value;
			if (message) {
				count++;
				//처음 메시지를 보내는 경우에만 채팅방을 생성하는 함수를 추가로 실행
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
	//DB에 채팅방을 생성해주는 함수 
	function addChatroom() {
		var goods_id = "${chatroom.goods_id}";
		
		//채팅방을 생성하기 위한 ajax
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
			}
		});
	}
	
	var message_receiver;
	//DB에 메시지를 저장하기 위한 함수
	function addMessage() {
		var message_sender = "${memberInfo.member_id}";
		var message_contents = $("#message").val();
		
		var buyer_id = "${chatroom.buyer_id}";
		var seller_id = "${chatroom.seller_id}";
			
		if (message_sender == buyer_id)
			message_receiver = seller_id;
		else
			message_receiver = buyer_id;
		
		//DB에 메시지를 저장하기 위해 필요한 정보(채팅방 아이디, 메시지 송신자/수신자, 메시지 내용)를 보내는 ajax
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
			}
		});
	}
	
	//소켓으로 메시지를 보내는 함수
	function sendMessage() {
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
	
	//소켓으로부터 메시지를 받은 경우 div(chatList)에 받은 내용(회원 아이디, 메시지)을 추가해줌
	function onMessage(evt) {
		var data = evt.data;
		var d = JSON.parse(data);
		
		var member_id = d.member_id;
		var message = d.message;
		
		var session_id = "${memberInfo.member_id}";

		var buyer_id = "${chatroom.buyer_id}";
		var buyer_market_image = "${chatroom.buyer_market_image}";
		var seller_id = "${chatroom.seller_id}";
		var seller_market_image = "${chatroom.seller_market_image}";
		
		var youId = null;
		if (session_id == buyer_id) //로그인 한 회원이 구매자인 경우 상대방은 판매자임
			youId = seller_id;
		else if (session_id == seller_id) //로그인 한 회원이 판매자인 경우 상대방은 구매자임
			youId = buyer_id;
					
		var printHTML;
		
		//'나'와 '상대방'을 구분해서 메시지를 표시함
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
	
	function fn_chat_list() {
		var url = "${contextPath}/chat/getChatroomList.do";
	
		var form = document.createElement("form");
		document.body.appendChild(form);

		form.action = url;
		form.method = "post";
		form.submit();
	}

</script>
</head>
<body>
	<div id="newChatList" class="chatData">
		<div class="chatHead">
			<div class="goChatMain">
				<a href="javascript:void(0)" onclick="fn_chat_list()"><img width="30" height="30" src="${contextPath}/resources/image/left-arrow.png" /></a>
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