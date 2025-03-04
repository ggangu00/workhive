package egovframework.com.common.service;

import java.util.List;

import egovframework.com.cmm.ComDefaultVO;

public interface CommonService {
	// 공통코드 디테일 전체조회
	public List<CommonDTO> commonSelectAll(String commCd);
	
	// 거래처 전체조회
	public List<CommonDTO> comSelectAll();
	
	// 로그인 로그 전체조회
	public List<CommonDTO> loginLogSelectAll(ComDefaultVO searchVO);
	
	// 로그인 로그 총 갯수
	public int loginLogSelectAllCnt(ComDefaultVO searchVO);
	
	// 로그인 잠금해제
	public boolean loginLogUpdate(String mberId);
	
	// 로그인 로그 등록
	public boolean loginLogInsert(CommonDTO dto);
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	public CommonDTO homeInfo(String memCd);
	
	// 회사 정보 조회
	public CommonDTO companyInfo();
	
}
