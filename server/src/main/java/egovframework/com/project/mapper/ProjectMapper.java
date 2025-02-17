package egovframework.com.project.mapper;

import java.util.List;

import egovframework.com.project.service.ProjectDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("projectMapper")
public interface ProjectMapper {
	
	int projectInsert(ProjectDTO projectDTO);
	int projectUpdate(ProjectDTO projectDTO);
	int projectDelete(String prCd);
	ProjectDTO projectSelect(String prCd);
	List<ProjectDTO> projectSelectAll();
}
