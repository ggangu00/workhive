package egovframework.com.project.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
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
	public Map<String, Object> projectSelect(String prCd) {
		Map<String, Object> resultMap = new HashMap<>();
		
		// 프로젝트 정보 조회
	    List<EgovMap> projectInfo = projectMapper.projectSelect(prCd);
	    resultMap.put("result", projectInfo);
	    
	    // 프로젝트 참여자 명단 조회
	    List<EgovMap> attendeeList = projectMapper.projectSelectMember(prCd);
	    resultMap.put("list", attendeeList);
		
		return resultMap;
	}

	//프로젝트 등록	
	@Transactional
    public boolean projectSave(ProjectDTO project) {
		
		 try {
			 	//프로젝트 insert
		        projectMapper.projectInsert(project);
		        
		        //insert된 프로젝트 코드 추출
		        String prCd = projectMapper.getLastInsertedPrCd();
		        project.setPrCd(prCd);  // DTO에 prCd 설정

		        // 과업목록이 존재할 경우
		        if (project.getWorkArr() != null && !project.getWorkArr().isEmpty()) {
		            
		            for (ProjectWorkDTO work : project.getWorkArr()) {
		                work.setPrCd(prCd);  // 각 작업 항목에 prCd 설정
		                projectMapper.projectWorkInsert(work);  // 과업목록 INSERT 실행
		            }
		        }
		        
		        
		        //히스토리 테이블에 담을 정보 입력
		        CommonDTO dto = new CommonDTO();

		        dto.setTypeCd("T01"); //등록
		        dto.setTblNm("PROJECT"); //프로젝트 파트
		        dto.setCreateId(project.getCreateId()); //등록자
		        
		        commonMapper.historyInsert(dto); //히스토리 테이블 입력
		        
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
		        
		        if(project.getMode() == "edit") {
		        	projectMapper.projectWorkDelete(prCd); //과업 전체 삭제
		        }

		        // 작업 리스트가 존재할 경우
		        if (project.getWorkArr() != null && !project.getWorkArr().isEmpty()) {
		            
		            for (ProjectWorkDTO work : project.getWorkArr()) {
		                work.setPrCd(prCd);  // 각 작업 항목에 prCd 설정
		                projectMapper.projectWorkInsert(work);  // 개별 INSERT 실행
		            }
		        }
		        
		        CommonDTO dto = new CommonDTO();

		        dto.setTypeCd("T02");
		        dto.setTblNm("PROJECT");
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
	        dto.setTblNm("PROJECT");
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
	
	//프로젝트 일정수정
	@Override
    public boolean projectPlanUpdate(ProjectDTO project) {
		return projectMapper.projectPlanUpdate(project) == 1 ? true : false;
    }
	
	//프로젝트 일정삭제
	@Override
	public boolean projectPlanDelete(String prPlanCd) {
		return projectMapper.projectPlanDelete(prPlanCd) == 1 ? true : false;
	}
	
	//====================프로젝트 참여자 관리===================

	//프로젝트별 참여자 조회 트리
	@Override
	public List<ProjectDTO> projectTree(ComDefaultVO searchVO) {
		return projectMapper.projectTree(searchVO);
	}
	
	//프로젝트 참여자 추가
	@Override
	public boolean projectMemInsert(ProjectDTO projectDTO) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
        projectDTO.setCreateId(userId);
        
		return projectMapper.projectMemInsert(projectDTO) == 1 ? true : false;
	}
		
	//프로젝트 팀장 변경
	@Override
	public boolean projectManagerUpdate(ProjectDTO projectDTO) {
        return projectMapper.projectManagerUpdate(projectDTO) > 0 ? true : false;
	}
	
	//프로젝트 참여자 삭제
	@Override
	public boolean projectMemDelete(ProjectDTO projectDTO) {
		return projectMapper.projectMemDelete(projectDTO) == 1 ? true : false;
	}

}
