package egovframework.com.department.service;

import java.util.List;

public interface DepartmantService {
	
	// 부서 전체조회
	public List<DepartmentDTO> departmentSelectAll();
	
	// 부서 단건조회
	public DepartmentDTO departmentSelect(int deptCd);
}
