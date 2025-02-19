package egovframework.com.approval.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.approval.mapper.DocumentMapper;
import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
import egovframework.com.approval.service.FormDTO;
import egovframework.com.approval.service.SearchDTO;

@Service
public class DocumentServiceImpl implements DocumentService{

	@Resource
	private DocumentMapper documentMapper;
	
	@Override
	public int documentInsert(DocumentDTO documentDTO) {
		return documentMapper.insert(documentDTO);
	}
	
	// 전체 조회
	@Override
	public List<DocumentDTO> documentSelectAll(SearchDTO searchDTO) {
		return documentMapper.documentSelectAll(searchDTO);
	}

	@Override
	public int getCount(SearchDTO searchDTO) {
		return documentMapper.getCount(searchDTO);
	}

	@Override
	public List<FormDTO> formSelectAll() {
		return documentMapper.formSelectAll();
	}

}
