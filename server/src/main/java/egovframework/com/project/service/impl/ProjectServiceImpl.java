package egovframework.com.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.common.mapper.CommonMapper;
import egovframework.com.common.service.CommonDTO;
import egovframework.com.project.mapper.ProjectMapper;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;
import egovframework.com.project.service.ProjectWorkDTO;
import egovframework.com.securing.service.CustomerUser;
import lombok.extern.slf4j.Slf4j;

@Service("ProjectService")
@Slf4j
public class ProjectServiceImpl implements ProjectService{
	
	@Resource
	private ProjectMapper projectMapper;
	
	@Resource
	private CommonMapper commonMapper;
	
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
	@Transactional
    public boolean projectSave(ProjectDTO project) {
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
		        
		        CommonDTO dto = new CommonDTO();

		        dto.setTypeCd("T01");
		        dto.setTblNm("PROJECT");
		        dto.setCreateId(project.getCreateId());
		        
		        commonMapper.historyInsert(dto);
		        return true; 
		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    }
    }

	//프로젝트 수정
	@Transactional
    public boolean projectUpdate(ProjectDTO project) {
		 try {
		        projectMapper.projectUpdate(project);
		        
		        String prCd = project.getPrCd();
		        
		        projectMapper.projectWorkDelete(prCd); //과업 전체 삭제

		        // 작업 리스트가 존재할 경우
		        if (project.getWorkArr() != null && !project.getWorkArr().isEmpty()) {
		            
		            for (ProjectWorkDTO work : project.getWorkArr()) {
		                work.setPrCd(prCd);  // 각 작업 항목에 prCd 설정
		                projectMapper.projectWorkInsert(work);  // 개별 INSERT 실행
		            }
		        }
		        
		        CommonDTO dto = new CommonDTO();

		        dto.setTypeCd("T02");
		        dto.setTblNm("project");
		        dto.setCreateId(project.getUpdateId());
		        
		        commonMapper.historyInsert(dto);
		        
		        return true; 
		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    }
    }

	//프로젝트 삭제
	@Override
	public boolean projectDelete(String prCd) {
		
		if(projectMapper.projectDelete(prCd) == 1) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        CustomerUser user = (CustomerUser) auth.getPrincipal();
	        String userId = user.getUserDTO().getMberId();
	        
	        CommonDTO dto = new CommonDTO();
	
	        dto.setTypeCd("T03");
	        dto.setTblNm("project");
	        dto.setCreateId(userId);
	        
	        commonMapper.historyInsert(dto);
		}
        
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
	
	//====================프로젝트 참여자 관리===================

	//프로젝트별 참여자 조회 트리
	@Override
	public List<ProjectDTO> projectTree() {
		return projectMapper.projectTree();
	}

}
