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
	
}
