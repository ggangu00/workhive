package egovframework.com.member.mapper;

import java.util.List;

import egovframework.com.securing.service.UserDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("MemberMapper")
public interface MemberMapper {

	// 프로젝트 전체조회
	public List<UserDTO> memberSelectAll();

	// 사원 단건조회
	public UserDTO memberSelect(String userId);
	
}
