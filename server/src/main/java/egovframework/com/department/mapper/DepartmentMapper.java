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

	// 부서 등록
	int departmentInsert(DepartmentDTO dto);
	
	// 부서 수정
	int departmentUpdate(DepartmentDTO dto);

	// 부서 삭제
	int departmentDelete(String deptCd);
	
	// 부서 트리 조회
	List<DepartmentDTO> deptTreeSelectAll(String deptCd);
}
