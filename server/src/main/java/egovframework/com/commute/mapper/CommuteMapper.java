package egovframework.com.commute.mapper;

import java.util.List;

import egovframework.com.commute.service.CommuteDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("commuteMapper")
public interface CommuteMapper {
	
	// 출퇴근
	int cmtInsert(CommuteDTO commuteDTO);
	int cmtUpdate(CommuteDTO commuteDTO);
	int cmtDelete(String commuteCd);
	CommuteDTO cmtSelect(String commuteCd);
	List<CommuteDTO> cmtSelectAll(String memCd);
	CommuteDTO lastCmtSelect(String memCd);
	
	
	// 출퇴근 정정
}
