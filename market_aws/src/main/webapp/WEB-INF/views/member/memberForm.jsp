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
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	  //한글입력 안되게 처리
	  $("input[name=member_id]").keyup(function(event){ 
		   if (!(event.keyCode >=37 && event.keyCode<=40)) {
			    var inputVal = $(this).val();
			    $(this).val(inputVal.replace(/[^a-z0-9]/gi,''));
		   }
	  });
	  
	  //전화번호 숫자만 입력되게 처리
	  $("input[name=tel2]").keyup(function(event){ 
		   if (!(event.key >= 0 && event.key <= 9)) {
			   var inputVal = $(this).val();
			   $(this).val(inputVal.replace(/[^0-9]/gi,''));
		   }
	  });
	  
	  $("input[name=tel3]").keyup(function(event){ 
		   if (!(event.key >= 0 && event.key <= 9)) {
			   var inputVal = $(this).val();
			   $(this).val(inputVal.replace(/[^0-9]/gi,''));
		   }
	  });
	  
});

var overlapped = false;

//아이디 중복 검사 함수
function fn_overlapped() {
	overlapped = true;
	
	var _id = $("#_member_id").val();
	if (_id == '') {
		alert("아이디를 입력하세요.");
		return;
	}
	
	//아이디 중복 검사 ajax
	$.ajax({
		type:"post",
		async:false,
		url:"${contextPath}/member/overlapped.do",
		dataType:"text",
		data:{id:_id},
		success:function(data, textStatus) {
			if (data == '') {
				alert("사용할 수 있는 아이디입니다.");
				$("#btnOverlapped").prop("disabled", true);
				
			} else {
				alert("사용할 수 없는 아이디입니다.");
			}
		},
		error:function(data, textStatus) {
			alert("에러가 발생했습니다.");
		},
		complete:function(data, textStatus) {

		}
	});
}

function fn_setEmail(obj) {
	var email2 = obj.value;
	$("#email2").val(email2);
	
}

//회원 추가 함수
function addMember() {
	var form = document.getElementById("joinForm");
	var write = true;
	var member_id = document.getElementById("_member_id").value;
	var member_pw = document.getElementById("_member_pw").value;
	var email_auth = document.getElementById("authCode").value;
		
	//필수 입력 항목 예외처리
	var r_member_gender = document.getElementsByName("member_gender");
	var checked = false;
	for(var i=0;i<r_member_gender.length;i++){
		if(r_member_gender[i].checked == true){ 
			checked = true;
		}
	}
	
	var tel2 = document.getElementById("tel2").value;
	var tel3 = document.getElementById("tel3").value;
	var email1 = document.getElementById("email1").value;
	var email2 = document.getElementById("email2").value;
			
	if (!member_id) {
		write = false;
	}
	else if (!member_pw) {
		write = false;
	}
	else if (!tel2) {
		write = false;
	}
	else if (!tel3) {
		write = false;
	}
	else if (!email1) {
		write = false;
	}
	else if (!email2) {
		write = false;
	}
	
	if (member_id && member_id.length < 6) {
		alert('아이디를 6자 이상으로 입력해주세요.');
		return;
	}
	
	if (!email_auth && write == true) {
		alert('이메일 인증번호를 입력해주세요.');
		return;
	}
		
	if (checked == false || write == false)
		alert('필수 입력 사항을 모두 입력해주세요.');
	else if (overlapped == false)
		alert('아이디 중복체크를 해주세요.');
	else {
		form.submit();
	}
}

var send_click = false;
function sendEmail() {
	var email1 = document.getElementById("email1").value;
	var email2 = document.getElementById("email2").value;
	
	if (!email1 || !email2) {
		alert("이메일을 입력해주세요.");
		return;
	}
	
	var email = email1 + "@" + email2;
	
	$.ajax({
		type : "post",
		async : false, 
		url : "${contextPath}/member/sendAuthMail.do",
		data : {
			email : email
		},
		success : function(data, textStatus) {
			alert("인증 번호를 발송하였습니다.");
			send_click = true;
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		}
	}); 
	
}

function checkAuth() {
	var authCode = document.getElementById("authCode").value;
	var sendBtn = document.getElementById("sendBtn");
	var checkBtn = document.getElementById("checkBtn");
	
	if (send_click == false) {
		alert("인증번호를 받지 않았습니다. '인증번호 받기' 버튼을 눌러주세요.");
		return;
	}
	
	if (!authCode) {
		alert("인증번호를 입력해주세요.");
		return;
	}
	
	$.ajax({
		type : "post",
		async : false, 
		url : "${contextPath}/member/checkAuthCode.do",
		data : {
			authCode : authCode
		},
		success : function(data, textStatus) {
			if (data == "success") {
				alert("인증이 성공하였습니다.");
				sendBtn.style.display = "none";
				checkBtn.style.display = "none";
			}
			else {
				alert("인증번호가 일치하지 않습니다. 다시 입력해주세요.");
			}
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		}
	}); 
	
}
</script>
</head>
<body>
	<form id="joinForm" action="${contextPath}/member/addMember.do" method="post">
		<div id="memberForm">
			<h3>필수 입력 정보</h3>
			<table>
				<tbody>
					<tr class="dot_line">
						<td class="fixed_join">아이디</td>
						<td>
							<input type="text" name="member_id" id="_member_id" size="20" maxlength="14"/>
							<input type="button" id="btnOverlapped" value="중복체크" onClick="fn_overlapped()" />
						</td>
						<td>
							<p style="font-size:9px; color:#828282">* 영어와 숫자만 입력가능합니다.</p>
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">비밀번호</td>
						<td><input type="password" id="_member_pw" name="member_pw" size="20" maxlength="18"/></td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">이름</td>
						<td><input type="text" name="member_name" size="20" maxlength="10"/></td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">성별</td>
						<td>
							<input type="radio" name="member_gender" value="102" />&nbsp;여성
							<span style="padding-left:100px"></span>
							<input type="radio" name="member_gender" value="101" />&nbsp;남성
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">생년월일</td>
						<td>
							<select name="member_birth_y" id="member_birth_y">
								<c:forEach var="year" begin="1" end="100">
									<option value="${1920+year}">${1920+year}</option>
								</c:forEach>
							</select>&nbsp;년
							<select name="member_birth_m">
								<c:forEach var="month" begin="1" end="12">
									<option value="${month}">${month}</option>
								</c:forEach>
							</select>&nbsp;월
							<select name="member_birth_d">
								<c:forEach var="day" begin="1" end="31">
									<option value="${day}">${day}</option>
								</c:forEach>
							</select>&nbsp;일
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">휴대폰번호</td>
						<td>
							<select name="tel1">
								<option>없음</option>
								<option selected value="010">010</option>
								<option value="011">011</option>
								<option value="016">016</option>
								<option value="017">017</option>
								<option value="018">018</option>
								<option value="019">019</option>
							</select>
							-
							<input type="text" id="tel2"name="tel2" size="10" maxlength="4"/>
							-
							<input type="text" id="tel3" name="tel3" size="10" maxlength="4"/><br><br>
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">이메일</td>
						<td>
							<input type="text" id="email1" name="email1" size="10">&nbsp;@&nbsp;<input type="text" name="email2" id="email2" size="10" />
							<select name="email" onChange="fn_setEmail(this)" title="직접입력">
								<option value="non">직접입력</option>
								<option value="naver.com">naver.com</option>
								<option value="daum.net">daum.net</option>
								<option value="hanmail.net">hanmail.net</option>
								<option value="nate.com">nate.com</option>
								<option value="gmail.com">gmail.com</option>
								<option value="kakao.com">kakao.com</option>
							</select>
						</td>
						<td>
							<input type="button" id="sendBtn" onclick="sendEmail()" class="white_btn" style="font-size:12px" value="인증번호 받기">
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">이메일 인증번호</td>
						<td>
							<input type="text" id="authCode" size="20">
							<input type="button" id="checkBtn" onclick="checkAuth()" value="확인" class="white_btn" style="font-size:12px">	
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="clear"></div>

		<input id="member_btn" class="btn" type="button" value="회원가입" onclick="addMember()">
	</form>
</body>
</html>