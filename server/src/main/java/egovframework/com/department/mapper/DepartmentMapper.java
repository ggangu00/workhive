package egovframework.com.department.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.com.department.service.DepartmentDTO;
import egovframework.com.securing.service.UserDTO;
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
	int departmentDelete(List<String> deptCdList);
	
	// 부서 트리 조회
	List<DepartmentDTO> deptTreeSelectAll(String deptCd);

	// 부서 등록 할 때 같은 계층의 마지막 순서 가져오기
	int getMaxSeq(@Param("parentCd") String parentCd, @Param("depth") int depth);
	
	// 부서장 조회
	UserDTO departmentManagerSelect(String deptCd);

	int departmentToMemberUpdate(@Param("paramMap") Map<String, Object> paramMap);

}
