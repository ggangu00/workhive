package egovframework.com.securing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.com.securing.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // 서비스 레이어에서 동작하도록 설정
@RequiredArgsConstructor // final 필드를 가진 생성자를 자동으로 생성해줌
public class CustomUserDetailService implements UserDetailsService {

	private final UserMapper uMapper; // 사용자 정보를 가져오는 MyBatis Mapper

	/**
	 * 사용자 정보를 데이터베이스에서 조회하여 UserDetails 객체로 변환하는 메서드
	 * @param username 로그인 아이디 (프론트에서 전달한 값)
	 * @return UserDetails 객체 (Spring Security 인증을 위해 필요)
	 */
	@Override
	public CustomerUser loadUserByUsername(String username) throws UsernameNotFoundException {
	    log.info("🔑 [UserDetailsService] 로그인 시도 - username: {}", username);

	    UserDTO userDTO = uMapper.getUser(username);

	    if (userDTO == null) {
	        log.warn("⚠️ [UserDetailsService] 해당 사용자 없음 - username: {}", username);
	        throw new UsernameNotFoundException("User not found: " + username);
	    }

	    log.info("✅ [UserDetailsService] 사용자 조회 성공 - mberId: {}, name: {}", userDTO.getMberId(), userDTO.getMberNm());

	    List<String> roles = new ArrayList<>();

	    if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
	        roles.add("ROLE_USER");
	    } else {
	        roles.addAll(userDTO.getRoles());
	    }
	    userDTO.setRoles(roles);

	    log.info("✅ [UserDetailsService] 사용자 권한 설정 완료 - roles: {}", roles);

	    return new CustomerUser(userDTO);
	}


}
