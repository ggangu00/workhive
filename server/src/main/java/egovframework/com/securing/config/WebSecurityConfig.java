package egovframework.com.securing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import egovframework.com.securing.service.impl.CustomUserDetailService;
import lombok.RequiredArgsConstructor;

@Configuration  // Spring Security 설정 클래스
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor // 생성자 주입을 자동으로 처리
public class WebSecurityConfig {
    
    //private final JwtUtil jwtUtil; // JWT 토큰 생성 및 검증 유틸리티
    private final CustomUserDetailService userDetailsService; // 사용자 정보 로드 서비스
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    /**
     * 🔐 비밀번호 암호화를 위한 BCryptPasswordEncoder 설정
     * - Spring Security는 기본적으로 평문(Plain Text) 비밀번호 사용을 허용하지 않음.
     * - 따라서 모든 비밀번호를 안전하게 저장하기 위해 BCrypt 해싱을 적용해야 함.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 🔒 Spring Security 필터 체인 설정
     * - HTTP 요청에 대한 보안 규칙을 정의
     * - JWT 기반 인증을 적용하기 위해 CSRF 비활성화 및 세션 정책을 STATELESS로 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	  http
	          .csrf(csrf -> csrf.disable())  // JWT 기반이므로 CSRF 비활성화
	          .sessionManagement(session -> session
	              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 아예 사용 안 함
	          )
	          .authorizeHttpRequests(auth -> auth
	              .requestMatchers("/loginProc", "/passwordProc", "/menu/**", "/board/**","/css/**", "/dist/**", "/js/**", "/assets/**", "/index.html").permitAll()  // 로그인 및 정적 리소스는 모두 허용
	              .requestMatchers("/access/**").authenticated()  // 권한 체크 API는 인증 필요
	              .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
	          )
	          .exceptionHandling(ex -> ex
	              .accessDeniedHandler(accessDeniedHandler())  // 권한 부족 시 핸들러
	              .authenticationEntryPoint(authenticationEntryPoint())  // 인증 안 된 사용자 접근 시 핸들러
	          )
	          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JWT 필터를 UsernamePassword 앞에 추가

      return http.build();
    }

    /**
     * ️ JWT 인증 필터 등록
     * JWT를 검증하여 사용자 인증을 수행하는 필터
     */
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(ObjectMapper objectMapper) {
//        return new JwtAuthenticationFilter(jwtUtil, userDetailsService, objectMapper);
//    }

    /**
     * AuthenticationManager 설정
     * Spring Security의 사용자 인증을 담당하는 핵심 객체
     * AuthenticationManager가 UserDetailsService를 이용해 사용자 인증 수행
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * AuthenticationProvider 설정
     * DaoAuthenticationProvider를 사용하여 인증 처리
     * UserDetailsService 및 PasswordEncoder 설정
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 로그인 성공 핸들러 설정
     * 로그인 성공 시 실행할 커스텀 핸들러 반환
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    /**
     * 로그인 실패 핸들러 설정
     * 로그인 실패 시 실행할 커스텀 핸들러 반환
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailedHandler() {
        return new CustomLoginFailedHandler();
    }

    /**
     * 인증되지 않은 사용자 접근 시 처리할 핸들러
     * 예를 들어, JWT 토큰이 없는 요청이 보호된 API를 호출할 경우 이 핸들러가 실행됨
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * 접근 권한이 없는 요청 시 처리할 핸들러
     * 인증은 되었지만 특정 리소스에 접근할 권한이 없을 때 실행됨
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
    /**
     * Spring MVC의 요청 매핑을 분석하는 Bean 등록
     * Spring Security에서 URL 패턴을 정확히 인식하기 위해 필요
     */
    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
