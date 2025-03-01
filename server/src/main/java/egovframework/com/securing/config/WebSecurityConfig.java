package egovframework.com.securing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import egovframework.com.securing.service.CustomUserDetailService;
import egovframework.com.securing.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@Configuration // Spring Security 설정을 위한 설정 클래스 선언
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
@RequiredArgsConstructor
public class WebSecurityConfig {
	
    private final JwtUtil jwtUtil;  // 생성자 주입
    private final CustomUserDetailService userDetailsService; // 생성자 주입
    
	@Bean
	PasswordEncoder passwordEncoder() {
		// 비밀번호를 안전하게 저장하기 위해 BCryptPasswordEncoder 사용
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/**", "/menu", "/comm/*", "/department", "/project/*", "/todo/**", "/document/*", "/meet/*", "/css/*", "/js/*", "/assets/*", "/login").permitAll() // 홈, 정적 리소스는 인증 없이 접근 가능

//				.requestMatchers("/", "/menu", "/css/*", "/js/*", "/assets/*", "/login", "/loginproc").permitAll() // 홈, 정적 리소스는 인증 없이 접근 가능

				.anyRequest().authenticated()) // 위의 URL 외에는 모두 인증된 사용자만 접근 가능
				.formLogin((form) -> form.successHandler(suthenticationSuccessHandler()) // 로그인 성공 시 실행할 핸들러
										.failureHandler(suthenticationFaildHandler()) // 로그인 실패 시 실행할 핸들러
										.permitAll()) // 로그인 페이지는 인증 없이 접근 가능하도록 설정
				.logout((logout) -> logout.deleteCookies("JSESSIONID") // 로그아웃 시 JSESSIONID 쿠키 삭제
										.permitAll()) // 로그아웃은 인증 없이도 가능하도록 설정
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session
		                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		            )
		            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 패스워드 체크 전에 확인함

		// CSRF 설정 (현재 비활성화됨)

		http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler())
										.authenticationEntryPoint(authenticationEntryPoint())); // 접근 거부(403) 예외 발생 시 커스텀 핸들러 적용

		return http.build(); // SecurityFilterChain 객체를 반환하여 Security 설정 적용
	}

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, CustomUserDetailService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
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
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
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