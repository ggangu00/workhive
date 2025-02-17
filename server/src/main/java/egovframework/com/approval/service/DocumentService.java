package egovframework.com.approval.service;

import java.util.List;

public interface DocumentService {
	public int documentInsert(DocumentDTO documentDTO);
	
	public List<DocumentDTO> getList(SearchDTO searchDTO);
	
	int getCount(SearchDTO searchDTO);
}
