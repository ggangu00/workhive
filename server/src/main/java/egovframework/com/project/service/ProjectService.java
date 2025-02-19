package egovframework.com.project.service;

import java.util.List;

public interface ProjectService {
	
	//프로젝트 전체조회
	public List<ProjectDTO> projectSelectAll();
	
	//프로젝트 단건조회
	public ProjectDTO projectSelect(String prCd);	
	
	//프로젝트 등록
	public boolean projectInsert(ProjectDTO project);
	
	//프로젝트 수정
	public boolean projectUpdate(ProjectDTO project);
	
	//프로젝트 삭제
	public boolean projectDelete(String prCd);
	
	//프로젝트 과업조회
	public List<ProjectDTO> projectWorkSelect(String prCd);
		
	//프로젝트 과업 등록
	public boolean projectWorkInsert(ProjectDTO project);
	
	//프로젝트 일정조회
	public List<ProjectDTO> projectPlanSelect(String prCd);
	
	//프로젝트 일정 등록
	public boolean projectPlanInsert(ProjectDTO project);
}
