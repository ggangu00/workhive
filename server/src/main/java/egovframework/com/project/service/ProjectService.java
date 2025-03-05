package egovframework.com.project.service;

import java.util.List;

import egovframework.com.cmm.ComDefaultVO;

public interface ProjectService {
	
	//======================프로젝트=====================
	
	//프로젝트 전체조회
	public List<ProjectDTO> projectSelectAll(ComDefaultVO searchVO);
	
	//프로젝트 건수
	public int projectSelectAllCnt(ComDefaultVO searchVO);
	
	//프로젝트 단건조회
	public ProjectDTO projectSelect(String prCd);	
	
	//프로젝트 헤더/디테일 등록
	public boolean projectSave(ProjectDTO project);
	
	//프로젝트 수정
	public boolean projectUpdate(ProjectDTO project);
	
	//프로젝트 삭제
	public boolean projectDelete(String prCd);
	
	//프로젝트 다중 삭제
	public boolean projectListDelete(List<String> projectArr);
	
	//======================프로젝트 과업=====================
	
	//프로젝트 과업 전체조회
	public List<ProjectWorkDTO> projectWorkSelectAll(String prCd);
	
	//프로젝트 과업 단건조회
	public ProjectWorkDTO projectWorkSelect(String prWorkCd);	
	
	//======================프로젝트 일정=====================
	
	//프로젝트 일정 전체조회
	public List<ProjectDTO> projectPlanSelectAll(String prCd);
	
	//프로젝트 일정 단건조회
	public ProjectDTO projectPlanSelect(String prPlanCd);	
	
	//프로젝트 일정 등록
	public boolean projectPlanInsert(ProjectDTO project);
	
	//프로젝트 일정 삭제
	public boolean projectPlanDelete(String prPlanCd);
}
