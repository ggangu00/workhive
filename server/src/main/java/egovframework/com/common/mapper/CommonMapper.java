package egovframework.com.common.mapper;

import java.util.List;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.common.service.CommonDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("commonMapper")
public interface CommonMapper {
	
	// 공통코드 디테일 전체조회
	public List<CommonDTO> commonSelectAll(String commCd);
	
	// 거래처 전체조회
	public List<CommonDTO> comSelectAll();
	
	// 로그인 로그 전체조회
	public List<CommonDTO> loginLogSelectAll(ComDefaultVO searchVO);
	
	// 로그인 로그 총갯수
	int loginLogSelectAllCnt(ComDefaultVO searchVO);
	
	// 로그인 로그 등록
	public int loginLogInsert(CommonDTO dto);
	
	// 로그인 잠금해제
	public int loginLogUpdate(String mberId);
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	public CommonDTO homeInfo(String memCd);
	
	// 홈 게시글 조회
	public List<CommonDTO> bbsSelect();
	
	// 이력내역 전체조회
	public List<CommonDTO> historySelectAll();
	
	// 이력내역 단건조회
	public CommonDTO historyInfo(String histCd);
	
	// 이력내역 등록
	public boolean historyInsert(CommonDTO dto);
	
	// 회사 정보 조회
	public CommonDTO companyInfo();
	
	// 홈 일정 조회
	public List<CommonDTO> selectSchHome(String calDt);
	
	// 홈 일자별 일정건수 조회
	public List<CommonDTO> selectSchCntHome(String calDt);
	
	//회사 정보 수정
	public int updateCompanyInfo(CommonDTO commonDTO);
	
}