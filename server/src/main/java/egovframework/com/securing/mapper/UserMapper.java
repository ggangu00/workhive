package egovframework.com.securing.mapper;

import java.util.List;

import egovframework.com.securing.service.RoleDTO;
import egovframework.com.securing.service.UserDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("userMapper")
public interface UserMapper {
	UserDTO getUser(String loginId);
	
	List<RoleDTO> getRole(Long id);
}
