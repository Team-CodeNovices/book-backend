## 1. 프로젝트 소개

<div align="center">
Bookey는 사용자가 책 제목 또는 내용을 자세히 기억하지 못할 때도 키워드로 검색할 수 있는 독특한 독서 및 독후감 공유 플랫폼입니다. 사용자들은 SNS 형식으로 독후감을 작성하고 다른 사람들과 책에 관한 생각들을 나눌 수 있습니다.
</div>
<br>


## 1.1 프로젝트 명: BOOKEY

> **🔗 배포 URL:** [BOOKEY](https://book.codenovices.store) <br />
**🔗 테스트 계정:** bookey@test.com / 1234
```jsx
📕  **독후감 기능** : 독서기록 서비스와 SNS가 결합된 서비스를 제공합니다.

📗  **키워드 검색 기능** : 내용은 알고 있지만 책의 이름이 기억이 나지 않을 경우 유사한 단어를 추적해 책을 찾아줍니다.

📒  **찜, 좋아요 기능** : 찜을 통해 좋아하는 책을 담을수 있고, 원하는 게시물에 '좋아요'를 누르거나 댓글을 등록할 수 있습니다.

📙  **도서정보 제공** : 독서기록뿐만 아니라, 출판사, 저자 등 책의 기본 정보를 볼 수 있는 기능이 있습니다.

📒  **추천 도서 기능** : 최신 트렌드를 따라가는 베스트셀러와 다양한 장르를 선보이는 에디터 추천 책을 소개하여 사용자들이 검색으로 소비하는 불필요한 시간을 절약할 수 있습니다. 
```
<br>

## 1.2 개발 환경

### ⚙️   사용기술 
| FrontEnd | BackEnd | Server | 협업방식 | DB |
| :----: | :----: | :----: | :----: |:----: |
| <img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=black"> <img src="	https://img.shields.io/badge/Flutter-02569B?style=for-the-badge&logo=flutter&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E.svg?style=flat-square&logo=JavaScript&logoColor=black"> | <img src="https://img.shields.io/badge/REST API-000000?style=flat-square&logo=logoColor=white"> <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> | <img src="https://img.shields.io/badge/Amazon_AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white"> | <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"> <img src="https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=Discord&logoColor=white"> | <img src="	https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"> | 


# 2. 팀원 소개
|조재근|서민희|박두환|이승준|박상현|
|---|---|---|---|---|
|![image]()|![image]()|![image]()|![image]()|![image]()|
|<a href="https://github.com/M0nk2y"><img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/></a>|<a href="https://github.com/plant0303"><img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/></a>|<a href="https://github.com/duhwan05"><img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/></a>|<a href="https://github.com/seung-jun2"><img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/></a>|<a href="https://github.com/psh980121"><img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/></a>|
|Server|Frontend|Backend|Backend|Backend|

## 2-1. 기능구현 및 역할 분담

| 멤버 | ⚒️ 기능구현 |
|---|---|
| 조재근 | - 서버관리 및 DB관리 |
| 서민희 | - 프론트엔드 git 주소 https://github.com/Team-CodeNovices/book-frontend |
| 박두환 | - 로그인(JWT활용)<br> - 회원가입<br> -마이페이지 CRUD<br>  - yes24 랭킹 크롤링<br> - 네이버api 및 ChatgptApi활용(데이터수집)<br> - 추천책기능(베스트셀러 및 에디터 추천)<br> - 도서 찜 기능<br> - 인증 및 권한 관리(spring security)<br> - Api 문서화기능(Swagger) https://book-backend.codenovices.store/swagger-ui/#/ |
| 이승준 | - ChatgptAssistantapi 활용(데이터 수집)<br> - S3서버 이미지 업로드 및 삭제<br> - 인터파크도서 랭킹 크롤링<br> - 독후감 댓글 CRUD  |
| 박상현 | - 알라딘 랭킹 크롤링<br> - 검색기능<br> - 독후감CRUD<br> - 독후감 좋아요기능<br> - 책상세페이지<br> |


<br />

# 3. 문제해결

| 박두환 | - 크롤링 및 api, 크롤링을 비동기로 실행, 추천책 구상 
| 이승준 | - 검색고도화 작업을 하던 중 어떻게 해야할지 고민하다가 ChatgapAssistant가 있다는 정보를 들어서 Aisstant를 교육하면 고도화 작업이 되지 않을까 싶어서 Aisstant를 쓰게 되었습니다. api를 활용해서 Assistant에게 
교육을 하였지만 교육한내용이 오래가지 않아 계속 질문을 하였을 때 다른답변이 나온다는걸 알고 질문을 가공해서 원하는 대답을 끄집어 내어서 책에대한 키워드들을 Db에 저장하였습니다.
| 박상현 | -

### 키워드 검색 고도화

