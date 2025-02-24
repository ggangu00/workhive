package egovframework.com.project.service;

import java.util.List;

import egovframework.com.cmm.ComDefaultVO;

public interface ProjectService {
	
	//======================프로젝트=====================
	
	//프로젝트 전체조회
	public List<ProjectDTO> projectSelectAll(ComDefaultVO searchVO);
	
	//프로젝트 단건조회
	public ProjectDTO projectSelect(String prCd);	
	
	//프로젝트 등록
	public boolean projectInsert(ProjectDTO project);
	
	//프로젝트 수정
	public boolean projectUpdate(ProjectDTO project);
	
	//프로젝트 삭제
	public boolean projectDelete(String prCd);
	
	//======================프로젝트 과업=====================
	
	//프로젝트 과업 전체조회
	public List<ProjectDTO> projectWorkSelectAll(String prCd);
	
	//프로젝트 과업 단건조회
	public ProjectDTO projectWorkSelect(String prWorkCd);	
		
	//프로젝트 과업 등록
	public boolean projectWorkInsert(ProjectDTO project);
	
	//프로젝트 과업 삭제
	public boolean projectWorkDelete(String prWorkCd);
	
	//======================프로젝트 일정=====================
	
	//프로젝트 일정 전체조회
	public List<ProjectDTO> projectPlanSelectAll(String prCd);
	
	//프로젝트 일정 단건조회
	public ProjectDTO projectPlanSelect(String prPlanCd);	
	
	//프로젝트 일정 등록
	public boolean projectPlanInsert(ProjectDTO project);
	
	//프로젝트 삭제
	public boolean projectPlanDelete(String prPlanCd);
}
