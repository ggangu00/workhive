package egovframework.com.approval.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.approval.mapper.DocumentMapper;
import egovframework.com.approval.service.ApprovalLine;
import egovframework.com.approval.service.ApprovalParentDTO;
import egovframework.com.approval.service.Document;
import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
import egovframework.com.approval.service.FormDTO;
import egovframework.com.approval.service.MemberDTO;
import egovframework.com.approval.service.Reception;
import egovframework.com.approval.service.SearchDTO;

@Service
public class DocumentServiceImpl implements DocumentService{

	@Resource
	private DocumentMapper documentMapper;
	
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

	@Override
	public int documentUpdate(DocumentDTO documentDTO) {
		return documentMapper.documentUpdate(documentDTO);
	}

	@Override
	public int documentDelete(DocumentDTO documentDTO) {
		return documentMapper.documentDelete(documentDTO);
	}

	@Override
	public List<MemberDTO> memberSelectAll(SearchDTO searchDTO) {
		return documentMapper.memberSelectAll(searchDTO);
	}

	//문서기안
	@Override
	public int approvalInsert(ApprovalParentDTO approvalParentDTO) {
		//문서
        Document document = approvalParentDTO.getDocument();
        System.out.println(document.toString());
        documentMapper.documentInsert(document);
        
        //문서코드가져오기
        
        String docCd = documentMapper.lastDocCdGet();
        
        System.out.println("생성된 문서 코드: " + docCd);

		//결재자
        List<ApprovalLine> approvalLines = approvalParentDTO.getApprovalLine();
        if (approvalLines != null && !approvalLines.isEmpty()) {
            for (int i = 0; i < approvalLines.size(); i++) {
                ApprovalLine line = approvalLines.get(i);
                line.setDocCd(docCd);
                line.setSignSeq(i + 1);
                documentMapper.approvalInsert(line);
            }
        }
		//수신자
        List<Reception> receptions = approvalParentDTO.getReception();
        if(receptions != null && !receptions.isEmpty()) {
        	   for (Reception reception : receptions) {
                   reception.setDocCd(docCd);
                   documentMapper.receptionInsert(reception);
               }
        }
		//첨부파일
		return 1;
	}

}
