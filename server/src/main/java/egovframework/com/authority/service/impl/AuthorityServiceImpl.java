package egovframework.com.authority.service.impl;

import java.util.List;

import javax.annotation.Resource;

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
		if (dto.getAuthorityNm() == null) {
            throw new IllegalArgumentException("권한명은 필수입니다.");
        }
		
		return authMapper.authorityInsert(dto) == 1 ? true : false;
	}

	// 권한 수정
	@Override
	public boolean authorityUpdate(String authorityCd) {
		return authMapper.authorityUpdate(authorityCd) == 1 ? true : false;
	}

	// 권한 삭제
	@Override
	public boolean authorityDelete(String authorityCd) {
		return authMapper.authorityDelete(authorityCd) == 1 ? true : false;
	}

}
