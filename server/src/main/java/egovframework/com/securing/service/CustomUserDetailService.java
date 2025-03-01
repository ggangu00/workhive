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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("UserDetailsService 로그인 시도 => " + username.toString());
		
		// 데이터베이스에서 username을 기준으로 사용자 정보 조회
        UserDTO userDTO = uMapper.getUser(username);
        
        // 사용자가 존재하지 않으면 예외 발생
        if (userDTO == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        // 사용자의 권한 목록 설정 (DB에서 불러오도록 개선 가능)
        List<String> roles = new ArrayList<>(); 

        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            roles.add("ROLE_USER"); // 기본값을 ROLE_USER로 설정
        } else {
            roles.addAll(userDTO.getRoles()); // DB에서 가져온 역할 추가
        }
        userDTO.setRoles(roles); // 권한 설정
        log.info("UserDetailsService 로그인 성공 => " + userDTO.getMberId() + ", 권한: " + roles);
        
        // Spring Security에서 사용할 수 있도록 UserDetails 객체로 변환하여 반환
        return new CustomerUser(userDTO);
	}
	
}
