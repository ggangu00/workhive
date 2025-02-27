package egovframework.com.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectWorkDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("projectMapper")
public interface ProjectMapper {

	//======================프로젝트=====================
	
	// 프로젝트 전체조회
	public List<ProjectDTO> projectSelectAll(ComDefaultVO searchVO);
	
	// 프로젝트 전체건수
	public int projectSelectAllCnt(ComDefaultVO searchVO);
	
	// 프로젝트 단건조회
	public ProjectDTO projectSelect(String prCd);
	
	// 프로젝트 등록
	public int projectInsert(ProjectDTO projectDTO);
	
	// 프로젝트 수정
	public int projectUpdate(ProjectDTO projectDTO);
	
	// 프로젝트 삭제
	public int projectDelete(String prCd);
	
	// 프로젝트 다중 삭제
	public int projectListDelete(List<String> projectArr);
	
	//======================프로젝트 과업=====================
	
	// 프로젝트 과업 전체조회
	public List<ProjectWorkDTO> projectWorkSelectAll(String prCd);
	
	// 프로젝트 과업 단건조회
	public ProjectWorkDTO projectWorkSelect(String prWorkCd);
		
	// 프로젝트 과업등록
	public int projectWorkInsert(@Param("list") Map<String, Object> paramMap);
	
	// 프로젝트 과업삭제
	public int projectWorkDelete(String prWorkCd);
	
	//======================프로젝트 일정=====================
	
	// 프로젝트 일정 전체조회
	public List<ProjectDTO> projectPlanSelectAll(String prCd);
	
	// 프로젝트 일정 단건조회
	public ProjectDTO projectPlanSelect(String prPlanCd);

	// 프로젝트 일정등록
	public int projectPlanInsert(ProjectDTO projectDTO);
	
	// 프로젝트 일정삭제
	public int projectPlanDelete(String prPlanCd);

	public String getLastInsertedPrCd();
}
