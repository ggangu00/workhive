package egovframework.com.securing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.com.securing.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 정보 조회 및 인증 처리를 담당하는 서비스 클래스
 * Spring Security의 UserDetailsService를 구현하여 사용자 인증 시 필요한 정보를 제공
 */
@Slf4j
@Service // 서비스 레이어에서 동작하도록 설정
@RequiredArgsConstructor // final 필드를 가진 생성자를 자동으로 생성해줌
public class CustomUserDetailService implements UserDetailsService {

	private final UserMapper uMapper; // 사용자 정보를 조회하는 MyBatis Mapper (의존성 주입)

	/**
     * 사용자 정보를 데이터베이스에서 조회하고, UserDetails 객체로 변환하는 메서드
     *
     * @param username 프론트에서 전달받은 로그인 아이디
     * @return UserDetails 사용자 정보 객체 (Spring Security 인증 프로세스에 사용)
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 예외 발생
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    log.info("🔑 [UserDetailsService] 로그인 시도 - username: {}", username);
	    
	    // DB에서 사용자 정보 조회
	    UserDTO userDTO = uMapper.getUser(username);
	    
	    // 조회된 사용자가 없으면 예외 발생
	    if (userDTO == null) {
	        log.warn("⚠️ [UserDetailsService] 해당 사용자 없음 - username: {}", username);
	        throw new UsernameNotFoundException("User not found: " + username);
	    }
	    log.info("✅ [UserDetailsService] 사용자 조회 성공 - mberId: {}, name: {}", userDTO.getMberId(), userDTO.getMberNm());

	    // 권한 리스트 설정(권한이 유동적으로 변하기 때문에 임시로 'ROLE_ADMIN' 부여)
	    List<String> roles = new ArrayList<>();
	    roles.add("ROLE_ADMIN");
	    userDTO.setRoles(roles);
	    
	    log.info("✅ [UserDetailsService] 사용자 권한 설정 완료 - roles: {}", roles);

	    // 조회된 사용자 정보를 기반으로 Custom한 UserDetails 객체 반환
	    return new CustomerUser(userDTO);
	}
}