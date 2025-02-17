package egovframework.com.authority.mapper;

import java.util.List;

import egovframework.com.authority.service.AuthorityDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("authorityMapper")
public interface AuthorityMapper {
	
	// 권한 전체조회
	public List<AuthorityDTO> authoritySelectAll();

	// 권한 단건조회
	public AuthorityDTO authoritySelect(String authorityCd);

	// 권한 등록
	public int authorityInsert(AuthorityDTO dto);

	// 권한 수정
	public int authorityUpdate(String authorityCd);

	// 권한 삭제
	public int authorityDelete(String authorityCd);

}
