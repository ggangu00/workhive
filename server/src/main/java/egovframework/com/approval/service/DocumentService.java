package egovframework.com.approval.service;

import java.util.List;

public interface DocumentService {
	public int documentInsert(DocumentDTO documentDTO);
	
	// 전체조회
	public List<DocumentDTO> documentSelectAll(SearchDTO searchDTO);
	
	int getCount(SearchDTO searchDTO);
}
