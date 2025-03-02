package egovframework.com.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.common.mapper.CommonMapper;
import egovframework.com.common.service.CommonDTO;
import egovframework.com.common.service.CommonService;

@Service("CommonService")
public class CommonServiceImpl implements CommonService {

	@Resource
	private CommonMapper commonMapper;

	// 공통코드 디테일 전체조회
	@Override
	public List<CommonDTO> commonSelectAll(String commCd) {
		return commonMapper.commonSelectAll(commCd);
	}

	// 거래처 전체조회
	@Override
	public List<CommonDTO> comSelectAll() {
		return commonMapper.comSelectAll();
	}

	// 로그인 로그 전체조회
	@Override
	public List<CommonDTO> loginLogSelectAll() {
		return commonMapper.loginLogSelectAll();
	}
	
	// 로그인 로그 등록
	@Override
	public boolean loginLogInsert(CommonDTO dto) {
		return commonMapper.loginLogInsert(dto) == 1 ? true : false;
	}

	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	@Override
	public CommonDTO homeInfo(String memCd) {
		return commonMapper.homeInfo(memCd);
	}

	
}
