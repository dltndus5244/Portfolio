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
<title>채팅방</title>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	var sock = new SockJS('http://3.130.107.103:8080/market_aws/chat'); //소켓 생성

	//소켓으로부터 메시지를 받은 경우 div(chatList)에 받은 내용(회원 아이디, 메시지)을 추가해줌
	sock.onmessage = function(evt) {
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
	
		$("#chatList").append(printHTML);
		$("#message").val("");
		
		var scroll = $('#chatList')[0].scrollHeight;
		$(document).scrollTop(scroll);
	}

	var chatroom_id = "${chatroom.chatroom_id}";
	var member_id = "${memberInfo.member_id}";
	var noreadMessageList;
	
	//소켓이 생성될 때(채팅방에 들어올 때) 읽지 않은 메시지가 있다면 메시지를 읽음 상태로 변경
	sock.onopen = function(e) {
		//읽지 않은 메시지를 가져오는 ajax
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/chat/getNoReadMessage.do",
			data : {
				chatroom_id : chatroom_id,
				message_receiver : member_id,
			},
			success : function(data, textStatus) {
				noreadMessageList = data;
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			}
		});	
		
		//읽지 않은 메시지가 있을 경우 메시지를 읽음 상태로 변경하는 ajax
		if (noreadMessageList.length > 0) {
			$.ajax({
				type : "post",
				async : false, 
				url : "${contextPath}/chat/modifyMessageRead.do",
				data : {
					chatroom_id : chatroom_id,
					message_receiver : member_id,
				},
				success : function(data, textStatus) {	
				},
				error : function(data, textStatus) {
					alert("code:"+data.status+"\n"+"message:"+data.responseText+"\n"+"error:"+textStatus);
				}
			});	
		}
	}
	
	//메시지 전송 버튼 클릭 시 addMessage()와 sendMessage() 호출
	$(function() {
		$("#sendBtn").click(function() {
			var message = document.getElementById("message").value;
			if (message) {
				addMessage();
				sendMessage();
				var scroll = $('#chatList')[0].scrollHeight;
				$(document).scrollTop(scroll+100);
			}
		});
	});     
	
	var message_receiver;
	//DB에 메시지를 저장하는 함수
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
				chatroom_id : chatroom_id,
				member_id : member_id,
				message : message,
				message_receiver : message_receiver
		}
		
		sock.send(JSON.stringify(data));
	}
</script>
</head>
<body>
	<div id="chatList" class="chatData">
		<!-- 채팅헤드(메인화면으로 이동, 상품 이미지, 상품 이름, 상품 가격) -->
		<div class="chatHead">
			<div class="goChatMain">
				<a href="${contextPath}/chat/getChatroomList.do">
					<img width="30" height="30" src="${contextPath}/resources/image/left-arrow.png" />
				</a>
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
		
		<!-- 채팅 메시지 표시 -->
		<c:forEach var="message" items="${messageList}">
			<c:choose>
				<!-- 로그인 한 회원이 메시지를 보낸 사람일 경우 myChat(오른쪽에 메시지 띄움) -->
				<c:when test="${memberInfo.member_id == message.message_sender}">
					<div id="myChat">
						<div class="my_ballon">
							${message.message_contents}
						</div>
					</div>
				</c:when>
				
				<!-- 로그인 한 회원이 메시지를 받는 사람일 경우 yourChat(왼쪽에 메시지 띄움) -->
				<c:otherwise>
					<div id="yourChat">
						<c:choose>
							<c:when test="${message.message_sender == chatroom.buyer_id}">
								<c:choose>
									<c:when test="${empty chatroom.buyer_market_image}">
										<img style="width:30px; height:30px;" class="profile" src="${contextPath}/resources/image/shop.png" />
									</c:when>
									<c:otherwise>
										<img class="profile" style="width:30px; height:30px;"
											src="${contextPath}/file/thumbMarketImage_s3.do?member_id=${chatroom.buyer_id}&market_image=${chatroom.buyer_market_image}">
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${message.message_sender == chatroom.seller_id}">
								<c:choose>
									<c:when test="${empty chatroom.seller_market_image}">
										<img style="width:30px; height:30px;" class="profile" src="${contextPath}/resources/image/shop.png" />
									</c:when>
									<c:otherwise>
										<img class="profile" style="width:30px; height:30px;"
											src="${contextPath}/file/thumbMarketImage_s3.do?member_id=${chatroom.seller_id}&market_image=${chatroom.seller_market_image}">
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
						${message.message_sender}<br>
						<div class="your_balloon">
							${message.message_contents}
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</div>	
	
	<!-- 메시지를 보내는 div -->
	<div id="sendMessage">
		<textarea style="border:none" id="message" rows="5" cols="45"></textarea>
		<input type="submit" value="전송" id="sendBtn"/>
	</div>
</body>

<script>
var scroll = $('#chatList')[0].scrollHeight;
$(document).ready(function() {
	$(document).scrollTop(scroll);
});

</script>
</html>