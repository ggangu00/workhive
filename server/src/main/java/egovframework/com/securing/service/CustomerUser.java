package egovframework.com.securing.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Spring Security의 UserDetails를 구현한 사용자 객체 UserDTO 정보를 기반으로 Spring Security
 * 인증에서 사용할 사용자 정보를 제공
 */
@Data
@AllArgsConstructor
public class CustomerUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	// 사용자 정보 객체 (UserDTO에는 사용자의 ID, 비밀번호, 권한 정보 등이 포함됨)
	private final UserDTO userDTO;

	/**
	 * 사용자의 권한(ROLE) 정보를 반환하는 메서드 UserDTO에 저장된 roles 리스트를 SimpleGrantedAuthority 객체로
	 * 변환하여 반환
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// userDTO.getRoles()가 null일 가능성 대비
		List<String> roles = userDTO.getRoles();

		if (roles == null || roles.isEmpty()) {
			return Collections.emptyList(); // 빈 리스트 반환
		}

		return roles.stream().filter(role -> role != null && !role.trim().isEmpty()) // 빈 문자열도 필터링
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	/**
	 * 사용자의 비밀번호를 반환하는 메서드
	 */
	@Override
	public String getPassword() {
		return userDTO.getPassword();
	}

	/**
	 * 사용자의 아이디(로그인 시 사용되는 값)를 반환하는 메서드
	 */
	@Override
	public String getUsername() {
		return userDTO.getMberId(); // userDTO에 저장된 회원 ID 반환
	}

	/**
	 * 계정의 만료 여부를 반환하는 메서드 true: 계정이 만료되지 않음
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정의 잠김 여부를 반환하는 메서드 true: 계정이 잠기지 않음
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * ✅ 계정의 활성화 여부를 반환하는 메서드 userDTO의 mberStatus가 'Y'일 경우만 활성화된 것으로 판단
	 */
	@Override
	public boolean isEnabled() {
		return "J01".equals(userDTO.getMberSttus());
	}

	/**
	 * 비밀번호의 만료 여부를 반환하는 메서드 true: 비밀번호가 만료되지 않음
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
