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
}