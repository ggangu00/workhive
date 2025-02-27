package egovframework.com.securing.service;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
	
	private Long id;
	private String mberId;
	private String password;
	private String fullName;
	private String deptName;
	
	private List<String> roles;

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		List<GrantedAuthority> role = new ArrayList<>();
//		roles.forEach(r -> role.add(new SimpleGrantedAuthority(r.getRoleName())));
//		return role;
//	}
//
//	@Override
//	public String getUsername() {
//		
//		return null;
//	}
}
