### 책 [스프링 부트와 aws로 혼자 구현하는 웹 서비스] 소스를 분석 하고 있습니다.
- 기술참조 : https://github.com/kwj1270/TIL_SPRINGBOOT_WITH_AWS
- 소스참조 : https://github.com/kwj1270/freelec-springboot2-webservice
- 신규작업 : https://github.com/kimilguk/kimilguk-springboot2.git
- 인텔리J 에서 깃 암호 저장하지 않게 설정(아래) 
- https://stackoverflow.com/questions/28142361/change-remote-repository-credentials-authentication-on-intellij-idea-14
- 헤로쿠용 빌드 파일 고정함. version '1.0.3-SNAPSHOT-'+new Date().format("yyyyMMddHHmmss") 수정.
- 유튜브 기술 참조: https://www.youtube.com/playlist?list=PLqaSEyuwXkSppQAjwjXZgKkjWbFoUdNXC
- 작업기간: 20일 ( 20210730 ~ 20210823 )

### 학습목차
- [들어가며](./README/00.md)
- [01 인텔리제이로 스프링 부트 시작하기](./README/01.md)
- [02 스프링부트에서 테스트 코드 작성하기](./README/02.md)
- [03 스프링부트에서 JPA로 데이터베이스 다루기](./README/03.md)
- [04 머스테치템플릿으로 화면 구성하기](./README/04.md)
- [05 스프링시큐리티와 OAuth2.0으로 로그인](./README/05.md)

### 이 프로젝트에서 구현된 내역(아래)
- 개발툴: 인텔리J 커뮤니티, 빌드툴버전: 그래들6.7.1, 스프링부트버전: 2.4.9, 자바버전: 오픈 JDK8
- 사용된 기술: 롬복, JUnit 테스트, mustache 화면처리, 하이버네이트 스프링 JPA, H2(메모리) postgreSQL(RDBS) 데이터베이스, HikariPool(스프링부트에 내장된 기본 DB 커넥션), 스프링 시큐리티(DB) + OAuth2(네이버외부API) + 스프링세션
- 구현1: 게시판 CRUD JUnit 테스트(롬복, HikariPool 사용)
- 구현2: 게시판 CRUD + 첨부파일기능(mustache 화면처리, 하이버네이트 H2 메모리 DB 사용)
- 구현3: 외부 API 로그인(네이버 OAuth2), 스프링시큐리티 로그인(스프링세션), 관리자/사용자 권한 관리(회원관리)
- 구현4: 게시판 검색과 페이징(스프링 Page 인터페이스 사용) 처리 및 에러페이지 추가, CRUD 완료 메세지기능 추가
- 구현5: 헤로쿠 배포 및 postgreSQL 데이터베이스 연동(하이버네이트 스프링 JPA 사용)

### 스프링부트 에서 MVC는
- 참고 스프링레거시 에서 MVC는 아래와 같습니다. 비교해 보시기 바랍니다.
- 스프링레거시 예: DB테이블 -> 모델(매퍼쿼리>@Repository인터페이스=DAO클래스) -> 서비스(@Service) -> 컨트롤러클래스 -> 뷰(JSP|타임리프,타일즈템플릿)
- 모델domain(구성:@Entity클래스 와 JpaRepository인터페이스) -> 서비스(@Service) -> 컨트롤러(구성:@Controller+DTO클래스) -> 뷰(타임리프,머스테치템플릿)
- @Entity클래스 : 데이터베이스 테이블을 자동생성 및 구현메서드(@Builder인터페이스사용) 추가.
- JpaRepository인터페이스 : 형식: JpaRepository<Entity 클래스, PK 타입> 기능: CRUD기본 쿼리 자동생성.
- 서비스(@Service) : 인터페이스 없이 사용.(주, 레포지토리는 인젝션으로 사용하지 않아도 롬복 @RequiredArgsConstructor 으로 사용가능. 
- 서비스 예, private final PostsRepository postsRepository;
- DTO(Data Transfer Object)클래스 : 스프링레거시의 VO와 같이 데이터 전송 임시 저장소로 Get/Set 역할을함.
- Mustache(머스테치)템플릿 : 템플릿 코드(예, {{userName}})로 자바 변수와 객체를 사용함.(JSTL과 타임리프,타일즈 대신사용)
- 작업순서예, 1. Requet 데이터임시저장 Dto, 2. API 요청을 받을 Controller, 3.서비스로 DAO호출, 4. 도메인작업(엔티티+레포지토리인터페이스)
- mustache 템플릿은 앱을 리스타트 해야지만 적용이 됩니다.
- Lombok을 사용하면 생성자도 자동으로 생성할 수 있습니다. 
- @NoArgsConstructor 어노테이션은 파라미터가 없는 기본 생성자를 자동으로 생성해주고, 
- @AllArgsConstructor 어노테이션은 모든 필드 값을 파라미터로 받는 생성자를 자동으로 만들어줍니다. 
- *가장중요 @RequiredArgsConstructor 어노테이션은 final 이나 @NonNull 인 필드 값만 파라미터로 받는 생성자를 자동으로 만들어줍니다.
- sql 쿼리 확인하기.(아래)
- https://velog.io/@dnjscksdn98/Spring-Data-JPA-H2-Console-%EB%B0%8F-%EB%A1%9C%EA%B9%85-%EC%84%A4%EC%A0%95

### 스프링 레거시와 스프링부트의 폴더 구조차이
- 스프링레거시 폴더구조:  [pom.xml], [WEB-INF>web.xml],[WEB-INF>spring>root-context.xml,WEB-INF>appServlet>servlet-context.xml]
- 스프링레거시 폴더구조: roo최상위 > [controller, dao(src/main/reousrces/mappers쿼리), service, vo 등]
- 스프링부트 폴더구조: roo최상위 > [build.gradle], [Application.java] > [config], [domain, service, web>dto 등]

### OAuth2 로그인에 영향을 주는 파일 6개
- UserRepository > CustomOAuth2UserService(외부 API 리턴값 발생 후 세션 DB 저장) > OAuthAttributes > LoginUserArgumentResolver > SessionUser > Users(엔티티)

### 앞으로 작업 예정
- 신규 프로젝트 springboot-kimilguk 으로 생성.

### 20210827(금) 엔티티관계4
- 다중 첨부파일에서 파일 개수 2개로 수정 마무리.
- $(".file_id").children("input[name='file_id']:first").val();//first 를 숫자로 변경, 아래 get(0)
- $(".file_id").children("input[name='file_id']:last").val();//last 를 숫자로 변경, 아래 get(1)
- var fileId = $("input[name='file_id']")
- .map(function(){ return $(this).val() })
- .get(0);
- 확인 URL: http://jsfiddle.net/hy7QS/

### 20210826(목) 엔티티관계3
- 다중 첨부파일에서 파일 개수 2개로 수정.

### 20210825(수) 엔티티관계2.
- postgreSQL 데이터페이스에서도 엔티티 관계 작동되는지 확인
- JPQL(Java Persistence Query Language) : 
- 단일 첨부파일에서 다중 첨부파일로 수정.
- static 디자인 index.html 실행 확인.

### 20210824(화) 엔티티관계1.
- 게시판과 첨부파일을 엔티티로 부모-자식 관계 설정 으로 변경해서 구현.
- 기술참조: https://wordbe.tistory.com/entry/Spring-Data-JPA-Entity-%EA%B4%80%EA%B3%84-%EB%A7%A4%ED%95%91
- N:1 단방향 엔티티 ManyFile 클래스 생성 : 스프링 JPA 에서 외래키 post_id 자동 생성됨.
- ManyFile, ManyFileRepository, ManyFileDto, ManyFileService, ManyFileUtilsApi, indexController 클래스 생성.
- 머스태치 뷰 파일 수정: posts-list, post-save, post-read, post-update 수정.

### 20210823(월) 작업.
- 본인 작성한 글만 수정/삭제 가능, 관리자는 모두 가능 처리.

### 20210822(일) 작업.
- 페이징 자바쪽 좀더 복잡한 로직 처리. PostsService.java -> PostPageService.java 로 분리
- 제대로 페이징 작업 참고: https://matchless.tistory.com/31
- 머스태치 뷰단 기존 간단한 적용도 보존 하면서 그 아래 신규 페이징 뷰단 추가 OK.

### 20210821(토) 작업.
- 페이징 처리 다른 방법 적용(좀더 복잡): https://victorydntmd.tistory.com/333
- 페이징 자바쪽 좀더 복잡한 로직 처리. PostsService.java 

### 20210820(금) 작업.
- 헤로쿠에 postgreSQL DB 생성 후 연동시키기, application-db-heroku.properties 추가.
- application.db-postgres.properties 설정파일에서 초기 더미값 입력 후 업데이트만 적용 시키기 (깃 소스를 2번 배포) 아래
- #spring.jpa.hibernate.ddl-auto=create-drop 고정
- spring.jpa.hibernate.ddl-auto=update 고정
- spring.datasource.schema=classpath:import.sql 부분 주석 처리 후 배포(초기:JPA 엔티티로 테이블 자동 생성)
- spring.datasource.schema=classpath:import.sql 부분 주석 해제 후 배포(중간)
- spring.datasource.schema=classpath:import.sql 부분 주석 처리 후 배포(마지막)
- postgreSQL 에서 시퀸스 posts_id_seq 20으로 수정. simple_users_id_seq 2로 수정. (더미데이터 입력 후 라서, mysql 일때는 필요 없음.)
- 빌드툴버전: 그래들4.10.2 -> 6.7.1(오픈자바8), 스프링부트버전: 2.1.7 -> 2.4.9
- 위 버전 마이그레이션 기술 참조: https://jojoldu.tistory.com/539
- 위 빌드버전 수정 후 build.gradle 에서 코끼리 클릭으로 업데이트 처리

### 20210819(목) 작업.
- h2 데이터에비스에 추가로 postgresql 도 지원가능하게 추가.
- build.gradle 와부 DB 의존성 추가 
- db-postgres.properties 파일추가 (하이버네이트 JPA 를 이용해서 테이블 생성, 더미데이터 입력)

### 20210818(수) 작업.
- Save-, Update-, List-, ResponseDto 클래스 1개로 통합. PostsDto.java 
- 페이징 웹툰 회차 처럼 Select 박스추가.

### 20210817(화) 작업
- 검색과 페이징 처리 기능 추가. 기술참조: https://gonyda.tistory.com/15
- 1. 검색 : Page<Posts> findByTitleContaining(String keyword, Pageable pageable); JPA 내장된 매서드 사용
- 2. 페이징 처리: 컨트롤러에서 @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable 사용
- 3. 검색 페이지에서 페이징 model.addAttribute("next", pageable.next().getPageNumber()); 사용
- 페이징 처리 다른 방법(좀더 복잡): https://victorydntmd.tistory.com/333

### 20210816(월) 작업
- file 처리 CRUD 중 RUD 처리(다운로드 기능)
- 게시물 수정시 파일 수정 처리는 기존파일 삭제 후 신규파일 저장 및 파일 엔티티는 update 처리
- 게시물 삭제 및 개별 파일 삭제 시는 기존파일 삭제 후 파일 엔티티 삭제
- $ajax 용 index.js, post-save, post-update mustache 파일 기능 추가
- application.properties 에 업로드 용량과 경로 추가.

### 20210815(일) 작업
- 게시판 첨부파일 기능 추가. (아래 기술참조 URL)
- https://kyuhyuk.kr/article/spring-boot/2020/07/22/Spring-Boot-JPA-MySQL-Board-Post-File-Upload-Download
- 기존 게시판 로직에 fileId 필드 추가.(domain, dto, mustache, index.js)
- File 로직 추가(domain, repository, dto, Service)
- File 뷰단 처리(post-save.mustache, index.js) file 처리 CRUD 중 C처리

### 20210814(토) 작업
- 회원가입 유효성 검사 자바 클래스 추가.
- 에러 페이지처리 기능.(에러페이지 만들기 로 검색)
- ErrorController 를 구현(상속)받는 클래스 생성 CustomErrorController implements ErrorController
- templates 폴더에 error 폴더 추가 후 error.mustache 공통 에러 뷰파일 주가 

### 20210813(금) 작업
- 참고: 회원관리기능에서 username 이 userid 와 같은 역할.
- 회원관리 기능 CRUD 추가(admin 관리자 ROLE_ADMIN 에서 회원등록 권한 추가)

### 20210812(목) 작업
- 회원관리 기능 CRUD 추가(admin 관리자 ROLE_ADMIN 에서 회원등록 권한 추가)
- [domain 폴더에 simple_users 패키지] 생성.
- [@Entity] SimpleUsers.java 클래스 생성.(앱 실행시 SIMPLE_USERS 테이블 자동생성 jpa 기능)
- @Entity 클래스에 빌더패턴으로 Setter 에 사용할 생성자매서드 추가.
- SimpleUsersRepository.java 기능 [JpaRepository 인터페이스 쿼리메서드] 추가.
- [web > dto 폴더]에 SimpleUsers.Dto [DTO](구 VO, 전송데이터 임시저장)기능 추가.
- [service 폴더에 simple_users 패키지] 생성.
- SimpleUsersService.java [@Service] 기능 추가.
- [web 폴더] 에 SimpleUsersController.java [@Controller 클래스] 추가.
- [resources > templates > simple_users 폴더]에 머스태치 jsp 파일 추가.
- list.mustache 까지 작업.

### 20210811(수) 작업
- Spring Boot: 시큐리티(Security) – 3 – 로그인 및 권한 정보를 DB에서 가져오기
- 회원 DB 에서 로그인 연동하기 : http://yoonbumtae.com/?p=1202
- 메모리에 하드코딩된 권한 정보를 데이터베이스에 옮겨서 가져오도록 처리.
- 스프링 암호화 값 만들기: https://www.browserling.com/tools/bcrypt (암호: 1234, ROUND: 12)
- @Entity SimpleUsers.java 생성 후 초기 값 import.sql 을 resources 폴더에 넣고 쿼리가 자동 실행 되게 함.

### 20210810(화) 작업
- 로깅레벌 설정: https://programmer93.tistory.com/46
- TRACE  <  DEBUG  <  INFO  <  WARN  <  ERROR
- 예를 들어 로깅 레벨 설정을 "INFO"로 하였을 경우 "TRACE", "DEBUG" 레벨은 무시한다.
- private: 다른 클래스에서 사용하지 못하도록.
- static: 인스턴스당 하나만 사용하겠다고 명시.
- final: 내용을 변경하지 않겠다고 명시. 
- Spring Boot: 시큐리티(Security) – 2 – 커스텀 로그인 페이지 만들기 참조: http://yoonbumtae.com/?p=1184
- /login 기존 자동 생성된 소스를 개발자가 커스터마이징 해서 생성하기 마무리.
- SecurityConfig 클래스에 폼로그인 메서드 추가: formLogin() 
- indexController 클래스에 추가: if(user == null && principal != null) { //일반 로그인 일때 세션 저장
- 빌더형 생성자 메서드에 데이터를 입력하는 방법(Serializable 사용안함. 아래처럼 메서드를 체이닝 사용 = 빌더패턴)
```java
//User user_local = new User(userName,"","",Role.USER);//Serializable 에러
/*
User user_local = User.builder()
        .name(userName)
        .email("")
        .picture("")
        .role(Role.USER)
        .build();
 */
```
- 일반 로그인 성공: 메모리 인증 사용
- 권한 정보를 inMemoryAuthentication 란 매서드를 이용해서 메모리에 하드코딩 해서 사용처리.
- 위 작업을 컨트롤러에서 LoginUserArgumentResolver.java 로 변경 = 공통코드 처리: 기술참조(아래)
- http://chomman.github.io/blog/spring%20framework/spring-security%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%82%AC%EC%9A%A9%EC%9E%90%EC%9D%98-%EC%A0%95%EB%B3%B4%EB%A5%BC-%EC%B0%BE%EB%8A%94-%EB%B0%A9%EB%B2%95/

### 20210809(월) 작업
- Spring Boot: 시큐리티(Security) – 1 참조: http://yoonbumtae.com/?p=764
- 네이버로그인 말고, 일반 회원 로그인 기능 추가
- /login 기존 자동 생성된 소스를 개발자가 커스터마이징 해서 생성하기.(참조:  http://yoonbumtae.com/?p=2872)
- SecurityConfig.java 에서 http는 내장된 대다수의 메서드들이 http 객체 자신을 반환하기 때문에 
- 제이쿼리의 처럼 메서드를 체이닝 하여 사용(빌더패턴)할 수 있습니다.

### 20210808(일) 작업
- [05] 4. 세션 저장소로 데이터베이스 사용하기
- 4.1. spring-session-jdbc 등록
- JUnit 테스트에서 CRUD 가능하게 처리 

### 20210807(토) 작업
- DTO 인 OAuthAttributes.java 클래스 소스코드 해석.(OAuthAttributes of(...) 핵심)
- 시프링시큐리티설정에 지정된 CustomOAuth2UserService 클래스의 loadUser 가 실행되면, 인증받은 반환값을 처리하게 된다.

### 20210806(금) 작업
- [05 스프링시큐리티와 OAuth2.0으로 로그인]
- 스프링 시큐리티 사전 정보: https://velog.io/@jayjay28/2019-09-04-1109-%EC%9E%91%EC%84%B1%EB%90%A8
- 2.구글 로그인 연동하기 시작
1. User @엔티티 도메인클래스+Role 열거형권한 Enum+UserRepository(내장 CRUD 자동메서드) 인터페이스셩성
2. 소셜 로그인 코드를 작성: config.auth 패키지에 SecurityConfig 스프링 시큐리티클래스 + CustomOAuth2UserService 소셜로그인클래스를 생성한다.
3. CustomOAuth2UserService 에서 소셜로그인 처리 후 가져온 객체를 세션애 저장할 DTO 객체클래스인 SessionUser(Serializable 상속) 클래스를 생성한다.
4. 위 서비스 클래스 loadUser() 에서 세션을 발생 시킨다. httpSession.setAttribute("user", new SessionUser(user));
5. CustomOAuth2UserService 클래스에서 사용될 saveOrUpdate(OAuthAttributes attributes) =OAuthAttributes DTO 클래스를 생성합니다.
6. OAuthAttributes DTO 클래스는 반환받은 사용자 정보를 분해(파싱)하는 역할을 합니다.
7. 인증된 사용자 정보 클래스인 SessionUser DTO 클래스를 생성합니다.
8. indexContorller 클래스 model 객체 추가 및, index.mustache 로그인 버튼 수정
9. Session user = (SessionUser) httpSession.getAttribute("user"); -> 메서드 매개변수로 처리 @LoginUser SessionUser user
10. 위 처리를 위해서 애노테이션 파일 LoginUser 생성 및 메서드 파라미터로 사용하기 위한 LoginUserArgumentResolver 클래스 생성
11. 위 Resolver 클래스를 스프링 에서 인식시키도록 @Configuration 클래스인 WebConfig.java 생성

### 20210805(목) 작업
- 1.5. 게시글 수정, 삭제 화면 만들기
- @Service 클래스에서는 2가지 작업을 실행하게 됨(1. ApiRepository 인터페이스에서 CRUD 실행 2. @Entity 클래스에서 CRUD)

### 20210804(수)
- [04 머스테치템플릿으로 화면 구성하기]
- 스프링 레거시와 마친가지로 스프링부트도 기능추가시 외부모듈 라이비러리를 추가해야 합니다.
- 스프링 레거시 pom.xml 대신에 스프링부트는 build.gradle 로 의존성 모듈을 추가 합니다.
- 작업순서: mustache파일 -> @Controller+dto클래스 -> @Service -> Repository+@Entity클래스

### 20210803(화)
- [03 스프링부트에서 JPA로 데이터베이스 다루기]까지 작업됨.

### 20210802(월)
- assertThat(조건): JUnit 에서 정해진 조건에 맞지 않을 때(false) 프로그램을 중단합니다

### 20210801(일)
- 스프링시큐리티 권한(ROLE_USER, ROLE_GUEST) 확인파일 5개(아래실행순서)
- config.auth폴더(CustomOAuto2UwerService.java:saveOrUpdate()메서드)
- config.auth.dto폴더(OAuthAttributes.java:toEntity()메서드)
- domain.user폴더(User.java:User()메서드)
- domain.user폴더(Role.java:USER()메서드)

### 20210731(토)
- 그래들(외부모듈)설정파일: build.gradle 확인 
- VCS->Import Into Version Control->Create Git Repository 로 로컬 깃 생성
- 깃허브에 레포지토리 생성 후 깃 주소 복사 (https://github.com/kimilguk/kimilguk-springboot2.git)
- commit 과 푸시 모두 실행
- 참고: 인텔리J  인덱싱 기능을 끄기: https://jootc.com/p/202001243264

### 20210730(금)
- 인텔리J 커뮤니티(무료)버전은 스프링 프로젝트를 지원하지 않는다. 아쉽지만, 플러그인 설치로 스프링부트를 사용할 수 있다.(아래)
- https://www.jetbrains.com/idea/features/editions_comparison_matrix.html
- 인텔리J 커뮤니티(무료)zip버전 으로 압축해제 후 사용: https://www.jetbrains.com/ko-kr/idea/download/#section=windows
- 인텔리J 한글 인코딩 설정 추가: https://goddaehee.tistory.com/248
- 기존소스 분석 및 작업한 결과 깃과 연동OK.
- 헤로쿠와 연동 Procfile 메이븐에서 그래들빌드용으로 변경(아래).
- web: java -Dserver.port=$PORT $JAVA_OPTS -jar build/libs/kimilguk-springboot2-1.0.3-SNAPSHOT-20210730151335.jar