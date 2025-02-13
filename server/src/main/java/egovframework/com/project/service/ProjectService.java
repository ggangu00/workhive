package egovframework.com.project.service;

import java.util.List;

import egovframework.com.project.dto.ProjectDTO;

public interface ProjectService {
	public void projectInsert(ProjectDTO project);
	public boolean projectUpdate(ProjectDTO project);
	public boolean projectDelete(Long prCd);
	public ProjectDTO projectSelect(Long prCd);
	public List<ProjectDTO> projectSelectAll();
}
