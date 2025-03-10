package egovframework.com.commute.service;

import java.util.List;

public interface CommuteService {

	// 출퇴근
	public boolean cmtInsert(CommuteDTO commuteDTO);
	public boolean cmtUpdate(CommuteDTO commuteDTO);
	public boolean crctSignUpdate(CommuteDTO commuteDTO);
	public boolean cmtDelete(String commuteCd);
	public CommuteDTO cmtSelect(String commuteCd);
	public List<CommuteDTO> cmtSelectAll(CommuteDTO commuteDTO);
	public CommuteDTO lastCmtSelect(String mberId);
	public CommuteDTO dateCmtSelect(String commuteDt);
	public CommuteDTO cmtTimeSelect();
	public int cmtSelectAllCnt(CommuteDTO commuteDTO);
}
