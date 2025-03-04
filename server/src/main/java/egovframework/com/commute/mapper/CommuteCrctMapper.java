package egovframework.com.commute.mapper;

import java.util.List;

import egovframework.com.commute.service.CommuteCrctDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("crctMapper")
public interface CommuteCrctMapper {

	// 출퇴근 정정
	int crctInsert(CommuteCrctDTO crctDTO);
	int crctUpdate(CommuteCrctDTO crctDTO);
	int crctDelete(String crctCd);
	CommuteCrctDTO crctSelect(String crctCd);
	List<CommuteCrctDTO> crctSelectAll(CommuteCrctDTO crctDTO);
	List<CommuteCrctDTO> crctSelectSigner(CommuteCrctDTO crctDTO);
	List<CommuteCrctDTO> crctSelectSigned(CommuteCrctDTO crctDTO);
	int signUpdate(CommuteCrctDTO crctDTO);
	int crctSelectAllCnt(CommuteCrctDTO crctDTO);
	int crctSelectSignerCnt(CommuteCrctDTO crctDTO);
	int crctSelectSignedCnt(CommuteCrctDTO crctDTO);
}
