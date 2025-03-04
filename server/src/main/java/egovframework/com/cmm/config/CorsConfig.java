package egovframework.com.cmm.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
	
	 @Override
	 public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**")	// 모든 요청에 CORS 적용
		         .allowedOrigins("http://localhost:8077") // Vue 개발 서버 허용
		         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
		         .allowedHeaders("*")
		         .allowCredentials(true); // 인증 정보 포함 (세션 유지)
	 }
}
