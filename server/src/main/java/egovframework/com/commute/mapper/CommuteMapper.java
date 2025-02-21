package egovframework.com.commute.mapper;

import java.util.List;

import egovframework.com.commute.service.CommuteDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("commuteMapper")
public interface CommuteMapper {
	
	// 출퇴근
	int cmtInsert(CommuteDTO commuteDTO);
	int cmtUpdate(CommuteDTO commuteDTO);
	int crctSignUpdate(CommuteDTO commuteDTO);
	int cmtDelete(String commuteCd);
	CommuteDTO cmtSelect(String commuteCd);
	List<CommuteDTO> cmtSelectAll(CommuteDTO commuteDTO);
	CommuteDTO lastCmtSelect(String mberId);
	CommuteDTO dateCmtSelect(String commuteDt);
	CommuteDTO cmtTimeSelect(); // 차후 회사 정보 설정에 따라 사업자 번호 받아서 검색
}
