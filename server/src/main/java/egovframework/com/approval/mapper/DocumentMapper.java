package egovframework.com.approval.mapper;

import java.util.List;

import egovframework.com.approval.service.ApprovalLine;
import egovframework.com.approval.service.Document;
import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.File;
import egovframework.com.approval.service.FileDetail;
import egovframework.com.approval.service.FormDTO;
import egovframework.com.approval.service.MemberDTO;
import egovframework.com.approval.service.Reception;
import egovframework.com.approval.service.SearchDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("documentMapper")
public interface DocumentMapper {
	//문서등록
	int documentInsert(Document document);
	//문서코드 가져오기
	String lastDocCdGet();
	//첨부파일 등록(헤더)
	int insertFileMaster(File file);
	//첨부파일 등록(디테일)
	int insertFileDetail(FileDetail fileDetail);
	//파일코드 갸저요기
	String lastFileCdGet();
	//결재자등록
	int approvalInsert(ApprovalLine approvalLine);
	//수신자등록
	int receptionInsert(Reception reception);
	
	//전체조회
	List<DocumentDTO> documentSelectAll(SearchDTO searchDTO);
	
	//페이징
	int getCount(SearchDTO searchDTO);
	
	//미결문서 개수
	int pendingDocCount(SearchDTO searchDTO);
	
	//양식조회
	List<FormDTO> formSelectAll();
	
	//문서회수(상태값 회수로 변경)
	int documentUpdate(DocumentDTO documentDTO);
	
	//문서삭제(delYN 변경)
	int documentDelete(DocumentDTO documentDTO);
	
	//부서별 사원조회
	List<MemberDTO> memberSelectAll(SearchDTO searchDTO);
	
	//결재선 조회
	List<ApprovalLine> approvalSelectAll(String docCd);
	
	//결재선 상태수정
	public int approvalStateUpdate(ApprovalLine approvalLine);
	
	//수신자 조회
	List<Reception> receiverSelecteAll(String docCd);
	
	//문서조회(미결함)
	List<DocumentDTO> pendingDocumentSelectAll(SearchDTO searchDTO);
	
	//결재 의견작성()
	public int approvalCnUpdate(ApprovalLine approvalLine);
}
