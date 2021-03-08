# USED MARKET | 중고마켓

프로젝트 소개
---
사람들이 중고 물품을 사고 파는 중고 거래 마켓에 서로 대화를 하기 위한 1:1 채팅 기능을 추가한 중고마켓 프로젝트입니다. 

개발환경
---
|  <center>Tool</center> |  <center>Version</center> |
|:--------|:--------:|
|JAVA | <center>1.8 </center> |
|SPRING | <center>4.3.13 RELEASE </center> |
|ORACLE | <center>19c </center> |
|AWS EC2 | <center>- </center> |
|AWS RDS | <center>- </center> |
|AWS S3 | <center>- </center> |

주요 기능
---
로그인/회원가입
---
- 사용자 아이디와 비밀번호를 이용해 로그인이 가능합니다. 아이디 또는 비밀번호가 일치하지 않을 경우 alert창을 띄워줍니다.
<img src="https://user-images.githubusercontent.com/32029329/110285284-5c43ec80-8026-11eb-9667-7872b3a99107.JPG" width="70%" height="70%">

- 작성한 이메일로 인증번호를 전송하고 인증번호를 맞게 입력하면 회원가입이 가능합니다. 
<img src="https://user-images.githubusercontent.com/32029329/110285388-8c8b8b00-8026-11eb-96ea-c5d03cac3ebf.JPG" width="70%" height="70%">
<img src="https://user-images.githubusercontent.com/32029329/110285393-8dbcb800-8026-11eb-80ae-39c138c3003a.JPG" width="50%" height="50%">

상품 등록/수정/삭제
---
-	글 제목, 내용, 가격, 카테고리, 거래지역과 이미지를 첨부하여 상품을 등록할 수 있습니다. 이미지는 최대 5장까지 첨부 가능합니다. 
<img src="https://user-images.githubusercontent.com/32029329/110285395-8dbcb800-8026-11eb-95df-d74fee0f4a65.JPG" width="70%" height="70%">

-	마이페이지 화면에서 상품의 상태(판매중,예약중,거래완료)를 변경할 수도 있고 상품 수정창에서 모든 상품 정보를 수정할 수 있습니다. 이미지 파일의 추가, 수정, 삭제도 가능합니다.
-	마이페이지 화면에서 상품을 삭제할 수 있습니다. 상품 삭제 시 관련된 채팅과 메시지도 같이 삭제되고 찜 목록에서도 삭제됩니다. 
<img src="https://user-images.githubusercontent.com/32029329/110285398-8e554e80-8026-11eb-85da-15c45835943d.JPG" width="70%" height="70%">
<img src="https://user-images.githubusercontent.com/32029329/110285401-8e554e80-8026-11eb-83e5-68382ffe79f9.jpg" width="70%" height="70%">


상품 상세보기
---
-	자세한 상품 정보와 상품을 판매하는 마켓 정보를 확인할 수 있습니다. 
<img src="https://user-images.githubusercontent.com/32029329/110285403-8eede500-8026-11eb-842a-da14f020b0d4.jpg" width="70%" height="70%">

상품 검색/카테고리
---
-	상품 제목의 키워드를 입력하여 원하는 상품을 검색할 수 있습니다. 최신순/저가순/고가순으로 상품 정렬이 가능합니다.
<img src="https://user-images.githubusercontent.com/32029329/110285404-8eede500-8026-11eb-81db-7d37a8284c43.jpg" width="70%" height="70%">

-	카테고리를 클릭하여 원하는 카테고리의 상품만 확인할 수 있습니다. 
<img src="https://user-images.githubusercontent.com/32029329/110285405-8f867b80-8026-11eb-8b00-987f7ec66a82.jpg" width="70%" height="70%">

상품 찜하기
---
-	상품 상세 페이지에서 찜 버튼을 클릭하여 원하는 상품을 찜 할 수 있습니다. 찜 한 상품은 마이페이지의 찜 목록에서 확인 할 수 있습니다.
-	또한 찜 버튼을 다시 누르면 찜이 해제되고, 찜 목록에서도 삭제됩니다.
<img src="https://user-images.githubusercontent.com/32029329/110285407-901f1200-8026-11eb-89a9-cb5277add561.jpg" width="70%" height="70%">

1:1 채팅
---
-	채팅 메인 화면에서 자신의 채팅 목록을 확인할 수 있습니다. 또한 읽지 않은 메시지의 개수도 확인할 수 있습니다.
-	채팅방 클릭 시 상품의 정보와 채팅 메시지를 확인할 수 있고, 채팅을 보낼 수 있습니다. 
<img src="https://user-images.githubusercontent.com/32029329/110289972-81882900-802d-11eb-8d19-5d095e75781b.jpg" width="70%" height="70%">

-	이 때, 상대방이 채팅방에 접속중이 아니라면 메시지가 도착했다는 알림을 표시해줍니다.
<img src="https://user-images.githubusercontent.com/32029329/110285412-90b7a880-8026-11eb-93e9-77b536edc93f.jpg" width="70%" height="70%">

-	상품 상세 페이지에서 채팅 버튼을 누르면 새로운 채팅방을 생성할 수 있고 메시지를 보낼 수 있습니다. 
<img src="https://user-images.githubusercontent.com/32029329/110285413-91503f00-8026-11eb-87a9-e8e025893653.jpg" width="70%" height="70%">

구매자 선택
---
-	마이페이지에서 상품의 상태를 ‘거래완료’로 변경하면 구매자 선택 창이 나타납니다. 해당 상품에 관해서 채팅을 한 회원의 아이디를 표시해주고, 이 중 한 명을 구매자로 선택할 수 있습니다.
<img src="https://user-images.githubusercontent.com/32029329/110285415-91503f00-8026-11eb-8c5d-37cfa7cfb953.jpg" width="70%" height="70%">

리뷰 작성
---
-	상품을 구매한 회원의 마이페이지의 구매 목록에 구매한 상품이 표시되고, 리뷰 작성이 가능합니다.
<img src="https://user-images.githubusercontent.com/32029329/110285417-91e8d580-8026-11eb-8e89-73f0df786576.jpg" width="70%" height="70%">

-	구매에 대한 별점과 코멘트를 작성할 수 있고, 이 리뷰는 판매자의 마켓 리뷰 화면에 표시됩니다.
<img src="https://user-images.githubusercontent.com/32029329/110285418-91e8d580-8026-11eb-9026-3b3b1738878c.jpg" width="70%" height="70%">

이 외에도 회원정보 수정, 마켓 이미지 등록/수정, 마켓 이름 수정이 가능합니다. 
