package egovframework.com.approval.mapper;

import java.util.List;

import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.FormDTO;
import egovframework.com.approval.service.SearchDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("documentMapper")
public interface DocumentMapper {
	//등록
	int insert(DocumentDTO documentDTO);
	
	//전체조회
	List<DocumentDTO> documentSelectAll(SearchDTO searchDTO);
	
	//페이징
	int getCount(SearchDTO searchDTO);
	
	//양식조회
	List<FormDTO> formSelectAll();

}
