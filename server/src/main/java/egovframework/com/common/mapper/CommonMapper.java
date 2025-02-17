package egovframework.com.common.mapper;

import java.util.List;

import egovframework.com.common.service.CommonDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("commonMapper")
public interface CommonMapper {
	List<CommonDTO> commonSelectAll(String commCd);
}