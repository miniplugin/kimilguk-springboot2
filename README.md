### 책 [스프링 부트와 aws로 혼자 구현하는 웹 서비스] 소스를 분석해 하고 있습니다.
- 기술참조 : https://github.com/kwj1270/TIL_SPRINGBOOT_WITH_AWS
- 소스참조 : https://github.com/kwj1270/freelec-springboot2-webservice
- 신규작업 : https://github.com/kimilguk/kimilguk-springboot2.git
- 인텔리J에서 깃 암호 저장하지 않게 설정(아래) 
- https://stackoverflow.com/questions/28142361/change-remote-repository-credentials-authentication-on-intellij-idea-14
- 헤로쿠용 빌드 파일 고정함. version '1.0.3-SNAPSHOT-'+new Date().format("yyyyMMddHHmmss") 수정.
- 유튜브 기술 참조: https://www.youtube.com/playlist?list=PLqaSEyuwXkSppQAjwjXZgKkjWbFoUdNXC

### 학습목차
- [들어가며](./README/00.md)
- [01 인텔리제이로 스프링 부트 시작하기](./README/01.md)
- [02 스프링부트에서 테스트 코드 작성하기](./README/02.md)
- [03 스프링부트에서 JPA로 데이터베이스 다루기](./README/03.md)
- [04 머스테치템플릿으로 화면 구성하기](./README/04.md)
- [05 스프링시큐리티와 OAuth2.0으로 로그인](./README/05.md)

### 스프링부트 에서 MVC
- 참고 스프링레거시 에서 MVC는 아래와 같습니다. 비교해 보시기 바랍니다.
- 스프링레거시 예: DB테이블 -> 모델(매퍼쿼리>@Repository인터페이스=DAO클래스) -> 서비스(@Service) -> 컨트롤러클래스 -> 뷰(JSP|타임리프,타일즈템플릿)
- 모델domain(구성:@Entity클래스 와 JpaRepository인터페이스) -> 서비스(@Service) -> 컨트롤러(구성:@Controller+DTO클래스) -> 뷰(타임리프,머스테치템플릿)
- @Entity클래스 : 데이터베이스 테이블을 자동생성 및 구현메서드(@Builder인터페이스사용) 추가.
- JpaRepository인터페이스 : 형식: JpaRepository<Entity 클래스, PK 타입> 기능: CRUD기본 쿼리 자동생성.
- 서비스(@Service) : 인터페이스 없이 사용.(주, 레포지토리는 인젝션으로 사용하지 않음. 
- 서비스 예, private final PostsRepository postsRepository;
- DTO(Data Transfer Object)클래스 : 스프링레거시의 VO와 같이 데이터 전송 임시 저장소로 Get/Set 역할을함.
- Mustache(머스테치)템플릿 : 템플릿 코드(예, {{userName}})로 자바 변수와 객체를 사용함.(JSTL과 타임리프,타일즈 대신사용)
- 작업순서예, 1. Requet 데이터임시저장 Dto, 2. API 요청을 받을 Controller, 3.서비스로 DAO호출, 4. 도메인작업(엔티티+레포지토리인터페이스)
- mustache 템플릿은 앱을 리스타트 해야지만 적용이 됩니다.

### 스프링 레거시와 스프링부트의 폴더 구조차이
- 스프링레거시 폴더구조:  [pom.xml], [WEB-INF>web.xml],[WEB-INF>spring>root-context.xml,WEB-INF>appServlet>servlet-context.xml]
- 스프링레거시 폴더구조: roo최상위 > [controller, dao(src/main/reousrces/mappers쿼리), service, vo 등]
- 스프링부트 폴더구조: roo최상위 > [build.gradle], [Application.java] > [config], [domain, service, web>dto 등]

### 20210812(목) 작업예정
- 일반회원 등록 기능 추가(admin 관리자 ROLE_ADMIN 에서 회원등록 권한 추가)
- 게시판 첨부파일 기능 추가

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