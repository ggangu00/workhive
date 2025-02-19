package egovframework.com.department.mapper;

import java.util.List;

import egovframework.com.department.service.DepartmentDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("departmentMapper")
public interface DepartmentMapper {

	// 부서 전체조회
	List<DepartmentDTO> departmentSelectAll();

	// 부서 단건조회
	DepartmentDTO departmentSelect(int deptCd);

}
