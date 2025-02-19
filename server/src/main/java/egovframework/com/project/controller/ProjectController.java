package egovframework.com.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/project")
@Slf4j
public class ProjectController {
	
	@Resource
	private  ProjectService proejctService;
	
	//프로젝트 전체조회
	@GetMapping("")
	public List<ProjectDTO> projectList() {	  
	  return proejctService.projectSelectAll();
	}	
	
	//프로젝트 단건조회
	@GetMapping("/{prCd}")
	public Map<String, Object> projectinfo(@RequestParam(name="prCd") String prCd) {
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 정보조회
		ProjectDTO infoDto = proejctService.projectSelect(prCd);
		
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
	public Map<String, Object> proejectAdd(@Validated ProjectDTO project) {
		
	  boolean result = proejctService.projectInsert(project);
	  	  
	  Map<String, Object> map = new HashMap<>();
	  map.put("result", result);
	  map.put("list", proejctService.projectSelectAll());
		
	  return map;
	}
	
	//프로젝트 수정
	@PutMapping("")
	public Map<String, Object> projectModify(@RequestBody ProjectDTO dto) {
		log.info("수정 권한 코드 출력 => " + dto.toString());
		
		Map<String, Object> map = new HashMap<>();
		
		boolean result = proejctService.projectUpdate(dto);
		
		map.put("result", result);
		map.put("list", proejctService.projectSelectAll());
		
		return map;
	}
	
	//프로젝트 삭제
	@DeleteMapping("/{prCd}")
	public Map<String, Object> projectRemove(@PathVariable(name="prCd") String prCd) {
		log.info("삭제 권한 코드 출력 => " + prCd);
	    
	    // 서비스 로직 실행
		boolean result = proejctService.projectDelete(prCd);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", proejctService.projectSelectAll());
		
		return map;
	}
	
	//프로젝트 과업조회
	@GetMapping("/work/{prCd}")
	public List<ProjectDTO> projectWorkList(@PathVariable(name="prCd") String prCd) {	  
	  return proejctService.projectWorkSelect(prCd);
	}	
	
	//프로젝트 과업등록
	@PostMapping("/work")
	public Map<String, Object> proejectWorkAdd(@Validated ProjectDTO project) {
		
	  boolean result = proejctService.projectWorkInsert(project);
	  	  
	  Map<String, Object> map = new HashMap<>();
	  map.put("result", result);
	  map.put("list", proejctService.projectWorkSelect(project.getPrCd()));
		
	  return map;
	}
	
	//프로젝트 일정조회
	@GetMapping("/plan/{prCd}")
	public List<ProjectDTO> projectPlanList(@PathVariable(name="prCd") String prCd) {	  
	  return proejctService.projectPlanSelect(prCd);
	}	
	
	//프로젝트 과업등록
	@PostMapping("/plan")
	public Map<String, Object> proejectPlanAdd(@Validated ProjectDTO project) {
		
	  boolean result = proejctService.projectPlanInsert(project);
	  
	  log.info("12312312312312 => " + result);
	  	  
	  Map<String, Object> map = new HashMap<>();
	  map.put("result", result);
	  map.put("list", proejctService.projectPlanSelect(project.getPrCd()));
		
	  return map;
	}
		
}