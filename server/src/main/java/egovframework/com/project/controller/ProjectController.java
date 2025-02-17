package egovframework.com.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/project")
@Slf4j
public class ProjectController {
	
	@Resource
	private  ProjectService service;
	
	//프로젝트 전체조회
	@GetMapping("/list")
	public List<ProjectDTO> list() {
	  
	  List<ProjectDTO> result = service.projectSelectAll();
	  
	  return result;
	}	
	
	//프로젝트 단건조회
	@GetMapping("/info")
	public ProjectDTO info(@RequestParam(name="pr") String prCd) {
		ProjectDTO project = service.projectSelect(prCd);
		return project;
	}
	
	//게시글 등록	
	@GetMapping("/add")
	public void add(ProjectDTO project) {}
	
	//게시글 등록처리
	@PostMapping("/add")
	public String add(@Validated ProjectDTO project,
						   BindingResult bindingResult,
						   RedirectAttributes rttr) {
		
		if(bindingResult.hasErrors()) {
			return "fail";
		}
		
		 log.info("register: " + project);	  
		
	  service.projectInsert(project);
	  return "success";
	}
	
		
}