package egovframework.com.commute.service;

import java.util.List;

public interface CommuteCrctService {

	// 출퇴근 정정
	public boolean crctInsert(CommuteCrctDTO crctDTO);
	public boolean crctUpdate(CommuteCrctDTO crctDTO);
	public boolean crctDelete(String crctCd);
	public CommuteCrctDTO crctSelect(String crctCd);
	public List<CommuteCrctDTO> crctSelectAll(CommuteCrctDTO crctDTO);
	public List<CommuteCrctDTO> crctSelectSigner(CommuteCrctDTO crctDTO);
	public List<CommuteCrctDTO> crctSelectSigned(CommuteCrctDTO crctDTO);
	public boolean signUpdate(CommuteCrctDTO crctDTO);
	public int crctSelectAllCnt(CommuteCrctDTO crctDTO);
	public int crctSelectSignerCnt(CommuteCrctDTO crctDTO);
	public int crctSelectSignedCnt(CommuteCrctDTO crctDTO);
}
