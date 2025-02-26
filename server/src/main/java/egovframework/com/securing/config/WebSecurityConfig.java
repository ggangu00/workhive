package egovframework.com.securing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration // Spring Security 설정을 위한 설정 클래스 선언
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
public class WebSecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		// 비밀번호를 안전하게 저장하기 위해 BCryptPasswordEncoder 사용
		return new BCryptPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/home", "/css/*", "/js/*", "/assets/*", "/login").permitAll() // 홈, 정적 리소스는 인증 없이 접근 가능
                .requestMatchers("/admin/*").hasRole("ADMIN") // "/admin/*" 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                .anyRequest().authenticated()) // 위의 URL 외에는 모두 인증된 사용자만 접근 가능
                .formLogin((form) -> form
                .usernameParameter("userid") // 로그인 시 사용할 username 파라미터 이름을 "userid"로 지정
                .successHandler(suthenticationSuccessHandler()) // 로그인 성공 시 실행할 핸들러 설정
                .failureHandler(suthenticationFaildHandler())
                .permitAll()) // 로그인 페이지는 인증 없이 접근 가능하도록 설정
                .logout((logout) -> logout
						.deleteCookies("JSESSIONID") // 로그아웃 시 JSESSIONID 쿠키 삭제
						.permitAll()) // 로그아웃은 인증 없이도 가능하도록 설정
        .csrf(csrf -> csrf.disable());

        // CSRF 설정 (현재 비활성화됨)

        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler())); // 접근 거부(403) 예외 발생 시 커스텀 핸들러 적용
        return http.build(); // SecurityFilterChain 객체를 반환하여 Security 설정 적용
    }

    @Bean
    public AuthenticationSuccessHandler suthenticationSuccessHandler() {
        return new CustomLoginSuccessHandler(); // 로그인 성공 시 실행할 커스텀 핸들러 반환
    }

    @Bean
    public AuthenticationFailureHandler suthenticationFaildHandler() {
        return new CustomLoginFailedHandler(); // 로그인 실패 시 실행할 커스텀 핸들러 반환
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(); // 접근 권한이 없을 때 실행할 커스텀 핸들러 반환
    }

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector(); // Spring MVC의 요청 매핑을 분석하는 Bean 등록 (Spring Security의 URL 인식에 필요)
    }
}