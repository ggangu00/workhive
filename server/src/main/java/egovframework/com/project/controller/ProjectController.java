package egovframework.com.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.javaparser.utils.Log;

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
	@GetMapping("")
	public List<ProjectDTO> list() {
	  
	  List<ProjectDTO> result = service.getList();
	  Log.info(result.toString());
	  
	  return result;
	}	
	
	//프로젝트 단건조회
	@GetMapping("/get")
	public ProjectDTO modify(@RequestParam(name="prCd") String prCd, Model model) {
		ProjectDTO project = service.get("1");
		Log.info("12313123123121223" + project.toString());
		return project;
	}
	
		
}