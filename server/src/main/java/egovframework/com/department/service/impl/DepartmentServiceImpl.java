package egovframework.com.department.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.common.mapper.CommonMapper;
import egovframework.com.common.service.CommonDTO;
import egovframework.com.department.mapper.DepartmentMapper;
import egovframework.com.department.service.DepartmantService;
import egovframework.com.department.service.DepartmentDTO;
import egovframework.com.securing.service.CustomerUser;

@Service
public class DepartmentServiceImpl implements DepartmantService {
	
	@Resource private DepartmentMapper dMapper;
	@Resource private CommonMapper commMapper;

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
	@Transactional
	public boolean departmentInsert(DepartmentDTO dto) {
		 // 같은 부모를 가진 부서 중 마지막 `SEQ` 값 가져오기
	    int maxSeq = dMapper.getMaxSeq(dto.getParentCd(), dto.getDepth());
	    dto.setSeq(maxSeq + 1); // 마지막 순서 다음으로 설정

	    // 이력히스토리 관리를 위한 객체
		CommonDTO commDto = new CommonDTO();

		commDto.setTypeCd("T01");	// 등록
		commDto.setTblNm("DEPARTMENT"); // 이력이 일어난 테이블명 
		commDto.setCreateId(dto.getCreateId());
        
		commMapper.historyInsert(commDto);
		
		return dMapper.departmentInsert(dto) == 1 ? true : false ;
	}

	// 부서 수정
	@Transactional
	public boolean departmentUpdate(DepartmentDTO dto) {
		// 이력히스토리 관리를 위한 객체
		CommonDTO commDto = new CommonDTO();

		commDto.setTypeCd("T02");	// 수정
		commDto.setTblNm("DEPARTMENT"); // 이력이 일어난 테이블명 
		commDto.setCreateId(dto.getCreateId());
        
		commMapper.historyInsert(commDto);
		return dMapper.departmentUpdate(dto) == 1 ? true : false ;
	}

	// 부서 삭제
	@Transactional
	public int departmentDelete(List<String> deptCdList) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        
        // 이력히스토리 관리를 위한 객체	
		CommonDTO commDto = new CommonDTO();

		commDto.setTypeCd("T03");	// 삭제
		commDto.setTblNm("DEPARTMENT"); // 이력이 일어난 테이블명 
		commDto.setCreateId(user.getUserDTO().getMberId());
        
		commMapper.historyInsert(commDto);
		
		return dMapper.departmentDelete(deptCdList);
	}
	
	// 부서 트리 조회
	@Override
	public List<DepartmentDTO> deptTreeSelectAll(String deptCd) {
		return dMapper.deptTreeSelectAll(deptCd);
	}
	
}
