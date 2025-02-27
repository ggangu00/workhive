package egovframework.com.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.project.mapper.ProjectMapper;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;
import egovframework.com.project.service.ProjectWorkDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	//프로젝트 전체건수
	@Override
	public int projectSelectAllCnt(ComDefaultVO searchVO) {
		return projectMapper.projectSelectAllCnt(searchVO);
	}
	
	//프로젝트 단건조회
	@Override
	public ProjectDTO projectSelect(String prCd) {
		return projectMapper.projectSelect(prCd);
	}

	//프로젝트 등록
	@Override
	public boolean projectInsert(ProjectDTO project) {
		return projectMapper.projectInsert(project) == 1 ? true : false;
	}	
	
	@Transactional
    public boolean saveProject(ProjectDTO project) {
		 try {
		        projectMapper.projectInsert(project);
		        
		        String prCd = projectMapper.getLastInsertedPrCd();
		        project.setPrCd(prCd);  // DTO에 prCd 설정

		        // 작업 리스트가 존재할 경우
		        if (project.getWorkArr() != null && !project.getWorkArr().isEmpty()) {
		            
		            for (ProjectWorkDTO work : project.getWorkArr()) {
		                work.setPrCd(prCd);  // 각 작업 항목에 prCd 설정
		                projectMapper.projectWorkInsert(work);  // 개별 INSERT 실행
		            }
		        }
		        return true; 
		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    }
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
	
	//프로젝트 다중 삭제
	public boolean projectListDelete(List<String> projectArr) {
        return projectMapper.projectListDelete(projectArr) == 1 ? true : false;
    }
	
	//======================프로젝트 과업=====================
	
	//프로젝트 과업 전체조회
	@Override
	public List<ProjectWorkDTO> projectWorkSelectAll(String prCd) {
		return projectMapper.projectWorkSelectAll(prCd);
	}
	
	//프로젝트 과업 단건조회
	@Override
	public ProjectWorkDTO projectWorkSelect(String prWorkCd) {
		return projectMapper.projectWorkSelect(prWorkCd);
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
