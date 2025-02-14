package egovframework.com.project.mapper;

import java.util.List;

import egovframework.com.project.service.ProjectDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("projectMapper")
public interface ProjectMapper {
	
	int insert(ProjectDTO projectDTO);
	int update(ProjectDTO projectDTO);
	int delete(Long prCd);
	ProjectDTO read(Long prCd);
	List<ProjectDTO> selectProjectList();
}
