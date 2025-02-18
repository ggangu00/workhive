package egovframework.com.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.project.mapper.ProjectMapper;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;

@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService{
	
	@Resource
	private ProjectMapper projectMapper;
	
	//프로젝트 전체조회
	@Override
	public List<ProjectDTO> projectSelectAll() {
		return projectMapper.projectSelectAll();
	}
	
	//프로젝트 단건조회
	@Override
	public ProjectDTO projectSelect(String prCd) {
		return projectMapper.projectSelect(prCd);
	}

	//프로젝트 등록
	@Override
	public boolean projectInsert(ProjectDTO project) {
		if (project.getPrNm() == null) {
            throw new IllegalArgumentException("프로젝트명은 필수입니다.");
        }else if (project.getTypeCd() == null) {
            throw new IllegalArgumentException("프로젝트 구분값은 필수입니다.");
        }else if (project.getStartDt() == null || project.getEndDt() == null) {
            throw new IllegalArgumentException("프로젝트 기간은 필수입니다.");
        }
		return projectMapper.projectInsert(project) == 1 ? true : false;
	}	

	//프로젝트 수정
	@Override
	public boolean projectUpdate(ProjectDTO project) {
		return projectMapper.projectUpdate(project) == 1 ? true : false;
	}

	//프로젝트 삭제
	@Override
	public boolean projectDelete(String prCd) {
		return projectMapper.projectDelete(prCd) == 1 ? true : false;
	}
	
	//프로젝트 과업조회
	@Override
	public List<ProjectDTO> projectWorkSelect(String prCd) {
		return projectMapper.projectWorkSelect(prCd);
	}

	//프로젝트 과업 등록
	@Override
	public boolean projectWorkInsert(ProjectDTO project) {
		return projectMapper.projectWorkInsert(project) == 1 ? true : false;
	}
	
	//프로젝트 일정조회
	@Override
	public List<ProjectDTO> projectPlanSelect(String prCd) {
		return projectMapper.projectPlanSelect(prCd);
	}

	//프로젝트 일정 등록
	@Override
	public boolean projectPlanInsert(ProjectDTO project) {
		return projectMapper.projectPlanInsert(project) == 1 ? true : false;
	}	
}
