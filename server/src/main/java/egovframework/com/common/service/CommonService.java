package egovframework.com.common.service;

import java.util.List;

public interface CommonService {
	// 공통코드 디테일 전체조회
	public List<CommonDTO> commonSelectAll(String commCd);
	
	// 거래처 전체조회
	public List<CommonDTO> comSelectAll();
}
