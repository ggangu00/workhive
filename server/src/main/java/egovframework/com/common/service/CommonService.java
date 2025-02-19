package egovframework.com.common.service;

import java.util.List;

public interface CommonService {
	// 공통코드 디테일 전체조회
	public List<CommonDTO> commonSelectAll(String commCd);
	
	// 거래처 전체조회
	public List<CommonDTO> comSelectAll();
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	public CommonDTO homeInfo(String memCd);
}
