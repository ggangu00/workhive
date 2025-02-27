package egovframework.com.vacation.mapper;

import egovframework.com.vacation.service.YearVcDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("yearVcMapper")
public interface YearVcMapper {
	int yearVcInsert(YearVcDTO yearVcDTO);
	int yearVcUpdate(YearVcDTO yearVcDTO);
	YearVcDTO yearVcSelect(YearVcDTO yearVcDTO);

}
