package egovframework.com.project.mapper;

import java.util.List;

import egovframework.com.project.service.ProjectDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("projectMapper")
public interface ProjectMapper {
	
	// 프로젝트 전체조회
	public List<ProjectDTO> projectSelectAll();
	
	// 프로젝트 단건조회
	public ProjectDTO projectSelect(String prCd);
	
	// 프로젝트 등록
	public int projectInsert(ProjectDTO projectDTO);
	
	// 프로젝트 수정
	public int projectUpdate(ProjectDTO projectDTO);
	
	// 프로젝트 삭제
	public int projectDelete(String prCd);
	
	// 프로젝트 과업조회
	public List<ProjectDTO> projectWorkSelect(String prCd);
		
	// 프로젝트 과업등록
	public int projectWorkInsert(ProjectDTO projectDTO);
	
	// 프로젝트 일정조회
	public List<ProjectDTO> projectPlanSelect(String prCd);

	// 프로젝트 일정등록
	public int projectPlanInsert(ProjectDTO projectDTO);
}
