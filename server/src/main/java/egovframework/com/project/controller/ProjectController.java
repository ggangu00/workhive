package egovframework.com.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	@GetMapping("/list")
	public List<ProjectDTO> projectList() {
	  
	  List<ProjectDTO> result = proejctService.projectSelectAll();
	  
	  return result;
	}	
	
	//프로젝트 단건조회
	@GetMapping("/info")
	public ProjectDTO projectinfo(@RequestParam(name="pr") String prCd) {
		ProjectDTO project = proejctService.projectSelect(prCd);
		return project;
	}
	
	//게시글 등록
	@PostMapping("")
	public boolean proejectAdd(@Validated ProjectDTO project) {
		
		 log.info("register: " + project);	  
		
	  boolean result = proejctService.projectInsert(project);
	  return result;
	}
	
	//프로젝트 삭제
	@DeleteMapping("")
	public boolean projectRemove(@RequestParam(name="pr") String prCd) {
	  
	  boolean result = proejctService.projectDelete(prCd);
	  log.info("delete: " + result);	 
	  
	  
	  return result;
	}	
		
}