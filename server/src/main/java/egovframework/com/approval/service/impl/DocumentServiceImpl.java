package egovframework.com.approval.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.approval.mapper.DocumentMapper;
import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
import egovframework.com.approval.service.SearchDTO;

@Service
public class DocumentServiceImpl implements DocumentService{

	@Resource
	private DocumentMapper documentMapper;
	
	@Override
	public int documentInsert(DocumentDTO documentDTO) {
		return documentMapper.insert(documentDTO);
	}

	@Override
	public List<DocumentDTO> getList(SearchDTO searchDTO) {
		return documentMapper.getList(searchDTO);
	}

	@Override
	public int getCount(SearchDTO searchDTO) {
		return documentMapper.getCount(searchDTO);
	}

}
