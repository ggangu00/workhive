package egovframework.com.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	//게시글 전체조회
	@GetMapping("")
	public List<ProjectDTO> list() {
	  
	  List<ProjectDTO> result = service.selectProjectList();
	  System.out.println("asfsdfsdfdsfdsfsafasfsfsdfs" + result.toString());
	  Log.info(result.toString());
	  
	  return result;
	}	
	
		
}