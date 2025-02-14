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
		projectMapper.insert(project);
		
	}

	@Override
	public boolean projectUpdate(ProjectDTO project) {
		return projectMapper.update(project) == 1 ? true : false;
	}

	@Override
	public boolean projectDelete(Long prCd) {
		return projectMapper.delete(prCd) == 1 ? true : false;
	}

	@Override
	public ProjectDTO projectSelect(Long prCd) {
		return projectMapper.read(prCd);
	}

	@Override
	public List<ProjectDTO> selectProjectList() {
		return projectMapper.selectProjectList();
	}

}
