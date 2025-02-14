package egovframework.com.project.service;

import java.util.List;

public interface ProjectService {
	
	public void projectInsert(ProjectDTO project);
	
	public boolean projectUpdate(ProjectDTO project);
	
	public boolean projectDelete(String prCd);
	
	public ProjectDTO get(String prCd);
	
	public List<ProjectDTO> getList();
	
}
