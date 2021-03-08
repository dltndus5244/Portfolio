<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<c:set var="marketInfo" value="${marketMap.marketInfo}" />
<c:set var="myGoodsList" value="${marketMap.myGoodsList}" />
<c:set var="myGoodsListSize" value="${fn:length(myGoodsList)}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<style type="text/css">
.star_rating {
	font-size:0; 
	letter-spacing:-10px;
}
.star_rating p {
	font-weight : bold;
    font-size:15px;
    letter-spacing:0;
    display:inline-block;
    margin-left:5px;
    color:#FFFFC6;
    text-decoration:none;
}
#buyer_market_image {
	float : left;
}
#review {
	margin-top : 5px;
	margin-left : 10px;
}
.star_rating p:first-child {margin-left:0;}
.star_rating p.on {color:#FFE400;}
</style>

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
$("document").ready(function() {
	//글 본문 글자 수 제한
	 $('#r_review_contents').on('keyup', function() {
			if($(this).val().length > 100) {
				alert("글자수는 100자로 이내로 제한됩니다.");
				$(this).val($(this).val().substring(0, 100));
			}
	});
});

//마켓 이름 수정 버튼 클릭 시 디자인 변경
function abled_market_name() {
	var i_market_name = document.getElementById("market_name");
	var enable_btn = document.getElementById("enable_btn");
	var mod_btn = document.getElementById("name_mod_btn");
	
	i_market_name.disabled = false;
	i_market_name.style.border = "1px solid #BDBDBD";
	i_market_name.style.fontSize = "16px";
	
	enable_btn.style.display = "none";
	mod_btn.style.display = "inline";
}

//마켓 이름 변경 함수
function modify_market_name() {
	var i_market_name = document.getElementById("market_name");
	var enable_btn = document.getElementById("enable_btn");
	var mod_btn = document.getElementById("name_mod_btn");
	
	var market_name = i_market_name.value;
	
	//마켓 이름을 변경해주는 ajax
	$.ajax({
		type:"post",
		async:false,
		url:"${contextPath}/market/modifyMarketName.do",
		data: {
			"market_name" : market_name
		},
		success:function(data, textStatus) {
			if (data == 'success') {
				alert("이름을 변경했습니다.");
			} else {
				alert("다시 시도해주세요.");
			}
		},
		error:function(data, textStatus) {
			alert("에러가 발생했습니다." + data);
		}
	});
	
	enable_btn.style.display = "inline";
	mod_btn.style.display = "none";
	
	i_market_name.style.border = "none";
}

window.onload = function() {
	selectBoxInit();
}

//회원정보, 상품상태 Select Box 초기화
function selectBoxInit() {
	var selTel1 = document.getElementById('tel1');
	var optionTel1 = selTel1.options;	
	var tel1 = '${memberInfo.tel1}';
	
	//전화번호 selectBox 초기화
	for (var i=0; i<optionTel1.length; i++) {
		if (optionTel1[i].value == tel1) {
			optionTel1[i].selected = true;
			break;
		}
	}
	
	//상품 상태 selectBox 초기화
	var myGoodsListSize = "${myGoodsListSize}";
	for (var i=0; i<myGoodsListSize; i++) {
		var selStatus = document.getElementById("goods_status"+i);
		var index = selStatus.options.selectedIndex;
		var goods_status = selStatus.options[index].value;
		
		if (goods_status == '거래완료') {
			$('#goods_status'+i).attr('disabled', 'true');
			$("#goods_modify_btn"+i).attr('disabled', 'true');
			$("#goods_modify_btn"+i).css({color:"#969696"});
		}
	}	
}

function fn_setEmail(obj) {
	var email2 = obj.value;
	$("#email2").val(email2);
	
}

//회원 정보 변경 함수
function fn_modify_info() {
	var frm_mod_member = document.frm_mod_member;

	//성별 가져옴
	var member_gender;
	var f_member_gender = frm_mod_member.member_gender;
	for (var i=0; i<f_member_gender.length; i++) {
		if (f_member_gender[i].checked) {
			member_gender = f_member_gender[i].value;
			break;
		}
	}
	
	//생년월일 가져옴
	var f_member_birth_y = frm_mod_member.member_birth_y;
	var f_member_birth_m = frm_mod_member.member_birth_m;
	var f_member_birth_d = frm_mod_member.member_birth_d;
	
	var member_birth_y, member_birth_m, member_birth_d;
	for (var i=0; i<f_member_birth_y.length; i++) {
		if (f_member_birth_y[i].selected) {
			member_birth_y = f_member_birth_y[i].value;
			break;
		}
	}
	
	for (var i=0; i<f_member_birth_m.length; i++) {
		if (f_member_birth_m[i].selected) {
			member_birth_m = f_member_birth_m[i].value;
			break;
		}
	}
	
	for (var i=0; i<f_member_birth_d.length; i++) {
		if (f_member_birth_d[i].selected) {
			member_birth_d = f_member_birth_d[i].value;
			break;
		}
	}
	
	//전화번호 가져옴
	var f_tel1 = frm_mod_member.tel1;
	var tel1;
	
	for (var i=0; i<f_tel1.length; i++) {
		if (f_tel1[i].selected) {
			tel1 = f_tel1[i].value;
			break;
		}
	}
	
	var f_tel2 = document.getElementById("tel2");
	var f_tel3 = document.getElementById("tel3");
	
	var tel2, tel3;
	if (f_tel2.value) tel2 = f_tel2.value;
	if (f_tel3.value) tel3 = f_tel3.value;
	
	//이메일 가져옴
	var email1 = document.getElementById("email1").value;
	var email2 = document.getElementById("email2").value;
	
	//변경된 데이터
	var updateData = {
			member_gender : member_gender,
			member_birth_y : member_birth_y,
			member_birth_m : member_birth_m,
			member_birth_d : member_birth_d,
			tel1 : tel1,
			tel2 : tel2,
			tel3 : tel3,
			email1 : email1,
			email2 : email2,
	}; 
	
	//변경된 회원 정보를 전달해주는 ajax
	$.ajax({
		type : "post",
		async : false, 
		url : "${contextPath}/member/modifyMyInfo.do",
		data : updateData,
		success : function(data, textStatus) {
			if (data == 'mod_success')
				alert('회원 정보를 수정했습니다.');
			
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		}
	}); 
}

//상품 삭제 함수
function fn_delete_goods(goods_id) {
	var form = document.createElement("form");
	
	var i_goods_id = document.createElement("input");
	i_goods_id.type = "hidden";
	i_goods_id.name = "goods_id";
	i_goods_id.value = goods_id;
	
	document.body.appendChild(form);
	form.appendChild(i_goods_id);
	
	form.method = "post"
	form.action = '${contextPath}/goods/deleteGoods.do';
	form.submit();
}

//상품 수정창으로 이동
function fn_modify_goods_form(goods_id) {
	var form = document.createElement("form");
	
	var i_goods_id = document.createElement("input");
	i_goods_id.type = "hidden";
	i_goods_id.name = "goods_id";
	i_goods_id.value = goods_id;
	
	document.body.appendChild(form);
	form.appendChild(i_goods_id);
	
	form.method = "post"
	form.action = '${contextPath}/goods/modifyGoodsForm.do';
	form.submit();
}

//마켓 이미지 변경 함수
function modify_market_image(member_id) {
	var form = document.getElementById("marketImageForm");
	var formData = new FormData(form);
	
	formData.append("member_id", member_id);
	
	//변경할 마켓 이미지를 전달해주는 ajax
	$.ajax({
		   url: '${contextPath}/market/modifyMarketImage.do',
		   processData: false,
		   contentType: false,
		   enctype: 'multipart/form-data',
		   data: formData,
		   type: 'post',
		   success: function(result){
			    alert("이미지를 수정했습니다!");
		   }
		 });
}

var pre_goods_status;
var goods_index;
var no_goods_id;

//상품 상태(예약중, 거래완료, 판매중) 변경  함수
function fn_modify_status(index, goods_id) {
	goods_index = index;
	
	var select_status = document.getElementById("goods_status"+index);
	var goods_status = select_status.options[select_status.selectedIndex].value;
	
	var seller_id = "${memberInfo.member_id}";
	var chatMemberList;
	
	//변경 할 상품 상태가 '거래완료'일 경우 먼저 구매자 선택을 해야함
	if (goods_status == '거래완료') {
		var buyerMap = null;
		
		//상품에 대해 채팅을 한 기록이 있는 회원 리스트를 가져옴
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/review/buyerSelectForm.do",
			data : {
				goods_id : goods_id,
				seller_id : seller_id
			},
			success : function(data, textStatus) {
				buyerMap = data;
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			}
		});
		
		var chatMemberList = buyerMap.chatMemberList;
		pre_goods_status = buyerMap.goods.goods_status;

		if (chatMemberList.length == 0) {
			$("#buyer_comment").text("구매자를 찾을 수 없습니다!");
			$("#buyer_no_btn").show();
			no_goods_id = goods_id;
		} else {
			$("#buyer_comment").text("거래가 완료되었습니다.");
			$("#buyer_comment2").text("구매자를 선택해주세요!");
			$("#buyer_no_btn").hide();
			
			var html = "";
			$("#buyer_list").empty();
			
			for (var i=0; i<chatMemberList.length; i++) {
				var marketInfo = chatMemberList[i];
				var market_image = marketInfo.market_image;
				
				if (!market_image)	
					html += "<img style='width:40px;height:40px;' class='profile' src='${contextPath}/resources/image/shop.png'>";
				else
					html += "<img style='width:40px;height:40px;' class='profile' src='${contextPath}/file/thumbMarketImage_s3.do?member_id="+marketInfo.member_id+"&market_image="+marketInfo.market_image+"'>";
				html += "<a href='javascript:select_buyer_id(\""+marketInfo.member_id+"\",\""+goods_id+"\")' style='margin-left:10px'>"+ marketInfo.member_id + "</a></br>";
			}
			
			$("#buyer_list").append(html);
		}
		
		$('#buyer_wrapper').toggleClass('open');

	} else { //나머지의 경우 상품 상태를 바로 변경함
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/goods/modifyGoodsStatus.do",
			data : {
				goods_status : goods_status,
				goods_id : goods_id
			},
			success : function(data, textStatus) {
				alert('상품상태를 변경하였습니다.')
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			}
		}); 
	}
}

//구매자 선택 모달 창 닫기 
function closeBuyerForm() {
	$("#buyer_wrapper").toggleClass('open');
	$("#goods_status"+goods_index).val(pre_goods_status).prop("selected", true);
}

//구매자 선택 함수
function select_buyer_id(buyer_id, goods_id) {
	var seller_id = "${memberInfo.member_id}";
	var select_status = document.getElementById("goods_status"+goods_index);
	var goods_status = select_status.options[select_status.selectedIndex].value;
	
	//구매자 선택 ajax
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
	
	//상품 상태 변경 ajax
	$.ajax({
		type : "post",
		async : false, 
		url : "${contextPath}/goods/modifyGoodsStatus.do",
		data : {
			goods_status : goods_status,
			goods_id : goods_id
		},
		success : function(data, textStatus) {
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		}
	});
	
	$("#buyer_wrapper").toggleClass('open');
	$('#goods_status'+goods_index).attr('disabled', 'true');
	$("#mod"+goods_index).attr('disabled', 'true');

}

//구매자 선택을 하지 않았을 경우 상품 상태만 변경함
function no_buyer() {
	var goods_status = "거래완료";
	var goods_id = no_goods_id;
	$.ajax({
		type : "post",
		async : false, 
		url : "${contextPath}/goods/modifyGoodsStatus.do",
		data : {
			goods_status : goods_status,
			goods_id : goods_id
		},
		success : function(data, textStatus) {
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		}
	});
	
	$("#buyer_wrapper").toggleClass('open');
	$('#goods_status'+goods_index).attr('disabled', 'true');
}

//상품 상세 창으로 이동
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

//회원 마켓 화면으로 이동
function marketMain(member_id) {
	var form = document.createElement("form");
	
	var i_member_id = document.createElement("input");
	i_member_id.type = "hidden";
	i_member_id.name = "member_id";
	i_member_id.value = member_id;
	
	document.body.appendChild(form);
	form.appendChild(i_member_id);
	
	form.method = "post"
	form.action = '${contextPath}/market/marketMain.do';
	form.submit();
}

//비밀번호 모달창 띄우기, 닫기
function openModifyForm() {
	 $("#pw_wrapper").toggleClass('open');
}

function closeModifyForm() {
	$("#pw_wrapper").toggleClass('open');
}

//비밀번호 수정 함수
function fn_modify_pw() {
	var member_pw = document.getElementById("new_member_pw").value;
	var member_pw2 = document.getElementById("new_member_pw_2").value;
	
	if (member_pw == member_pw2) {
		$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/member/modifyMyPw.do",
			data : {
				"member_pw" : member_pw
			},
			success : function(data, textStatus) {
				if (data == 'mod_success') {
					alert('비밀번호를 변경했습니다.');
					closeModifyForm()
				}
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			}
		});

	} else {
		alert('다시 입력해주세요');
	}
}

//이미지 미리보기
function readURL(input,preview) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#'+preview).attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}

//리뷰 별점 초기화
function init_review() {
	for (var i=1; i<=5; i++)
		$("#s"+i).removeClass("on");
	
	$("#r_review_contents").val('');
	$("#r_review_contents").attr("readonly", false);
}

var review_id = 0;

//리뷰 모달 띄우기
function openReviewForm(goods_id) {
	var buyer_id = "${memberInfo.member_id}";
	var reviewMap = null;

	//리뷰를 작성했는지 확인하기 위해서 reviewMap(goods, review)를 가져옴
	$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/review/reviewModal.do",
			data : {
				goods_id : goods_id,
				buyer_id : buyer_id
			},
			success : function(data, textStatus) {
				reviewMap = data;
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+textStatus);
			}
	});
	
	var goods = reviewMap.goodsVO;
	var review = reviewMap.reviewVO;
	review_id = review.review_id;
	
	init_review();
	
	//작성된 리뷰가 없을 경우
	if (!review.review_star && !review.review_contents) {
		$(".r_comment").text('거래가 완료되었다면 리뷰를 작성해주세요!'); 
		$("#r_review_btn").show();
	}
	//작성된 리뷰가 있을 경우
	else {
		$(".r_comment").text('이미 리뷰를 작성하셨습니다.'); 
		
		if (review.review_star == 1) {
			$("#s1").addClass("on");
		} else if (review.review_star == 2) {
			for (var i=1; i<=2; i++)
				$("#s"+i).addClass("on");
		} else if (review.review_star == 3) {
			for (var i=1; i<=3; i++)
				$("#s"+i).addClass("on");
		} else if (review.review_star == 4) {
			for (var i=1; i<=4; i++)
				$("#s"+i).addClass("on");
		} else if (review.review_star == 5) {
			for (var i=1; i<=5; i++)
				$("#s"+i).addClass("on");
		}
		
		$("#r_review_contents").val(review.review_contents);
		$("#r_review_contents").attr("readonly", true);
		$("#r_review_btn").hide();
	}
	
	$("#r_goods_title").text(goods.goods_title);

	var src = "${contextPath}/file/thumbGoodsImage_s3.do?goods_id=" + goods.goods_id + "&fileName=" + goods.filename;
	$("#r_goods_image").attr("src", src);	
	
	$('#review_wrapper').toggleClass('open');
	
}

//리뷰 작성 함수
function write_review() {
	var review_contents = document.getElementById("r_review_contents").value;
	var review_star = $('.on').length;

	if (review_star == 0 || !review_contents)
		alert('리뷰를 작성해주세요.');
	else {
		//리뷰 작성에 필요한 데이터(리뷰 아이디, 리뷰 내용, 리뷰 별점)을 보내는 ajax
	 	$.ajax({
			type : "post",
			async : false, 
			url : "${contextPath}/review/writeReview.do",
			data : {
				review_id : review_id,
				review_contents : review_contents,
				review_star : review_star
			},
			success : function(data, textStatus) {
				alert('리뷰를 작성하였습니다.')
			},
			error : function(data, textStatus) {
				alert("에러가 발생했습니다."+data);
			}
		});   
	 	
	 	$('#review_wrapper').toggleClass('open');
	}
	
}

//리뷰 모달창 닫기
function closeReviewForm() {
	$('#review_wrapper').toggleClass('open');
}

$(document).ready(function() {
	$(".star_rating a").click(function() {
	    $(this).parent().children("a").removeClass("on");
	    $(this).addClass("on").prevAll("a").addClass("on");
	    return false;
	});
});

function delete_goods_alert() {
	alert("삭제된 상품입니다.");
}
</script>
</head>
<body>
	<div class="market_main">
		<form id="marketImageForm" enctype="multipart/form-data">
			<div class="d_market_image">
				<c:choose>
						<c:when test="${not empty marketInfo.market_image}">
								<img class="profile" id="preview"
									src="${contextPath}/file/downloadMarketImage_s3.do?member_id=${marketInfo.member_id}&market_image=${marketInfo.market_image}">
						</c:when>
						<c:otherwise>
							<img class="profile" id="preview" src="${contextPath}/resources/image/shop.png" />
						</c:otherwise>
				</c:choose>
				<div class="filebox">
					<label for="market_image">마켓 이미지 업로드</label>
					<input type="file" id="market_image" name="market_image" onChange="readURL(this, 'preview')"/>
					<input type="button" id="image_btn" class="btn" value="이미지 등록" onclick="modify_market_image('${marketInfo.member_id}')"/>
				</div>
			</div>
			
			<div class="d_market_name">
				<input type="text" maxlength="10" style="font-weight:bold" id="market_name" class="market_name" value="${marketInfo.market_name}" disabled/>
				<input type="button" class="white_btn" id="enable_btn" value="마켓이름 수정" onclick="abled_market_name()"/>
				<input type="button" class="white_btn" id="name_mod_btn" value="확인" onclick="modify_market_name()" style="display:none"/>
			</div>
		</form>
	</div>
	
	<div class="clear"></div>
	
	<div class="tabs_menu">
	    <input id="tab1" type="radio" name="tabs" class="tabs" checked>
	    <label for="tab1">상품 ${fn:length(myGoodsList)}</label>
	
	    <input id="tab2" type="radio" name="tabs" class="tabs">
	    <label for="tab2">구매 ${fn:length(buyerGoodsList)}</label>
	
	    <input id="tab3" type="radio" name="tabs" class="tabs">
	    <label for="tab3">찜 ${fn:length(memberHeartList)}</label>
	    
	    <input id="tab4" type="radio" name="tabs" class="tabs">
	    <label for="tab4">마켓 후기 ${fn:length(reviewList)}</label>
	
	    <input id="tab5" type="radio" name="tabs" class="tabs">
	    <label for="tab5">정보수정</label>

	    <section id="content1" class="tabs_content">
	        <h3><span>상품</span> <span style="color:red">${fn:length(myGoodsList)}</span></h3>
	        <table>
	        	<tr align="center" style="color:#323232">
	        		<td>대표 이미지</td>
	        		<td>글 제목</td>
	        		<td>찜</td>
	        		<td>상품 상태</td>
	        		<td> </td>
	        		<td> </td>
	        	</tr>
	        	
		        <c:forEach var="goods" items="${myGoodsList}" varStatus="status">
		        		<tr align="center">
		        			<td>
		        				<a href="javascript:void(0)" onclick="detailGoods(${goods.goods_id})">
						        	<img width="110" height="110"
						        		src="${contextPath}/file/thumbGoodsImage_s3.do?goods_id=${goods.goods_id}&fileName=${goods.filename}">
			        			</a>
		        			</td>
		        			<td>${goods.goods_title}</td>
		        			<td><span style="color:red">❤</span> <span>${goods.heart_num}</span></td>
		        			<td>
		        				<select id="goods_status${status.index}" name="goods_status" onchange="fn_modify_status(${status.index}, ${goods.goods_id})">
		        					<c:choose>
		        						<c:when test="${goods.goods_status == '판매중'}">
		        							<option value="판매중" selected>판매중</option>
				        					<option value="예약중">예약중</option>
				        					<option value="거래완료">거래완료</option>
		        						</c:when>
		        						<c:when test="${goods.goods_status == '예약중'}">
		        							<option value="판매중">판매중</option>
				        					<option value="예약중" selected>예약중</option>
				        					<option value="거래완료">거래완료</option>
		        						</c:when>
		        						<c:when test="${goods.goods_status == '거래완료'}">
		        							<option value="판매중">판매중</option>
				        					<option value="예약중">예약중</option>
				        					<option value="거래완료" selected>거래완료</option>
		        						</c:when>
		        					</c:choose>
		        					
		        				</select>
		        			</td>
		        			<td><input type="button" id="goods_modify_btn${status.index}" class="white_btn" value="수정" onclick="fn_modify_goods_form(${goods.goods_id})"/></td>
		        			<td><input type="button" id="goods_remove_btn" class="white_btn" value="삭제" onclick="fn_delete_goods(${goods.goods_id})"/></td>
		        		</tr>
		        </c:forEach>
	        </table>
	    </section>
	
	    <section id="content2" class="tabs_content">
	        <h3><span>구매</span> <span style="color:red">${fn:length(buyerGoodsList)}</span></h3>
	        <br>
	        <c:choose>
	        	<c:when test="${empty buyerGoodsList}">
	        		구매내역이 없습니다.
	        	</c:when>
	        	<c:otherwise>
	        		<c:forEach var="buyer_goods" items="${buyerGoodsList}">
	        			<div class="goods" id="buyer_goods">
       						<a href="javascript:void(0)" onclick="detailGoods(${buyer_goods.goods_id})">
								<img width="180" height="180" 
									src="${contextPath}/file/thumbGoodsImage_s3.do?goods_id=${buyer_goods.goods_id}&fileName=${buyer_goods.filename}" >
							</a>
							<div class="title">${buyer_goods.goods_title}</div>
							<div class="price">
								<fmt:formatNumber value="${buyer_goods.goods_price}" type="number" var="goods_price" />${goods_price}원
								<input type="button" class="white_btn" value="리뷰쓰기" onclick="openReviewForm(${buyer_goods.goods_id})"/>
							</div>
						</div>
	        		</c:forEach>
	        	</c:otherwise>
	        </c:choose>
	    </section>
	
	    <section id="content3" class="tabs_content">
	        <h3><span>찜</span> <span style="color:red">${fn:length(memberHeartList)}</span></h3>
	        <br>
	        <c:forEach var="heart" items="${memberHeartList}">
	        	<div class="goods" id="heart_goods">
		        	<a href="javascript:void(0)" onclick="detailGoods(${heart.goods_id})">
			        	<img width="180" height="180"
			        		src="${contextPath}/file/thumbGoodsImage_s3.do?goods_id=${heart.goods_id}&fileName=${heart.filename}">
			        </a>
			        <div class="title">${heart.goods_title}</div>
					<div class="price">
						<fmt:formatNumber value="${heart.goods_price}" type="number" var="goods_price" />${goods_price}원
					</div>
		        </div>
	        </c:forEach>
	    </section>
	    
	    <section id="content4" class="tabs_content">
	        <h3><span>후기</span> <span style="color:red">${fn:length(reviewList)}</span></h3>
	        <br>
	        <c:forEach var="review" items="${reviewList}">
	        	<div id="review">
	        		<div id="review_image">
		        		<a href="javascript:void(0)" onclick="marketMain('${review.buyer_id}')">
		        			<c:choose>
		        				<c:when test="${empty review.market_image}">
		        					<img class="profile" src="${contextPath}/resources/image/shop.png" />
		        				</c:when>
		        				<c:otherwise>
		        					<img class="profile" 
		        						src="${contextPath}/file/thumbMarketImage_s3.do?member_id=${review.buyer_id}&market_image=${review.market_image}">
		        				</c:otherwise>
		        			</c:choose>
			        	</a>
		        	</div>
		        	<h5>${review.buyer_id}</h5>
		        	<c:choose>
		        		<c:when test="${review.review_star == 1}">
							    <span style="color:#FFE400">★</span>
							    <span style="color:#aaaaaa">★</span>
							    <span style="color:#aaaaaa">★</span>
							    <span style="color:#aaaaaa">★</span>
							    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 2}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#aaaaaa">★</span>
						    <span style="color:#aaaaaa">★</span>
						    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 3}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#aaaaaa">★</span>
						    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 4}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#aaaaaa">★</span>
		        		</c:when>
		        		<c:when test="${review.review_star == 5}">
		        			<span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
						    <span style="color:#FFE400">★</span>
		        		</c:when>
		        	</c:choose>
		        	<br><br>
		        	<div id="review_contents">
			        	<a href="javascript:void(0)" onclick="detailGoods(${review.goods_id})"><p>${review.goods_title} 〉</p></a>
			        	<h5 style="font-size:12px;">${review.review_contents}</h5>
		        	</div>
		        </div>
	        </c:forEach>
	    </section>
	
	    <section id="content5" class="tabs_content">
	        <h3>회원 정보 수정</h3>
	        <form name="frm_mod_member">
				<div id="detail_table">
					<table>
						<tbody>
							<tr class="dot_line">
								<td class="fixed_join">아이디</td>
								<td>
									<input type="text" name="member_id" size="20" value="${memberInfo.member_id}" disabled />
								</td>
								<td></td>
							</tr>
							<tr class="dot_line">
								<td class="fixed_join">이름</td>
								<td>
									<input type="text" name="member_name" size="20" value="${memberInfo.member_name}" disabled />
								</td>
								<td></td>
							</tr>
							<tr class="dot_line">
								<td class="fixed_join">성별</td>
								<td>
									<c:choose>
										<c:when test="${memberInfo.member_gender == '101'}">
											<input type="radio" name="member_gender" value="102" />
											여성 <span style="padding-left:30px"></span>
											<input type="radio" name="member_gender" value="101" checked />남성
										</c:when>
										<c:otherwise>
											<input type="radio" name="member_gender" value="102" checked/>
											여성 <span style="padding-left:30px"></span>
											<input type="radio" name="member_gender" value="101" />남성
										</c:otherwise>
									</c:choose>
								</td>
								<td></td>
							</tr>
							<tr class="dot_line">
								<td class="fixed_join">생년월일</td>
								<td>
								   <select name="member_birth_y">
								     <c:forEach var="i" begin="1" end="100">
								       <c:choose>
								         <c:when test="${memberInfo.member_birth_y == 1920+i }">
										   <option value="${1920+i}" selected>${1920+i} </option>
										</c:when>
										<c:otherwise>
										  <option value="${1920+i}" >${1920+i} </option>
										</c:otherwise>
										</c:choose>
								   	</c:forEach>
								</select>년 
								<select name="member_birth_m" >
									<c:forEach var="i" begin="1" end="12">
								       <c:choose>
								         <c:when test="${memberInfo.member_birth_m == i }">
										   <option value="${i}" selected>${i}</option>
										</c:when>
										<c:otherwise>
										  <option value="${i}">${i}</option>
										</c:otherwise>
										</c:choose>
								   	</c:forEach>
								</select>월 
								<select name="member_birth_d">
										<c:forEach var="i" begin="1" end="31">
								       <c:choose>
								         <c:when test="${memberInfo.member_birth_d == i }">
										   <option value="${i}" selected>${i}</option>
										</c:when>
										<c:otherwise>
										  <option value="${i}">${i}</option>
										</c:otherwise>
										</c:choose>
								   	</c:forEach>
								</select>일 <span style="padding-left:30px"></span>
								</td>
								<td></td>
							</tr>
							<tr class="dot_line">
								<td class="fixed_join">휴대폰번호</td>
								<td>
								   <select  name="tel1" id="tel1">
										<option>없음</option>
										<option value="010">010</option>
										<option value="011">011</option>
										<option value="016">016</option>
										<option value="017">017</option>
										<option value="018">018</option>
										<option value="019">019</option>
									</select> 
									 - <input type="text" id="tel2" size=4 value="${memberInfo.tel2 }"> 
									 - <input type="text" id="tel3"  size=4 value="${memberInfo.tel3 }">
							    </td>
							</tr>
							<tr class="dot_line">
								<td class="fixed_join">이메일</td>
								<td>
								   <input type="text" id="email1" size=10 value="${memberInfo.email1}" /> @ 
								   <input type="text" size=10  id="email2" value="${memberInfo.email2}" /> 
								   <select name="select_email2" onChange="fn_setEmail(this)"  title="직접입력">
										<option value="non">직접입력</option>
										<option value="hanmail.net">hanmail.net</option>
										<option value="naver.com">naver.com</option>
										<option value="yahoo.co.kr">yahoo.co.kr</option>
										<option value="hotmail.com">hotmail.com</option>
										<option value="paran.com">paran.com</option>
										<option value="nate.com">nate.com</option>
										<option value="google.com">google.com</option>
										<option value="gmail.com">gmail.com</option>
										<option value="empal.com">empal.com</option>
										<option value="korea.com">korea.com</option>
										<option value="freechal.com">freechal.com</option>
								</select> 
								</td>
							</tr>
						</tbody>
					</table>
					<div id="mod_btn">
						<input class="btn" type="button" value="수정하기" onClick="fn_modify_info()" />
						<input class="btn" type="button" value="비밀번호 변경하기" onClick="openModifyForm()" />
					</div>
				</div>
				<br><br>
			</form>
	    </section>
	</div>
	
	<!-- 리뷰 모달창 -->
	<div class="modal-wrapper" id="review_wrapper">
	  <div class="modal" style="height:600px; width:500px;">
	    <div class="modal-head">
	      <a id="close_btn" class="btn-close" href="javascript:void(0)" onclick="closeReviewForm()">
	      	<img width="15" height="15" src="${contextPath}/resources/image/close.png" />
	      </a>
	    </div>
	    <div class="content">
	        <div id="review_modal">
				<h4 class="r_comment"></h4>
				<br><br>
				<img id="r_goods_image" width="90" height="90" src="">
				<div id="d_goods_title">
					<h4 style="color:#6e6e6e; margin-bottom:5px; font-size:15px;">거래한 물건</h4>
					<h5 id="r_goods_title"></h5>
				</div>
				<div class="clear"></div>
				<div id="d_review_form">
					<p class="star_rating">
					    <a id="s1" href="#">★</a>
					    <a id="s2" href="#">★</a>
					    <a id="s3"href="#">★</a>
					    <a id="s4"href="#">★</a>
					    <a id="s5"href="#">★</a>
					</p>			
					<br>
					<textarea name="review_contents" id="r_review_contents" rows="8" cols="40"></textarea>
				</div>
				<input id="r_review_btn" type="button" value="리뷰 쓰기" onclick="write_review()"/>
			</div>
	    </div>
	  </div>
	</div>
	
	<!-- 비밀번호 변경 모달창 -->
	<div class="modal-wrapper" id="pw_wrapper">
	  <div class="modal">
	    <div class="modal-head">
	      <a id="close_btn" class="btn-close" href="javascript:void(0)" onclick="closeModifyForm()">
	      	<img width="15" height="15" src="${contextPath}/resources/image/close.png" />
	      </a>
	    </div>
	    <div class="content">
	        <div id="pw_modal">
				변경 비밀번호 <input type="password" id="new_member_pw" style="margin-left:28px"/><br>
				변경 비밀번호 확인 <input type="password" id="new_member_pw_2"/><br>
				<input style="color:#FFB85A; margin-left:220px;" type="submit" class="white_btn" value="변경하기" onclick="fn_modify_pw()"/>
			</div>
	    </div>
	  </div>
	</div>
	
	<!-- 구매자 선택 모달창 -->
	<div class="modal-wrapper" id="buyer_wrapper">
	  <div class="modal" style="width:300px; height:400px;">
	    <div class="modal-head">
	      <a id="close_btn" class="btn-close" href="javascript:void(0)" onclick="closeBuyerForm()">
	      	<img width="15" height="15" src="${contextPath}/resources/image/close.png" />
	      </a>
	    </div>
	    <div class="content">
	        <div id="buyer_modal">
				<p id="buyer_comment"></p>
				<p id="buyer_comment2">
				<div id="buyer_no_btn">
					<input type="button" class="white_btn" value="거래확정하기" onclick="no_buyer()"/>
					<input type="button" class="white_btn" value="취소하기" onclick="closeBuyerForm()"/>
				</div>
				<div id="buyer_list" style="margin-top:10px">
				</div>
			</div>
	    </div>
	  </div>
	</div>
</body>
</html>