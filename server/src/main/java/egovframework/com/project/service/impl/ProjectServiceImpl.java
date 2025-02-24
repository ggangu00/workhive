package egovframework.com.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.project.mapper.ProjectMapper;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;

@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService{
	
	@Resource
	private ProjectMapper projectMapper;
	
	//======================프로젝트=====================
	
	//프로젝트 전체조회
	@Override
	public List<ProjectDTO> projectSelectAll(ComDefaultVO searchVO) {
		return projectMapper.projectSelectAll(searchVO);
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
	
	//======================프로젝트 과업=====================
	
	//프로젝트 과업 전체조회
	@Override
	public List<ProjectDTO> projectWorkSelectAll(String prCd) {
		return projectMapper.projectWorkSelectAll(prCd);
	}
	
	//프로젝트 과업 단건조회
	@Override
	public ProjectDTO projectWorkSelect(String prWorkCd) {
		return projectMapper.projectWorkSelect(prWorkCd);
	}

	//프로젝트 과업등록
	@Override
	public boolean projectWorkInsert(ProjectDTO project) {
		return projectMapper.projectWorkInsert(project) == 1 ? true : false;
	}
	
	//프로젝트 과업삭제
	@Override
	public boolean projectWorkDelete(String prWorkCd) {
		return projectMapper.projectPlanDelete(prWorkCd) == 1 ? true : false;
	}
	
	//======================프로젝트 일정=====================
	
	//프로젝트 일정 전체조회
	@Override
	public List<ProjectDTO> projectPlanSelectAll(String prCd) {
		return projectMapper.projectPlanSelectAll(prCd);
	}
	
	//프로젝트 일정 단건조회
	@Override
	public ProjectDTO projectPlanSelect(String prPlanCd) {
		return projectMapper.projectPlanSelect(prPlanCd);
	}

	//프로젝트 일정등록
	@Override
	public boolean projectPlanInsert(ProjectDTO project) {
		return projectMapper.projectPlanInsert(project) == 1 ? true : false;
	}	
	
	//프로젝트 일정삭제
	@Override
	public boolean projectPlanDelete(String prPlanCd) {
		return projectMapper.projectPlanDelete(prPlanCd) == 1 ? true : false;
	}
}
