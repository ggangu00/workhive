package egovframework.com.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.project.mapper.ProjectMapper;
import egovframework.com.project.service.ProjectDTO;
import egovframework.com.project.service.ProjectService;

@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService{
	
	@Resource
	private ProjectMapper projectMapper;

	@Override
	public void projectInsert(ProjectDTO project) {
		projectMapper.projectInsert(project);		
	}

	@Override
	public boolean projectUpdate(ProjectDTO project) {
		return projectMapper.projectUpdate(project) == 1 ? true : false;
	}

	@Override
	public boolean projectDelete(String prCd) {
		return projectMapper.projectDelete(prCd) == 1 ? true : false;
	}

	@Override
	public ProjectDTO projectSelect(String prCd) {
		return projectMapper.projectSelect(prCd);
	}

	@Override
	public List<ProjectDTO> projectSelectAll() {
		return projectMapper.projectSelectAll();
	}
}
