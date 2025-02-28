package egovframework.com.vacation.mapper;

import java.util.List;

import egovframework.com.vacation.service.VacationDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("vacationMapper")
public interface VacationMapper {
	int vcInsert(VacationDTO vcDTO);
	int vcUpdate(VacationDTO vcDTO);
	int vcDelete(String vcCd);
	VacationDTO vcSelect(String vcCD);
	List<VacationDTO> vcSelectAll(VacationDTO vcDTO);
	VacationDTO expectSelect(VacationDTO vcDTO);
	List<VacationDTO> vcSelectSigner(VacationDTO vcDTO);
	List<VacationDTO> vcSelectSigned(VacationDTO vcDTO);
	int signUpdate(VacationDTO vcDTO);
	
}
