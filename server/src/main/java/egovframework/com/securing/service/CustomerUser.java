package egovframework.com.securing.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UserDTO userDTO;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userDTO.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return userDTO.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isEnabled() { // enable 컬럼 만들어서 특정 계정에 넣어주면 => 로그인 못함
		return true;
	}

	@Override
	public String getUsername() {
		return userDTO.getLoginId();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

}
