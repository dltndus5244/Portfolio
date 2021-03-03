<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="goods" value="${goodsMap.goods}" />
<c:set var="imageFileList" value="${goodsMap.imageFileList}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

	$("select[name^=goods_location1]").each(function() {
		$selsido = $(this);
		$.each(eval(area0), function() {
			if ('${goods.goods_location1}' == this) {
				$selsido.append("<option value='"+this+"' selected>"+this+"</option>");
			} else {
	   			$selsido.append("<option value='"+this+"'>"+this+"</option>");
			}
	  	})
		
	  	var area = "area"+$("option",$(this)).index($("option:selected",$(this))); 
	  	var $gugun = $(this).next();
	  	
	  	$.each(eval(area), function() {
	  		if ('${goods.goods_location2}' == this) {
	  			$gugun.append("<option value='"+this+"' selected>"+this+"</option>");
	  		} else {
	  			$gugun.append("<option value='"+this+"'>"+this+"</option>");
	  		}
   		});
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
});

window.onload = function() {
	selectBoxInit();
}

//상품 카테고리 selectBox 초기화
function selectBoxInit() {
	var s_goods_sort = document.getElementById("goods_sort");
	var option = s_goods_sort.options;
	var goods_sort = '${goods.goods_sort}';
	
	for (var i=0; i<option.length; i++) {
		if (goods_sort == option[i].value) {
			option[i].selected = true;
			break;
		}
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

//상품 이미지 삭제 함수
function deleteImage(goods_id, image_id, filename) {
	var form = document.createElement("form");
	
	var i_goods_id = document.createElement("input");
	i_goods_id.type = "hidden";
	i_goods_id.name = "goods_id";
	i_goods_id.value = goods_id;
	
	var i_image_id = document.createElement("input");
	i_image_id.type = "hidden";
	i_image_id.name = "image_id";
	i_image_id.value = image_id;
	
	var i_filename = document.createElement("input");
	i_filename.type = "hidden";
	i_filename.name = "filename";
	i_filename.value = filename;
	
	document.body.appendChild(form);
	form.appendChild(i_goods_id);
	form.appendChild(i_image_id);
	form.appendChild(i_filename);
	
	form.method = "post"
	form.action = '${contextPath}/goods/removeGoodsImageFile.do';
	form.submit();
}

var cnt = 1;
//이미지 추가하기 버튼 클릭 시 동적으로 추가해주는 함수
function fn_addFile() {
	var goods_id = "${goods.goods_id}";
	var html = "<tr>";
	html += "<td><input type='file' name='sub_image' id='sub_image"+cnt+"' onchange=readURL(this,'previewImage"+cnt+"') /></td>";
	html += "<td><img id='previewImage"+cnt+"' width=200 height=200 /></td>";
	html += "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>";
	html += "<td><input type='button' class='white_btn' value='추가' onclick=addNewImageFile('sub_image',"+goods_id+","+cnt+") /></td>";
	html += "</tr>";
	
	$("#imageTable").append(html);
	
	cnt++;
}

//상품 이미지 추가 함수
function addNewImageFile(filetype, goods_id, index) {
	var form = document.getElementById("modifyImageForm");
    var formData = new FormData(form);
    
    formData.append("goods_id", goods_id);
    formData.append("filetype", filetype);
	
    var file = document.getElementById("sub_image"+index).value;
   	if (!file) {
   		alert('파일을 첨부해주세요.');
   		return;
   	}
   	else {
   		$.ajax({
	        url: '${contextPath}/goods/addNewGoodsImage.do',
	        processData: false,
	        contentType: false,
	        enctype: 'multipart/form-data',
	        data: formData,
	        type: 'post',
	 	    success: function(result){
	 	       alert("이미지를 추가했습니다.");
	 	       location.href="${contextPath}/goods/modifyGoodsForm.do?goods_id=" + goods_id;
	 	    }
	     });
   	}
}

//상품 이미지 수정 함수
function modifyImageFile(goods_id, image_id) {
	 var form = document.getElementById("modifyImageForm");
	 var formData = new FormData(form);
	
	 formData.append("goods_id", goods_id);
	 formData.append("image_id", image_id);
	 
	 $.ajax({
	   url: '${contextPath}/goods/modifyGoodsImageFile.do',
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
</script>
</head>
<body>
	<form id="modifyGoodsForm" action="${contextPath}/goods/modifyGoodsInfo.do" method="post">
		<h3>상품 수정하기</h3>
		<table>
			<tr>
				<td>제목</td>
				<td><input type="text" size="51" name="goods_title" value="${goods.goods_title}"></td>
			</tr>
			<tr>
				<td>내용</td>
				<td><textarea rows="20" cols="50" name="goods_contents">${goods.goods_contents}</textarea></td>
			</tr>
			<tr>
				<td>가격</td>
				<td><input type="text" name="goods_price" value="${goods.goods_price}"></td>
			</tr>
			<tr>
				<td>상품상태</td>
				<td>
					<select id="goods_status" name="goods_status">
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
			</tr>
			<tr>
				<td>카테고리 선택</td>
				<td>
					<select id="goods_sort" name="goods_sort">
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
					<select name="goods_location1" id="goods_location1"></select>
					<select name="goods_location2" id="goods_location2"></select>
				</td>
			</tr>
		</table>
		<input type="hidden" name="goods_id" value="${goods.goods_id}" />
		<input type="submit" class="btn" value="수정하기" />
	</form>
	<br>
	
	<form id="modifyImageForm" method="post" enctype="multipart/form-data">
		<h3>상품이미지</h3>
		<table>
			<tbody id="imageTable">
				<c:forEach var="item" items="${imageFileList}" varStatus="itemNum">
					<tr>
						<c:choose>
							<c:when test="${item.filetype == 'main_image'}">
								<td>
									<input type="file" name="main_image" onChange="readURL(this, 'preview${itemNum.count}')"/>
								</td>
								<td>
									<img id="preview${itemNum.count}" width="200" height="200" src="${contextPath}/file/downloadGoodsImage_s3.do?goods_id=${item.goods_id}&fileName=${item.filename}">
								</td>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="button" class="white_btn" value="수정" onclick="modifyImageFile(${item.goods_id}, ${item.image_id})"/>
								</td>	
							</c:when>
							<c:otherwise>
								<td>
									<input type="file" name="sub_image${itemNum.count}" onChange="readURL(this, 'preview${itemNum.count}')"/>
								</td>
								<td>
									<img id="preview${itemNum.count}" width="200" height="200" src="${contextPath}/file/downloadGoodsImage_s3.do?goods_id=${item.goods_id}&fileName=${item.filename}">
								</td>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="button" class="white_btn" value="수정" onclick="modifyImageFile(${item.goods_id}, ${item.image_id})"/>
									<input type="button" style="color:red" class="white_btn" value="삭제" onclick="deleteImage(${item.goods_id}, ${item.image_id},'${item.filename}')"/>
								</td>	
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input type="button" value="이미지파일추가하기"  onClick="fn_addFile()"/>
	</form>
</body>
</body>
</html>