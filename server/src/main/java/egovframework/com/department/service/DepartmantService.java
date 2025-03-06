package egovframework.com.department.service;

import java.util.List;

public interface DepartmantService {
	
	// 부서 전체조회
	public List<DepartmentDTO> departmentSelectAll();
	
	// 부서 단건조회
	public DepartmentDTO departmentSelect(int deptCd);
	
	// 부서 등록
	public boolean departmentInsert(DepartmentDTO dto);
	
	// 부서 수정
	public boolean departmentUpdate(DepartmentDTO dto);
	
	// 부서 삭제
	public boolean departmentDelete(String deptCd);

	// 부서 트리 조회
	public List<DepartmentDTO> deptTreeSelectAll(String deptCd);
}
