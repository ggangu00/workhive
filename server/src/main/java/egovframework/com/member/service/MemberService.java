package egovframework.com.member.service;

import java.util.List;

import egovframework.com.securing.service.UserDTO;

public interface MemberService {
	
	//사원 전체조회
	public List<UserDTO> memberSelectAll();
}
