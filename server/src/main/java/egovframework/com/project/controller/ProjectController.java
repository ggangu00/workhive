package egovframework.com.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;
import egovframework.com.project.service.ProjectWorkDTO;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/project")
@Slf4j
public class ProjectController {
	
	@Resource
	private  ProjectService projectService;
	
	 @Resource(name = "propertiesService")
	 protected EgovPropertyService propertiesService;
	
	//======================프로젝트=====================
	
	//프로젝트 전체조회
	@GetMapping("/list")
	public List<ProjectDTO> projectList(ComDefaultVO searchVO, ModelMap model) {	  
		/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));
  	  
	  	//목록 조회
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
	  	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	  	
		List<ProjectDTO> projectList = projectService.projectSelectAll(searchVO);
				
		int totCnt = projectService.projectSelectAllCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("list", projectList); 
		
		return projectList;
	}	
	
	//프로젝트 단건조회
	@GetMapping("/info/{prCd}")
	public Map<String, Object> projectinfo(@PathVariable("prCd") String prCd) {
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 정보조회
		ProjectDTO infoDto = projectService.projectSelect(prCd);
		
		if (infoDto != null) {
	        map.put("result", true);
	        map.put("info", infoDto);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	//프로젝트 등록
	@PostMapping("")
	public boolean projectAdd(@RequestBody ProjectDTO project) {
		boolean result = projectService.saveProject(project);
		
	  return result;
	}
	
	//프로젝트 수정
	@PutMapping("")
	public Map<String, Object> projectModify(@RequestBody ProjectDTO dto) {
		
		Map<String, Object> map = new HashMap<>();
		
		boolean result = projectService.projectUpdate(dto);
		
		map.put("result", result);
		
		return map;
	}
	
	//프로젝트 삭제
	@DeleteMapping("/{prCd}")
	public Map<String, Object> projectRemove(@PathVariable(name="prCd") String prCd) {
	    
	    // 서비스 로직 실행
		boolean result = projectService.projectDelete(prCd);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		//map.put("list", projectService.projectSelectAll());
		
		return map;
	}
	
	//프로젝트 다중 삭제
	@PutMapping("/delete")
	public boolean projectListRemove(@RequestBody Map<String, List<String>> requestBody) {
	    List<String> projectArr = requestBody.get("projectArr");
	    
	    if (projectArr == null || projectArr.isEmpty()) {
	        return false;
	    }

	    return projectService.projectListDelete(projectArr);
	}
	
	//======================프로젝트 과업=====================
	
	//프로젝트 과업조회
	@GetMapping("/work/{prCd}")
	public List<ProjectWorkDTO> projectWorkList(@PathVariable(name="prCd") String prCd) {	  
	  return projectService.projectWorkSelectAll(prCd);
	}	

	//프로젝트 과업 단건조회
	@GetMapping("/work/info/{prWorkCd}")
	public Map<String, Object> projectWorkinfo(@PathVariable("prWorkCd") String prWorkCd) {
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 과업 정보조회
		ProjectWorkDTO infoDto = projectService.projectWorkSelect(prWorkCd);
		
		if (infoDto != null) {
	        map.put("result", true);
	        map.put("info", infoDto);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	//프로젝트 과업 삭제
	@DeleteMapping("/work/{prWorkCd}")
	public Map<String, Object> projectWorkRemove(@PathVariable("prWorkCd") String prWorkCd) {
		log.info("삭제 권한 코드 출력 => " + prWorkCd);
	    
	    // 서비스 로직 실행
		boolean result = projectService.projectWorkDelete(prWorkCd);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", projectService.projectWorkSelectAll(prWorkCd));
		
		return map;
	}
	
	//======================프로젝트 일정=====================
	
	//프로젝트 일정조회
	@GetMapping("/plan/{prCd}")
	public List<ProjectDTO> projectPlanList(@PathVariable("prCd") String prCd) {
		List<ProjectDTO> result = projectService.projectPlanSelectAll(prCd);
		return result;
	}	
	
	//프로젝트 일정 단건조회
	@GetMapping("/plan/info/{prPlanCd}")
	public Map<String, Object> projectPlaninfo(@PathVariable("prPlanCd") String prPlanCd) {
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 정보조회
		ProjectDTO infoDto = projectService.projectPlanSelect(prPlanCd);
		
		if (infoDto != null) {
	        map.put("result", true);
	        map.put("info", infoDto);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	//프로젝트 일정등록
	@PostMapping("/plan")
	public Map<String, Object> projectPlanAdd(@Validated ProjectDTO project) {
		
	  boolean result = projectService.projectPlanInsert(project);	  
	  	  
	  Map<String, Object> map = new HashMap<>();
	  map.put("result", result);
	  map.put("list", projectService.projectPlanSelectAll(project.getPrCd()));
		
	  return map;
	}
	
	//프로젝트 일정 삭제
	@DeleteMapping("/plan/{prPlanCd}")
	public Map<String, Object> projectPlanRemove(@PathVariable("prPlanCd") String prPlanCd) {
		log.info("삭제 권한 코드 출력 => " + prPlanCd);
	    
	    // 서비스 로직 실행
		boolean result = projectService.projectPlanDelete(prPlanCd);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", projectService.projectPlanSelectAll(prPlanCd));
		
		return map;
	}
		
}