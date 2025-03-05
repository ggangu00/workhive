package egovframework.com.authority.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import egovframework.com.authority.mapper.AuthorityMapper;
import egovframework.com.authority.service.AuthorityDTO;
import egovframework.com.authority.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Resource
	private AuthorityMapper authMapper;

	// 권한 전체조회
	@Override
	public List<AuthorityDTO> authoritySelectAll() {
		return authMapper.authoritySelectAll();
	}

	// 권한 단건조회
	@Override
	public AuthorityDTO authoritySelect(String authorityCd) {
		return authMapper.authoritySelect(authorityCd);
	}

	// 권한 등록
	@Override
	public boolean authorityInsert(AuthorityDTO dto) {

		return authMapper.authorityInsert(dto) == 1 ? true : false;
	}

	// 권한 수정
	@Override
	public boolean authorityUpdate(AuthorityDTO dto) {
		return authMapper.authorityUpdate(dto) == 1 ? true : false;
	}

	// 권한 삭제
	@Override
	public boolean authorityDelete(@Param("authorityCd") String authorityCd, @Param("mberId") String mberId) {
		return authMapper.authorityDelete(authorityCd, mberId) == 1 ? true : false;
	}

	// 메뉴 접근 권한 조회
	@Override
	public boolean isMenuAccessible(@Param("menuCd") String menuCd, @Param("userId") String userId) {
		return authMapper.isMenuAccessible(menuCd, userId) > 0 ? true : false;
	}

}
