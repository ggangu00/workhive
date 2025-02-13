package egovframework.com.project.mapper;

import java.util.List;

import egovframework.com.project.dto.ProjectDTO;

public interface ProjectMapper {
	
	int insert(ProjectDTO projecDTO);
	int update(ProjectDTO projecDTO);
	int delete(Long prCd);
	ProjectDTO read(Long prCd);
	List<ProjectDTO> getList();
}
