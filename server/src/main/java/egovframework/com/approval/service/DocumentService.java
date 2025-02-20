package egovframework.com.approval.service;

import java.util.List;

public interface DocumentService {
	//문서기안
	public int documentInsert(DocumentDTO documentDTO);
	
	// 전체조회
	public List<DocumentDTO> documentSelectAll(SearchDTO searchDTO);
	
	//문서 개수
	int getCount(SearchDTO searchDTO);
	
	//문서양식 이름 조회
	public List<FormDTO> formSelectAll();
	
	//문서회수(상태값 회수로 변경)
	public int documentUpdate(DocumentDTO documentDTO);
	
	//삭제 (delYn 변경)
	public int documentDelete(DocumentDTO documentDTO);
}
