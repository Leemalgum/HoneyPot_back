package com.beeSpring.beespring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CorsConfig 를 통해 스프링 부트 애플리케이션이 모든 도메인에서의 요청을 허용하고, 특정 헤더를 노출하며, 다양한 HTTP 메서드를 지원할 수 있게된다.
 * 이 설명이 애초에 포트 번호 다른 서버 (프론트와 백처럼) 간의 통신 때문에 그런건데 아래처럼 beespring.shop 으로 제한할 수 있는지 확인해 보아야 한다.
 * */
@Configuration
public class CorsConfig {
    @Bean
    @Lazy
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // url 패턴을 기반으로 CORS 구성을 저장하는 객체
        CorsConfiguration config = new CorsConfiguration(); // CORS 정책을 정의하는 객체
        config.setAllowCredentials(true); // 자격 증명 (쿠키, 인증 헤더 등) 을 허용하도록 설정
        config.addAllowedOrigin("http://localhost:3000"); // 모든 도메인에서의 요청을 허용 (와일드카드 문법을 허용하는 메서드)
        config.addAllowedOrigin("http://localhost:8080"); // 모든 도메인에서의 요청을 허용 (와일드카드 문법을 허용하는 메서드)
        // 꼭 !!!!! 배포 후에는 config.addAllowedOriginPatter("http://beespring.shop")로 바꿔야 한다. 안그러면 위에 true 설정 때문에 보안 엄청 취약해짐
        config.addAllowedHeader("*"); // 모든 헤더를 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 (GET, POST, PUT, DELETE 등)을 허용
        config.addExposedHeader("Authorization"); // 클라이언트가 'Authorization' 헤더에 접근할 수 있게 한다
        config.addExposedHeader("refreshToken"); // 클라이언트가 'refreshToken' 헤더에 접근할 수 있게 한다
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위에서 설정한 CORS 구성을 적용한다
        return new CorsFilter(source); // CorsFilter 객체를 생성하여 반환한다. 이 필터는 위에서 설정한 CORS 정책을 기반으로 작동한다.
    }
}
