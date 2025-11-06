[README.md](https://github.com/user-attachments/files/23382249/README.md)
<img width="800"  alt="pin-up_logo" src="https://github.com/user-attachments/assets/944556e2-658f-44eb-89f9-502f11709006" />

### 🧭 프로젝트 개요 (Overview)
- 프로젝트명: 핀-업 (Pin-Up)
- 슬로건: 여행을 기록하고, 영토를 넓히며, 전국을 점령하라!
  
  
<br>
핀업(Pin-Up)은 사용자가 실제 방문한 지역을 **지도 위의 “영토”로 점령**하여 자신만의 여행 지도를 완성하고,<br>
랭킹을 통해 **다른 사용자와 “경쟁”할** 수 있는 새로운 형태의 **여행 기록 서비스**입니다.

<br>

### 💡 주요 목표

- 국내 여행을 **데이터화 + 시각화**
- 실제 방문 지역을 기반으로 **영토 확장 시스템** 구현
- 게임 요소(랭킹, 성취)를 통해 지속적 서비스 이용 유도
- 백엔드 중심으로 **API, 인증, 지도 데이터, 통계 처리** 경험 강화


## 👥 팀 구성 (Contributors)

<table style="width: 100%; text-align: center;">
<tr>
<td align="center"> <a href="https://github.com/silentanderson">김동리</a></td>
<td align="center"> <a href="https://github.com/okok02110211">이상도</a></td>
<td align="center"> <a href="https://github.com/sangdo19909-code">신광운</a></td>
<td align="center"> <a href="https://github.com/waifuser">박연수</a></td>
<td align="center"> <a href="https://github.com/Zane-Jeong">최지원</a></td>
<td align="center"> <a href="https://github.com/Hyennnn">김현수</a></td>
</tr>
<tr>
<td align="center">
    <img width="127" height="171" alt="Image" src="https://github.com/user-attachments/assets/5bc3ea77-f501-4722-afb1-f17002e699ad" />
</td>
<td align="center">
   <img width="127" height="171" alt="Image" src="https://github.com/user-attachments/assets/d9e84617-492b-492f-b7cb-50d9ee5c4cb2" />
</td>
<td align="center">
     <img width="127" height="171" alt="Image" src="https://github.com/user-attachments/assets/c56421c4-0237-4067-b76d-240dc4997b8e" />
</td>
<td align="center">
     <img width="127" height="171" alt="Image" src="https://github.com/user-attachments/assets/bb003998-97e9-4958-8a93-220c40c1f0fc" />
</td>
<td align="center">
     <img width="127" height="171" alt="Image" src="https://github.com/user-attachments/assets/05baf95b-9928-4194-80d6-8c90dca00f97" />
</td>
<td align="center">
     <img width="127" height="171" alt="Image" src="https://github.com/user-attachments/assets/4d675b14-35a9-448d-a6ee-a962efb101ff" />
</td>
</tr>
</table>

## ✨ 주요 기능 (Features)
###  Auth (인증)
- JWT 기반 로그인 및 회원 인증  
- Access / Refresh Token 발급 및 검증  
- 회원가입, 로그인, 로그아웃, 토큰 재발급 기능  

###  Config (환경설정)
- 전역 설정 및 공통 예외 처리  
- CORS, Security, Swagger 등 설정 관리  

###  Conquer (영토 점령)
- 위치 기반으로 여행한 지역을 점령 등록  
- 지도 좌표와 연동된 점령 데이터 저장  
- 지역별 점령 이력 및 시각화 기능  

###  Feed (피드)
- 유저별 여행 기록 피드 생성  
- 게시글 작성 / 수정 / 삭제  
- 좋아요, 댓글 등 소셜 상호작용 기능  

###  Home (홈)
- 서비스 메인 화면 구성  
- 요약 통계, 최근 점령 지역, 공지 요약 표시  

###  Member (회원)
- 회원 프로필, 닉네임, 포인트 관리  
- 활동 통계 및 등급 표시  
- 내 점령 지역 및 구매 아이템 조회  

###  Notice (공지)
- 공지사항 등록 / 수정 / 조회  
- 이벤트 및 업데이트 안내  

###  Notification (알림)
- 점령/댓글/좋아요 등 실시간 알림 전송  
- 읽음/미확인 상태 관리  

###  Ranking (랭킹)
- 점령 지역 수 및 활동 점수 기반 순위 집계  
- 지역별 / 전국 랭킹 제공  

###  Report (리포트)
- 여행 통계 리포트 자동 생성  
- 방문 횟수, 점령 비율, 지도 시각화  

###  Store (상점)
- 아이템 목록 및 카테고리별 조회  
- 아이템 구매 / 장착 / 해제 기능  
- 한정판, 이벤트 아이템 관리

## 🏗️ 프로젝트 아키텍처 (Architecture)
<br>

| 구분 | 사용 기술 |
| --- | --- |
| **Backend** | Spring Boot, Spring Security, JPA (Hibernate) |
| **Database** | MariaDB |
| **Frontend** | Vue.js |
| **Map / Location** | Kakao Map API, Google Maps, GeoJSON |
| **Auth** | JWT, OAuth2 (Kakao / Google) |
<br>

## 🧩 ERD 및 주요 도메인 (Domain & ERD)

### 요구사항명세서
<img width="1392" height="721" alt="Image" src="https://github.com/user-attachments/assets/c61aa2b7-18f1-4e8a-91ac-4a8b60e1a296" />

<br>

### DDD
<img width="1040" height="871" alt="Image" src="https://github.com/user-attachments/assets/4001b209-5d7c-4f35-bdf0-a9cd8f80f20e" />

<br>

### ERD

<img width="1151" height="636" alt="Image" src="https://github.com/user-attachments/assets/2410224e-c427-4046-9dce-74ab9bb9310a" />

## 🔗 API 구조 (API Overview)


<img width="734" height="852" alt="Image" src="https://github.com/user-attachments/assets/104da3f0-619e-4a9d-ae65-ad06ad149a6f" />

## 📁 폴더 구조 (Directory Structure)

📦 pinup-backend<br>
┃ ┃ ┃ ┗ 📂 pinup <br>
┃ ┃ ┃ ┃ ┗ 📂 backend <br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 auth # 인증 (JWT, 로그인/회원가입)<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 config # 전역 설정 (Security, CORS, Swagger 등)<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 conquer # 영토 점령 기능 (좌표, 지도 연동)<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 feed # 여행 피드 및 게시글 관리<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 home # 홈 화면 요약 데이터<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 member # 회원 관리 (프로필, 포인트, 활동 통계)<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 notice # 공지사항 관리<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 notification # 알림 기능<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 ranking # 랭킹 시스템<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 report # 리포트/통계 모듈<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 store # 상점 및 인벤토리 아이템 관리<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 PinupApplication.java<br>
┃ ┃ ┣ 📂 resources<br>
┃ ┃ ┃ ┣ 📂 mapper # MyBatis or Query Mapper (선택적)<br>
┃ ┃ ┃ ┣ 📂 pinup # 프로젝트 리소스 (이미지, 아이콘 등)<br>
┃ ┃ ┃ ┣ 📂 static # 정적 파일 (CSS, JS 등)<br>
┃ ┃ ┃ ┣ 📂 templates # Thymeleaf 템플릿 (if used)<br>
┃ ┃ ┃ ┣ 📜 application.yml # 환경 설정 파일<br>
┃ ┃ ┃ ┣ 📜 dummy_feedtest.sql # 피드 테스트용 SQL 데이터<br>
┃ ┃ ┃ ┗ 📜 dummy_regions.sql # 지역 더미 데이터<br>
┃ ┗ 📂 test # 테스트 코드<br>

## 🚀 실행 및 테스트 방법 (Run & Test)

## 🧪 Postman / Swagger

## 🤝 기여 가이드 (Contribution Guide)
