package egovframework.com.approval.service;

import java.util.List;

public interface DocumentService {
	//문서기안(수신자 결재자 첨부파일 문서)
	public int approvalInsert(ApprovalParentDTO approvalParentDTO);
	
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
	
	//부서로 사원조회
	public List<MemberDTO> memberSelectAll(SearchDTO searchDTO);
	
	//결재선조회(문서번호로)
	public List<ApprovalLine> approvalSelectAll(String docCd);
	
	//결재선 상태 변경
	public boolean approvalStateUpdate(List<String> approvalArr, String signStat, String mberId);
}
