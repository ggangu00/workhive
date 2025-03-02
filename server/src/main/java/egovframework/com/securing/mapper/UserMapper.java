package egovframework.com.securing.mapper;

import java.util.List;

import egovframework.com.securing.service.RoleDTO;
import egovframework.com.securing.service.UserDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * 사용자 관련 DB 접근을 담당하는 매퍼 인터페이스 MyBatis Mapper로 등록되어 있으며, 사용자 정보 및 권한 정보를 조회하는 역할
 */
@Mapper("userMapper")
public interface UserMapper {

	/**
	 * 로그인 아이디로 사용자 정보를 조회하는 메서드
	 * 
	 * @param loginId 사용자 로그인 아이디
	 * @return 해당 아이디에 해당하는 사용자 정보 (UserDTO)
	 */
	UserDTO getUser(String loginId);

	/**
	 * 사용자 ID로 해당 사용자의 권한 목록을 조회하는 메서드
	 * 
	 * @param id 사용자 PK (내부 관리용 ID)
	 * @return 해당 사용자의 권한 목록 (RoleDTO 리스트)
	 */
	List<RoleDTO> getRole(Long id);

}
