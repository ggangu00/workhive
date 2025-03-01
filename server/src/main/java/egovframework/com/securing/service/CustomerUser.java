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
public class CustomerUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	// 사용자 정보 객체 (null 불가)
	private final UserDTO userDTO;

	// 생성자 - userDTO가 null이면 바로 예외 발생
	public CustomerUser(UserDTO userDTO) {
		if (userDTO == null) {
			throw new IllegalArgumentException("UserDTO는 null일 수 없습니다.");
		}
		this.userDTO = userDTO;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roles = userDTO.getRoles();
		if (roles == null || roles.isEmpty()) {
			return Collections.emptyList();
		}
		return roles.stream()
				.filter(role -> role != null && !role.trim().isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return userDTO.getPassword();
	}

	@Override
	public String getUsername() {
		return userDTO.getMberId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return "N".equals(userDTO.getLockAt());
	}

	@Override
	public boolean isEnabled() {
		return "J01".equals(userDTO.getMberSttus());
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}

