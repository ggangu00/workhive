package egovframework.com.authority.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AuthorityService {
	
	// 권한 전체조회
	public List<AuthorityDTO> authoritySelectAll();
	
	// 권한 단건조회
	public AuthorityDTO authoritySelect(String authorityCd);
	
	// 권한 등록
	public boolean authorityInsert(AuthorityDTO dto);
	
	// 권한 수정
	public boolean authorityUpdate(AuthorityDTO dto);
	
	// 권한 삭제
	public boolean authorityDelete(String authorityCd);
	
	// 메뉴 접근 권한 조회
	public boolean isMenuAccessible(@Param("menuCd") String menuCd, @Param("userId") String userId);
	
}
