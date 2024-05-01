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

## 박두환 
## 네이버 api 도입 및 크롤링

>책을 추천하기 위해 다양한 정보가 필요하다는 문제에 직면하며, 초기에는 API를 통해 정보를 얻었지만 부족한 부분이 있었습니다. 
이에 더 많은 정보를 확보하기 위해 크롤링을 활용하는 것이 필요하다고 판단했습니다. 이러한 문제를 해결하기 위해 크롤링을 통해 부족한 정보를 보완하고, 
스케줄러를 활용하여 정기적으로 데이터베이스를 업데이트함으로써 지속적으로 데이터를 확보하는 방안을 도입했습니다.


## 사이트 크롤링 및 비동기적 데이터베이스 저장 작업.

>우리 Bookey 사이트는 메인 페이지에서 각 사이트별 상위 50위 책을 보여주는데, 이를 위해 사이트에서 책 이름만을 데이터베이스에 저장하고, 별도의 테이블을 만들지 않았습니다. 
대신, 메인 페이지에 접속할 때마다 해당 책 이름을 디비에 저장하는 작업을 수행했습니다. 그러나 크롤링과 데이터베이스 저장 작업을 동시에 진행하다보니 메인 페이지에서 책을 보여주는 데 
많은 시간이 소요되었습니다. 이를 해결하기 위해, 작업을 동시에 진행하는 데 필요한 시간을 단축하기 위해 스레드를 하나 더 만들고, 메소드를 비동기화하여 처리 속도를 1분에서 3초까지 단축했습니다. 
이 과정에서 @Async 어노테이션을 활용하여 비동기 처리를 하였습니다.


## ChatGPT 기반의 책 추천 시스템 개발

>우리 Bookey 사이트는 사용자에게 더 나은 책을 추천하기 위해 다양한 방법을 모색했습니다. 그 중 하나는 각 사이트별 인기도서가 트랜드를 반영한다는 인식을 바탕으로 chat gpt api와 chat gpt 어시스턴트 api를 활용하기로 하였습니다.
우선, 책 이름을 보고 나오는 주요 키워드를 ChatGPT를 활용하여 추출하여 mainkeyword에 저장했습니다. 또한, 책의 상세내용과 몇몇 데이터를 ChatGPT 어시스턴트를 통해 교육시켜 나온 응답을 assistant keyword에 담았습니다. 이를 토대로 
각 사이트의 상위 1~10위 책의 정보를 받아와, 우리 디비에 있는 키워드를 활용하여 새로운 추천 책을 선별하는 방식을 도입했습니다. 이를 통해 베스트셀러 추천을 제공하고, 에디터 추천은 인기 있는 책보다는 
상위 11위부터 50위 사이의 책을 랜덤으로 추출하여 디비에 있는 키워드를 기반으로 추천했습니다. 이를 통해 사용자가 더 다양한 추천을 받을 수 있게끔 하였습니다.



## 이승준  
## 검색키워드 고도화

>저는 검색 고도화 작업을 수행하는 중에, ChatGPT Assistant를 이용하여 문제 해결을 시도했습니다. 하지만 교육을 거치더라도 Assistant의 답변이 예상과 다를 때가 종종 있었습니다. 
이에 대한 대응책으로, 질문을 분석하고 관련된 책 자료에 대한 키워드를 추출하여 데이터베이스(DB)에 저장하는 방법을 선택했습니다. 이를 통해 나중에 유사한 질문이 들어올 때 
정확한 답변을 제공할 수 있도록 시스템을 개선하고자 했습니다.


