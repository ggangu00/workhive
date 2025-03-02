package egovframework.com.securing.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

/**
 * Spring Security의 UserDetails 구현 클래스
 * 
 * - UserDTO 정보를 기반으로, 스프링 시큐리티가 요구하는 사용자 정보와 권한을 제공하는 역할
 * - 인증 과정에서 사용자의 정보와 권한을 시큐리티 내부적으로 관리할 때 이 객체가 활용됨
 */
@Data
public class CustomerUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	// 인증 대상 사용자 정보를 담고 있는 DTO
	private final UserDTO userDTO;

	/**
	 * 생성자 - 반드시 UserDTO를 받아야 하며, null이면 예외 발생
	 * 
	 * @param userDTO 사용자 정보 객체 (필수)
	 */
	public CustomerUser(UserDTO userDTO) {
		if (userDTO == null) {
			throw new IllegalArgumentException("UserDTO는 null일 수 없습니다."); // 방어코드
		}
		
		this.userDTO = userDTO;
	}

	/**
	 * 사용자의 권한 목록 반환
	 * - UserDTO에 저장된 roles(List<String>) 정보를 SimpleGrantedAuthority 객체로 변환
	 * 
	 * @return 사용자의 권한 목록 (SimpleGrantedAuthority 리스트)
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roles = userDTO.getRoles();
		
		if (roles == null || roles.isEmpty()) {
			return Collections.emptyList(); // 권한이 없으면 빈 리스트 반환
		}
		return roles.stream()
				.filter(role -> role != null && !role.trim().isEmpty()) // null이나 빈 권한은 제외
				.map(SimpleGrantedAuthority::new) // 문자열 role을 시큐리티용 객체로 변환
				.collect(Collectors.toList());
	}

	/**
	 * 비밀번호 반환
	 * 
	 * @return 비밀번호 (암호화된 상태일 가능성 높음)
	 */
	@Override
	public String getPassword() {
		return userDTO.getPassword();
	}

	/**
	 * 사용자명 반환 (이 시스템에서는 회원ID가 사용자명)
	 * 
	 * @return 사용자ID
	 */
	@Override
	public String getUsername() {
		return userDTO.getMberId();
	}

	/**
	 * 계정 만료 여부 반환 (현재는 무조건 true로 설정 - 관리 로직 필요시 변경 가능)
	 * 
	 * @return 계정이 만료되지 않았는지 여부
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정 잠금 여부 반환
	 * - lockAt이 'A02'이면 잠금 아님
	 * 
	 * @return 계정이 잠금되지 않았는지 여부
	 */
	@Override
	public boolean isAccountNonLocked() {
		return "A02".equalsIgnoreCase(userDTO.getLockAt());
	}

	/**
	 * 계정 활성화 여부 반환
	 * - 회원상태 코드가 'J03'이 아닌 경우 true
	 * 
	 * @return 계정이 활성화되어 있는지 여부
	 */
	@Override
	public boolean isEnabled() {
		return !"J03".equals(userDTO.getMberSttus());
	}

	/**
	 * 비밀번호 유효성 여부 반환 (현재는 무조건 true로 설정 - 관리 로직 필요시 변경 가능)
	 * 
	 * @return 비밀번호가 만료되지 않았는지 여부
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}