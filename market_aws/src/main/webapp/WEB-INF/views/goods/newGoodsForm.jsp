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
<title>상품등록 페이지</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
$("document").ready(function() {
	//거래지역 선택 selectBox
	var area0 = ["시/도 선택","서울특별시","인천광역시","대전광역시","광주광역시","대구광역시","울산광역시","부산광역시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"];
	var area1 = ["강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"];
	var area2 = ["계양구","남구","남동구","동구","부평구","서구","연수구","중구","강화군","옹진군"];
	var area3 = ["대덕구","동구","서구","유성구","중구"];
	var area4 = ["광산구","남구","동구","북구","서구"];
	var area5 = ["남구","달서구","동구","북구","서구","수성구","중구","달성군"];
	var area6 = ["남구","동구","북구","중구","울주군"];
	var area7 = ["강서구","금정구","남구","동구","동래구","부산진구","북구","사상구","사하구","서구","수영구","연제구","영도구","중구","해운대구","기장군"];
	var area8 = ["고양시","과천시","광명시","광주시","구리시","군포시","김포시","남양주시","동두천시","부천시","성남시","수원시","시흥시","안산시","안성시","안양시","양주시","오산시","용인시","의왕시","의정부시","이천시","파주시","평택시","포천시","하남시","화성시","가평군","양평군","여주군","연천군"];
	var area9 = ["강릉시","동해시","삼척시","속초시","원주시","춘천시","태백시","고성군","양구군","양양군","영월군","인제군","정선군","철원군","평창군","홍천군","화천군","횡성군"];
	var area10 = ["제천시","청주시","충주시","괴산군","단양군","보은군","영동군","옥천군","음성군","증평군","진천군","청원군"];
	var area11 = ["계룡시","공주시","논산시","보령시","서산시","아산시","천안시","금산군","당진군","부여군","서천군","연기군","예산군","청양군","태안군","홍성군"];
	var area12 = ["군산시","김제시","남원시","익산시","전주시","정읍시","고창군","무주군","부안군","순창군","완주군","임실군","장수군","진안군"];
	var area13 = ["광양시","나주시","목포시","순천시","여수시","강진군","고흥군","곡성군","구례군","담양군","무안군","보성군","신안군","영광군","영암군","완도군","장성군","장흥군","진도군","함평군","해남군","화순군"];
	var area14 = ["경산시","경주시","구미시","김천시","문경시","상주시","안동시","영주시","영천시","포항시","고령군","군위군","봉화군","성주군","영덕군","영양군","예천군","울릉군","울진군","의성군","청도군","청송군","칠곡군"];
	var area15 = ["거제시","김해시","마산시","밀양시","사천시","양산시","진주시","진해시","창원시","통영시","거창군","고성군","남해군","산청군","의령군","창녕군","하동군","함안군","함양군","합천군"];
	var area16 = ["서귀포시","제주시","남제주군","북제주군"];

	$("select[name^=goods_location]").each(function() {
		$selsido = $(this);
	  	$.each(eval(area0), function() {
	   		$selsido.append("<option value='"+this+"'>"+this+"</option>");
	  	});
	  	$selsido.next().append("<option value=''>구/군 선택</option>");
	 });

	 $("select[name^=goods_location]").change(function() {
	 	var area = "area"+$("option",$(this)).index($("option:selected",$(this)));
	  	var $gugun = $(this).next(); 
	  	$("option",$gugun).remove();

	  	if(area == "area0")
	   		$gugun.append("<option value=''>구/군 선택</option>");
	  	else {
	   		$.each(eval(area), function() {
	    		$gugun.append("<option value='"+this+"'>"+this+"</option>");
	   		});
	  	}
	 });
	 
	 //글 본문 글자 수 제한
	 $('#i_goods_contents').on('keyup', function() {
			if($(this).val().length > 500) {
				alert("글자수는 500자로 이내로 제한됩니다.");
				$(this).val($(this).val().substring(0, 500));
			}
	});
	 
	 //상품 가격 숫자만 입력되게
	 $("input[name=goods_price]").keyup(function(event){ 
		   if (!(event.key >= 0 && event.key <= 9)) {
			   var inputVal = $(this).val();
			   $(this).val(inputVal.replace(/[^0-9]/gi,''));
		   }
	  });
	 
	//상품 가격 글자 수 제한
	 $('#i_goods_price').on('keyup', function() {
			if($(this).val().length > 9) {
				$(this).val($(this).val().substring(0, 9));
			}
	});
	
});

var cnt = 0;
//이미지 추가하기 버튼 클릭 시 동적으로 추가해주는 함수
function fn_addFile() {
	if (cnt == 5) {
		alert("파일은 최대 5장까지 첨부 가능합니다.");
		document.getElementById("add_btn").disabled = "disabled";
		return;
	}
	if (cnt == 0)
		$("#d_file").append("<br>" + "<input type='file' name='main_image' id='f_main_image' onchange='fileUpload(this)'/>");
	else 
		$("#d_file").append("<br>" + "<input type='file' name='sub_image" + cnt + "' id='f_sub_image" + cnt + "' onchange='fileUpload(this)'/>");	
	
	var file = $("#f_sub_image" + cnt).val();
	if (!file) {
		document.getElementById("add_btn").disabled = "disabled";
	}
	cnt++;
}

function fileUpload(obj) {
	var fileName = obj.value;
	document.getElementById("add_btn").disabled = false;
}

//상품 추가 함수
function fn_add_new_goods() {
	var write = true;
	var form = document.getElementById("goodsForm");
	
	var goods_title = document.getElementById("i_goods_title").value;
	var goods_contents = document.getElementById("i_goods_contents").value;
	var goods_price = document.getElementById("i_goods_price").value;
	var fileName = $('#f_main_image').val();
	var goods_location1 = $("#i_goods_location1 option:selected").text();
	var goods_location2 = $("#i_goods_location2 option:selected").text();
	
	//필수 입력 값이 없을 경우 에러처리
	if (!goods_title)
		write = false;
	else if (!goods_contents)
		write = false;
	else if (!goods_price)
		write = false;
	else if (goods_location1 == '시/도 선택')
		write = false;
	else if (goods_location2 == '구/군 선택')
		write = false;
	
	if (!fileName) {
		alert('이미지를 한 장 이상 첨부해주세요.');
		return;
	}
	
	if (write == false) {
		alert('입력 사항을 모두 입력해주세요.');
	} else {
		form.submit();
	}
}


</script>
</head>
<body>
	<form id="goodsForm" action="${contextPath}/goods/addNewGoods.do" method="post" enctype="multipart/form-data">
		<h3>상품 등록하기</h3>
		<table>
			<tr>
				<td>글 제목</td>
				<td><input size="51" type="text" id="i_goods_title" name="goods_title" maxlength="50"></td>
			</tr>
			<tr>
				<td>글 내용</td>
				<td><textarea rows="20" cols="50" id="i_goods_contents" name="goods_contents"></textarea></td>
			</tr>
			<tr>
				<td>가격 등록</td>
				<td><input type="text" id="i_goods_price" name="goods_price"></td>
			</tr>
			<tr>
				<td>카테고리 선택</td>
				<td>
					<select name="goods_sort">
						<option value="woman">여성의류</option>
						<option value="man">남성의류</option>
						<option value="stuff">패션잡화</option>
						<option value="digital">디지털/가전</option>
						<option value="furniture">가구/소품</option>
						<option value="house">생활용품</option>
						<option value="beauty">뷰티/미용</option>
						<option value="pet">애완용품</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>거래지역</td>
				<td>
					<select name="goods_location1" id="i_goods_location1"></select>
					<select name="goods_location2" id="i_goods_location2"></select>
				</td>
			</tr>
			<tr>
				<td>이미지 등록</td>
				<td align="left"><input type="button" id="add_btn" class="white_btn" value="파일 추가" onclick="fn_addFile()" /></td>
				<td><div id="d_file"></div></td>
			</tr>
		</table>
		<input type="button" class="btn" value="등록하기" onclick="fn_add_new_goods()"/>
	</form>
</body>
</html>