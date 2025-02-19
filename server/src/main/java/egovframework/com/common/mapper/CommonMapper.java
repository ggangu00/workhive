package egovframework.com.common.mapper;

import java.util.List;

import egovframework.com.common.service.CommonDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("commonMapper")
public interface CommonMapper {
	
	// 공통코드 디테일 전체조회
	List<CommonDTO> commonSelectAll(String commCd);
	
	// 거래처 전체조회
	List<CommonDTO> comSelectAll();
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	CommonDTO homeInfo(String memCd);
}