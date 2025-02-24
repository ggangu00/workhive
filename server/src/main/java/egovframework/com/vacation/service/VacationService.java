package egovframework.com.vacation.service;

import java.util.List;

public interface VacationService {

	public boolean vcInsert(VacationDTO vcDTO);
	public boolean vcUpdate(VacationDTO vcDTO);
	public boolean vcDelete(String vcCd);
	public VacationDTO vcSelete(String vcCD);
	public List<VacationDTO> vcSelectAll(VacationDTO vcDTO);
}
