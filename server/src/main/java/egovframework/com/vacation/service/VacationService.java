package egovframework.com.vacation.service;

import java.util.List;

public interface VacationService {

	public boolean vcInsert(VacationDTO vcDTO);
	public boolean vcUpdate(VacationDTO vcDTO);
	public boolean vcDelete(String vcCd);
	public VacationDTO vcSelect(String vcCD);
	public List<VacationDTO> vcSelectAll(VacationDTO vcDTO);
	public VacationDTO expectSelect(String createId);
	public List<VacationDTO> vcSelectSigner(VacationDTO vcDTO);
	public List<VacationDTO> vcSelectSigned(VacationDTO vcDTO);
}
