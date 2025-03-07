package egovframework.com.department.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.department.mapper.DepartmentMapper;
import egovframework.com.department.service.DepartmantService;
import egovframework.com.department.service.DepartmentDTO;

@Service
public class DepartmentServiceImpl implements DepartmantService {
	
	@Resource DepartmentMapper dMapper;

	// 부서 전체조회
	@Override
	public List<DepartmentDTO> departmentSelectAll() {
		return dMapper.departmentSelectAll();
	}

	// 부서 단건조회
	@Override
	public DepartmentDTO departmentSelect(int deptCd) {
		return dMapper.departmentSelect(deptCd);
	}

	// 부서 등록
	@Override
	public boolean departmentInsert(DepartmentDTO dto) {
		return dMapper.departmentInsert(dto) == 1 ? true : false ;
	}

	// 부서 수정
	@Override
	public boolean departmentUpdate(DepartmentDTO dto) {
		return dMapper.departmentUpdate(dto) == 1 ? true : false ;
	}

	// 부서 삭제
	@Override
	public boolean departmentDelete(String deptCd) {
		return dMapper.departmentDelete(deptCd) == 1 ? true : false ;
	}
	
	// 부서 트리 조회
	@Override
	public List<DepartmentDTO> deptTreeSelectAll(String deptCd) {
		return dMapper.deptTreeSelectAll(deptCd);
	}
	
}
