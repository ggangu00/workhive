package egovframework.com.securing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.com.authority.mapper.AuthorityMapper;
import egovframework.com.securing.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserMapper uMapper;
	private final AuthorityMapper authMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("username => " + username);
		// 단건조회
		
		UserDTO userDTO = uMapper.getUser(username);
		if(userDTO == null) {
			throw new UsernameNotFoundException("id check");
		}
		List<String> list = new ArrayList();
		
		list.add("ROLE_ADMIN");
		
		userDTO.setRoles(list);
		
		return new CustomerUser(userDTO);
	}
	
}
