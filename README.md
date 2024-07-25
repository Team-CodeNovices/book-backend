## 1. 프로젝트 소개

<div align="center">
Bookey는 사용자가 책 제목 또는 내용을 자세히 기억하지 못할 때도 키워드로 검색할 수 있는 독특한 독서 및 독후감 공유 플랫폼입니다. 
사용자들은 SNS 형식으로 독후감을 작성하고 다른 사람들과 책에 관한 생각들을 나눌 수 있습니다.
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
| <img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=black"> <img src="	https://img.shields.io/badge/Flutter-02569B?style=for-the-badge&logo=flutter&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E.svg?style=flat-square&logo=JavaScript&logoColor=black"> | <img src="https://img.shields.io/badge/REST API-000000?style=flat-square&logo=logoColor=white"> <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> | <img src="https://img.shields.io/badge/Amazon_AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white"> | <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"> <img src="https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=Discord&logoColor=white"> | <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"> | 


# 2. 팀원 소개
|조재근|서민희|박두환|이승준|박상현|
|---|---|---|---|---|
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

### 박두환 
## 네이버 api 도입 및 크롤링

>책을 추천하기 위해서는 다양한 책 정보가 필요했습니다. 초기에는 알라딘 API를 사용했지만, 정보를 얻었지만 부족한 부분이 있었습니다. 그래서 네이버 API로 전환하였지만. 
이에 더 많은 정보를 확보하기 위해 크롤링을 활용하는 것이 필요하다고 판단했습니다. 그래서 **네이버 API를 통해 1차적으로 정보를 얻고 네이버 크롤링을 통해 부족한 정보를 보완하였습니다.**
이후 스케줄러를 활용하여 정기적으로 데이터베이스를 업데이트함으로써 지속적으로 데이터를 확보하는 방안을 도입했습니다.

<details>
<summary>네이버 api 와 크롤링 작업 코드</summary>
<div markdown="1">
  
  ```java
    //네이버 api Data 받아서 업데이트 처리하는 메소드
    @Scheduled(cron = "0 0/30 * * * *") // 매 30분마다 반복
    public void updateBooksFromApi() throws IOException {
        if (isServer) {     //서버에서 일때만 스케줄러가 사용할수 있게
            List<OurBookDto> nullList = dao.selectnull();
            if (!nullList.isEmpty()) {
                for (OurBookDto bookDto : nullList) {
                    List<OurBookDto> updatedBookDtoList = getNaverApi(bookDto.getBookname());
                    for (OurBookDto updatedBookDto : updatedBookDtoList) {
                        List<OurBookDto> infoList = getNaverCrawling(updatedBookDto.getLink());
                        if (!infoList.isEmpty()) {
                            OurBookDto infoDto = infoList.get(0);
                            OurBookDto updatedDto = new OurBookDto();
                            updatedDto.setLink(updatedBookDto.getLink());
                            updatedDto.setImage(updatedBookDto.getImage());
                            updatedDto.setBookname(bookDto.getBookname());
                            updatedDto.setAuthor(updatedBookDto.getAuthor());
                            updatedDto.setPublisher(updatedBookDto.getPublisher());
                            updatedDto.setGenre(infoDto.getGenre());
                            updatedDto.setContents(infoDto.getContents());
                            updatedDto.setBookdetail(updatedBookDto.getBookdetail());
                            updatedDto.setAuthordetail(infoDto.getAuthordetail());
                            updatedDto.setPrice(updatedBookDto.getPrice());
                            updatedDto.setWritedate(updatedBookDto.getWritedate());
                            updatedDto.setMainkeyword(null);
                            updatedDto.setAssistkeyword(null);
                            dao.updateBooksByList(Collections.singletonList(updatedDto));
                            log.info("책 정보 업데이트 완료. bookname: " + bookDto.getBookname());
                        }
                    }
                }
                log.info("업데이트 완료!!");
            } else {
                log.info("업데이트할 리스트가 없어 종료합니다.");
            }
        } else {
            log.info("서버가 아니므로 작업을 스킵합니다.");
        }
    }

    //네이버 크롤링하는 메소드
    public static List<OurBookDto> getNaverCrawling(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        List<OurBookDto> list = new ArrayList<>();

        //장르
        Element genreElement = doc.select("[class*=bookCatalogTop_category__]").last();
        String genre = (genreElement != null && !genreElement.text().isEmpty()) ? genreElement.text() : "정보 없음";
        // 목차
        Element contentsElement = doc.select("[class*=infoItem_data_text__]").last();
        String contents = (contentsElement != null && !contentsElement.text().isEmpty()) ? contentsElement.text() : "정보 없음";
        // 저자 소개
        Element authorDetailElement = doc.select("[class*=authorIntroduction_introduce_text__]").first();
        String authorDetail = (authorDetailElement != null && !authorDetailElement.text().isEmpty()) ? authorDetailElement.text() : "정보 없음";

        // OurBookDto 객체에 정보 저장
        OurBookDto bookDto = new OurBookDto();
        bookDto.setGenre(genre);
        bookDto.setContents(contents);
        bookDto.setAuthordetail(authorDetail);

        list.add(bookDto);

        return list;
    }

    //네이버 api 받아오는 메소드
    public List<OurBookDto> getNaverApi(String bookName) throws IOException {
        // 외부 API의 엔드포인트 URL
        String apiUrl = "https://openapi.naver.com/v1/search/book.json?display=1&query=" + bookName;

        // REST 요청을 보내기 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", NaverId);
        headers.set("X-Naver-Client-Secret", NaverSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API에 GET 요청을 보내고 JSON 형식으로 응답을 받아옴
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        String jsonResponse = responseEntity.getBody();

        // JSON 응답을 분석하여 필요한 필드 추출
        ObjectMapper objectMapper = new ObjectMapper();
        List<OurBookDto> bookList = new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode items = root.path("items");

        for (JsonNode item : items) {
            OurBookDto apiData = new OurBookDto();
            String link = item.path("link").asText();
            apiData.setLink(link != null && !link.isEmpty() ? link : "정보 없음");
            String image = item.path("image").asText();
            apiData.setImage(image != null && !image.isEmpty() ? image : "정보 없음");
            String author = item.path("author").asText().replace("^", ",");
            apiData.setAuthor(author != null && !author.isEmpty() ? author : "정보 없음");
            String publisher = item.path("publisher").asText();
            apiData.setPublisher(publisher != null && !publisher.isEmpty() ? publisher : "정보 없음");
            String description = item.path("description").asText();
            apiData.setBookdetail(description != null && !description.isEmpty() ? description : "정보 없음");
            String price = item.path("discount").asText();
            apiData.setPrice(price != null && !price.isEmpty() ? price : "정보 없음");
            String pubdate = item.path("pubdate").asText();
            apiData.setWritedate(pubdate != null && !pubdate.isEmpty() ? pubdate : "정보 없음");

            bookList.add(apiData);
        }
        return bookList;
    }  
```
</div>
</details>

<br />

## 사이트 크롤링 및 비동기적 데이터베이스 저장 작업

>우리 Bookey 사이트는 메인 페이지에서 각 사이트별 상위 50위 책을 보여주는데, 이를 위해 사이트에서 책 이름만을 데이터베이스에 저장하고, 별도의 테이블을 만들지 않았습니다. 
대신, 메인 페이지에 접속할 때마다 해당 책 이름을 디비에 저장하는 작업을 수행했습니다. 그러나 크롤링과 데이터베이스 저장 작업을 동시에 진행하다보니 메인 페이지에서 책을 보여주는 데 
많은 시간이 소요되었습니다. 이를 해결하기 위해, 작업을 동시에 진행하는 데 필요한 시간을 단축하기 위해 스레드를 하나 더 만들고, **메소드를 비동기화하여 처리 속도를 1분에서 3초까지 단축했습니다.**
이 과정에서 **@Async 어노테이션**을 활용하여 비동기 처리를 하였습니다.

<details>
<summary>비동기 저장 작업 코드</summary>
<div markdown="1">

- 페이지에서 랭킹을 보여주면서 저장을 하기위해 **랭킹만을 반환하는 기능**과 **책을 저장 기능**이 있는 스레드를 나누어 비동기로 처리하였습니다.
  ```java
    //랭킹 반환하는 클래스
  
    //예스24 크롤링 메소드
    public List<RankingDto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<RankingDto> list = new ArrayList<>();
        int count = 0;
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
                count++;
                if (count >= 51) {
                    break;
                }
                String gdName = good.select(".gd_name").text();
                if (gdName == null || gdName.isEmpty()) {
                    gdName = "19세 도서";
                }
                String image = good.select(".lazy").attr("data-original");
                String infoAuth = good.select(".info_auth").text();
                String infoPub = good.select(".info_pub").text();
                String rank = good.select(".ico.rank").text();

                RankingDto dto = new RankingDto(
                        Integer.parseInt(rank),
                        image,
                        gdName,
                        infoAuth,
                        infoPub
                );
                list.add(dto);
            }

        }
        return list;
    }

    // yes24 50위 반환하고 전체 크롤링 하는 메소드
    public List<RankingDto> getYes24Top50() throws IOException {
        //50위까지만 처리하고 리턴
        List<RankingDto> yes24Top50 = getYes24DataNew(1, 1);

        // getYes24Another()를 비동기적으로 실행
        asyncService.getYes24Another();
        return yes24Top50;
    }
  ```

  ```java
    //저장작업을 하는 클래스

    //yes24 전체데이터 가져온 후 insert 하는 메소드(비동기)
    @Async
    public void getYes24Another() throws IOException {
        List<RankingDto> list = getYes24DataNew(1, 9);
        insertYes24Data(list);
    }

    //에스 24 크롤링 하는 메소드
    public static List<RankingDto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<RankingDto> list = new ArrayList<>();
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
                String gdName = good.select(".gd_name").text().split("\\(")[0].trim();
                if (gdName == null || gdName.isEmpty()) {
                    gdName = "19세 도서";
                }

                RankingDto dto = RankingDto.rankingDtoBuilder().bookname(gdName).build();
                list.add(dto);
            }
        }
        return list;
    }

    //ourbook에 없는 데이터 insert 하는 메소드(yes24)
    public void insertYes24Data(List<RankingDto> yes24) throws IOException {
        List<OurBookDto> existBooks = dao.select();
        List<OurBookDto> list2 = new ArrayList<>();

        for (RankingDto yes24Dto : yes24) {

            String finalBookNameText = yes24Dto.getBookname();
            boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().replaceAll("\\s", "").equals(finalBookNameText.replaceAll("\\s", "")));
            if (!exist) {
                OurBookDto dto2 = OurBookDto.ourBookDtoBuilder().bookname(finalBookNameText).build();
                list2.add(dto2);
            }
        }
        // 여러번 insert 되는 문제를 막기위해 비동기로 진행
        if (!list2.isEmpty()) {
            log.info("insert 시작 (Yes24)");
            dao.insert(list2);
            log.info("새로운 Yes24 책 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }
    }
  ```
  
</div>
</details>

<br />

## ChatGPT 기반의 책 추천 시스템 개발

> 우리 Bookey 사이트는 사용자에게 더 나은 책을 추천하기 위해 다양한 방법을 모색했습니다. 그 중 하나는 각 사이트별 인기도서가 트랜드를 반영한다는 것을 바탕으로 **chat gpt api와 chat gpt 어시스턴트 api를 활용하기로 하였습니다.**
우선, 책 이름을 보고 나오는 주요 키워드를 ChatGPT를 활용하여 추출하여 mainkeyword에 저장했습니다. 또한, 책의 상세내용과 몇몇 데이터를 ChatGPT 어시스턴트를 통해 교육시켜 나온 응답을 assistant keyword에 담았습니다.
>**이를 토대로 각 사이트의 상위 1~10위 책의 정보를 받아와, 우리 디비에 있는 키워드를 활용하여 새로운 추천 책을 선별하는 방식을 도입했습니다.**
> 이를 통해 베스트셀러 추천을 제공하고, 에디터 추천은 인기 있는 책보다는 상위 11위부터 50위 사이의 책을 랜덤으로 추출하여 디비에 있는 키워드를 기반으로 추천했습니다.
> 이를 통해 사용자가 더 다양한 추천을 받을 수 있게끔 하였습니다.

<details>
<summary>추천 책 작업 코드</summary>
<div markdown="1">
  
  ```java

    //베스트,에디터 추천 도서 불러오기
    public List<RecommendBooksDto> randomBooksFromTopN(int start, int end, int pick) throws IOException {
        List<RecommendBooksDto> recommendedBooks = new ArrayList<>();
        List<RankingDto> yes24Top50 = yes24.getYes24DataNew(1,1);

        if (!yes24Top50.isEmpty()) {

            List<RankingDto> topNBooks = yes24Top50.subList(start, end);
            Collections.shuffle(topNBooks);

            for (int i = 0; i < Math.min(pick, topNBooks.size()); i++) {
                RankingDto rankingDto = topNBooks.get(i);
                RecommendBooksDto recommendBooksDto = new RecommendBooksDto();
                String genre = dao.selectAuthor(rankingDto.getBookname());

                recommendBooksDto.setImage(rankingDto.getImage());
                recommendBooksDto.setGenre(genre);
                recommendBooksDto.setBookname(rankingDto.getBookname());
                recommendBooksDto.setAuthor(rankingDto.getAuthor());

                recommendedBooks.add(recommendBooksDto);
            }
        }
        return recommendedBooks;
    }

  ```
</div>
</details>

<br />

### 이승준  
## 검색키워드 고도화

>저는 검색 고도화 작업을 수행하는 중에, ChatGPT Assistant를 이용하여 문제 해결을 시도했습니다. 하지만 교육을 거치더라도 Assistant의 답변이 예상과 다를 때가 종종 있었습니다. 
이에 대한 대응책으로, 질문을 분석하고 관련된 책 자료에 대한 키워드를 추출하여 데이터베이스(DB)에 저장하는 방법을 선택했습니다. 이를 통해 나중에 유사한 질문이 들어올 때 
정확한 답변을 제공할 수 있도록 시스템을 개선하고자 했습니다.

<details>
<summary>AssistantApi 작업 코드</summary>
<div markdown="1">

```java
// Assistant 메시지 보내기 및 실행하는 메소드
    public Map<String, Object> sendMessage(String bookname, String bookdetail) {

            String bookDetailMessage = "\n\nBookdetail:\n" +
                    "Book Name: " + bookname + "\n" +
                    "bookdetail: " + bookdetail + "\n" +
                    "내가 너에게 준 bookname하고 bookdetail에서 준 정보로 단어로된 키워드 5개만 뽑아줘 그리고 공백 없이 키워드만 보여줘"
                    ;

            HttpHeaders headers = createHeaders();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("role", "user");
            requestBody.put("content", bookDetailMessage);

            String messageUrl = threadurl + "/" + lastThreadId + "/messages";
            String assistantRunUrl = threadurl + "/" + lastThreadId + "/runs";

            try {
                // 메시지 전송
                ResponseEntity<String> messageResponseEntity = restTemplate.exchange(messageUrl, HttpMethod.POST,
                        new HttpEntity<>(requestBody, headers), String.class);
                String messageResponseBody = messageResponseEntity.getBody();

                // Assistant 실행
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(assistantRunUrl,
                        new HttpEntity<>(Collections.singletonMap("assistant_id", assistantId), headers), Map.class);
                Map<String, String> responseBody = responseEntity.getBody();
                String runId = responseBody.get("id");
                System.out.println("Assistant run started successfully for thread ID: " + lastThreadId);
                System.out.println("Run ID: " + runId);

                return Map.of("status", "Success", "responseBody", messageResponseBody);
            } catch (HttpClientErrorException e) {
                return Map.of("status", "Error");
            } catch (Exception e) {
                log.error("Exception occurred: {}", e.getMessage());
                return Map.of("status", "Error");
            }
        }

 // 키워드 업데이트
    // @Scheduled(cron = "0 */2 * * * *")
    public void updateAssistKeywords() {
        if (!isServer) {
            // 서버 환경에서는 스케줄러를 동작시킵니다.
            int count = 0;
            List<OurBookDto> books = dao.assistnull();
            if (books.isEmpty()) {
                log.info("No books found with null assist keyword.");
                return;
            }

            for (OurBookDto book : books) {
                if (count >= 1) {
                    log.info("Reached the limit of 1 updates.");
                    break;
                }

                try {
                    sendMessage(book.getBookname(), book.getBookdetail());
                    Thread.sleep(10000);
                    OpenAIResponse response = getLastMessage();
                    if (response != null && response.getText() != null && !response.getText().isEmpty()) {
                        book.setAssistkeyword(response.getText());
                        dao.updateAssistKeyword(Collections.singletonList(book));
                        log.info("Updated assist keyword for book: {}", book.getBookname());
                        count++;
                    } else {
                        log.error("Failed to retrieve message from OpenAI.");
                    }
                } catch (Exception e) {
                    log.error("Exception occurred while updating assist keyword: {}", e.getMessage());
                }
            }
        } else {
            // 로컬 환경에서는 스케줄러를 비활성화합니다.
            log.info("Scheduler is disabled in local environment.");
            return;
        }
    }
```
</div>
</details>
</div>
</details>

<br />

### 박상현  
## 키워드 기능

> 책을 추천받고 비슷한 장르물에 책을 더 보고싶을 때 키워드를 검색하면 같은 장르의 책을 보여주도록 기능을 만들었습니다.
<details>
<summary>AssistantApi 작업 코드</summary>
<div markdown="1">

```java
// Assistant 메시지 보내기 및 실행하는 메소드
    public Map<String, Object> sendMessage(String bookname, String bookdetail) {

            String bookDetailMessage = "\n\nBookdetail:\n" +
                    "Book Name: " + bookname + "\n" +
                    "bookdetail: " + bookdetail + "\n" +
                    "내가 너에게 준 bookname하고 bookdetail에서 준 정보로 단어로된 키워드 5개만 뽑아줘 그리고 공백 없이 키워드만 보여줘"
                    ;

            HttpHeaders headers = createHeaders();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("role", "user");
            requestBody.put("content", bookDetailMessage);

            String messageUrl = threadurl + "/" + lastThreadId + "/messages";
            String assistantRunUrl = threadurl + "/" + lastThreadId + "/runs";

            try {
                // 메시지 전송
                ResponseEntity<String> messageResponseEntity = restTemplate.exchange(messageUrl, HttpMethod.POST,
                        new HttpEntity<>(requestBody, headers), String.class);
                String messageResponseBody = messageResponseEntity.getBody();

                // Assistant 실행
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(assistantRunUrl,
                        new HttpEntity<>(Collections.singletonMap("assistant_id", assistantId), headers), Map.class);
                Map<String, String> responseBody = responseEntity.getBody();
                String runId = responseBody.get("id");
                System.out.println("Assistant run started successfully for thread ID: " + lastThreadId);
                System.out.println("Run ID: " + runId);

                return Map.of("status", "Success", "responseBody", messageResponseBody);
            } catch (HttpClientErrorException e) {
                return Map.of("status", "Error");
            } catch (Exception e) {
                log.error("Exception occurred: {}", e.getMessage());
                return Map.of("status", "Error");
            }
        }

 // 키워드 업데이트
    // @Scheduled(cron = "0 */2 * * * *")
    public void updateAssistKeywords() {
        if (!isServer) {
            // 서버 환경에서는 스케줄러를 동작시킵니다.
            int count = 0;
            List<OurBookDto> books = dao.assistnull();
            if (books.isEmpty()) {
                log.info("No books found with null assist keyword.");
                return;
            }

            for (OurBookDto book : books) {
                if (count >= 1) {
                    log.info("Reached the limit of 1 updates.");
                    break;
                }

                try {
                    sendMessage(book.getBookname(), book.getBookdetail());
                    Thread.sleep(10000);
                    OpenAIResponse response = getLastMessage();
                    if (response != null && response.getText() != null && !response.getText().isEmpty()) {
                        book.setAssistkeyword(response.getText());
                        dao.updateAssistKeyword(Collections.singletonList(book));
                        log.info("Updated assist keyword for book: {}", book.getBookname());
                        count++;
                    } else {
                        log.error("Failed to retrieve message from OpenAI.");
                    }
                } catch (Exception e) {
                    log.error("Exception occurred while updating assist keyword: {}", e.getMessage());
                }
            }
        } else {
            // 로컬 환경에서는 스케줄러를 비활성화합니다.
            log.info("Scheduler is disabled in local environment.");
            return;
        }
    }
```
</div>
</details>
