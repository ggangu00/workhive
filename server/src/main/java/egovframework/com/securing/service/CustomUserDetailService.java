package egovframework.com.securing.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.com.securing.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserMapper uMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 단건조회
		UserDTO userDTO = uMapper.getUser(username);
		if(userDTO == null) {
			throw new UsernameNotFoundException("id check");
		}
		
		return new CustomerUser(userDTO);
	}
	
}
