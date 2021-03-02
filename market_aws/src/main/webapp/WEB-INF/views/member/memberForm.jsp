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
	var overlapped = false;
	function fn_overlapped() {
		overlapped = true;
		
		var _id = $("#_member_id").val();
		if (_id == '') {
			alert("아이디를 입력하세요.");
			return;
		}
		
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
	
	function addMember() {
		var form = document.getElementById("joinForm");
		var write = true;
		var member_id = document.getElementById("_member_id").value;
		var member_pw = document.getElementById("_member_pw").value;
		
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
		
		if (checked == false || write == false)
			alert('필수 입력 사항을 모두 입력해주세요.');
		else if (overlapped == false)
			alert('아이디 중복체크를 해주세요.');
		else {
			form.submit();
		}
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
							<input type="text" name="member_id" id="_member_id" size="20" />
							<input type="button" id="btnOverlapped" value="중복체크" onClick="fn_overlapped()" />
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">비밀번호</td>
						<td><input type="password" id="_member_pw" name="member_pw" size="20" /></td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">이름</td>
						<td><input type="text" name="member_name" size="20" /></td>
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
							<input type="text" id="tel2"name="tel2" size="10" />
							-
							<input type="text" id="tel3" name="tel3" size="10" /><br><br>
						</td>
					</tr>
					<tr class="dot_line">
						<td class="fixed_join">이메일<br></td>
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
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="clear"></div>

		<input id="member_btn" class="btn" type="button" value="회원가입" onclick="addMember()">
	</form>
</body>
</html>