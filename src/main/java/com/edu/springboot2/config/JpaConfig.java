package com.edu.springboot2.config;

        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //대표적으로 생성일자, 수정일자, 등록자 와 같은 도메인마다 공통으로 존재하는 중복 코드를 자동으로 등록하는 기능을 사용한다.
public class JpaConfig {
}
