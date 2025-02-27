package egovframework.com.approval.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;

@Configuration
@Service
public class DocumentServiceImpl implements DocumentService{

	@Resource
	private DocumentMapper documentMapper;
	
	// eGov 파일관리 서비스 빈 주입 (파일 마스터/상세 등록용)
    @Resource(name = "EgovFileMngService")
    private EgovFileMngService fileMngService;
    
    // eGov 파일 관리 유틸리티
    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileMngUtil;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
//    // 파일 저장 경로 (global.properties 또는 EgovProperties에서 가져옴)
//    @org.springframework.beans.factory.annotation.Value("${Globals.fileStorePath}")
//    private String fileStorePath;
	
	
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
        //  첨부파일 처리 (eGovFrame 파일업로드 사용)
		List<MultipartFile> files = approvalParentDTO.getMultipartFileList();
        List<FileVO> fileVOList = null;
        String atchFileId = "";
        
        if (files != null && !files.isEmpty()) {
            try {
                // 파일을 변환하여 저장할 준비
                fileVOList = fileMngUtil.parseFileInf(files, "DSCH_", 0, "", "");

                // 파일을 DB에 저장하고 생성된 첨부파일 ID를 가져옴
                atchFileId = fileMngService.insertFileInfs(fileVOList);
                System.out.println("생성된 첨부파일 ID: " + atchFileId);
            } catch (Exception e) {
                e.printStackTrace();
                return 0; // 파일 저장 중 오류 발생 시 중단
            }
        }
		
		//문서
        Document document = approvalParentDTO.getDocument();
        documentMapper.documentInsert(document);
        document.setAtchFileId(atchFileId);
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

		return 1;
	}

	//결재선 조회
	@Override
	public List<ApprovalLine> approvalSelectAll(String docCd) {
		return documentMapper.approvalSelectAll(docCd);
	}

	@Override
	public boolean approvalStateUpdate(List<String> approvalArr, String signStat, String mberId) {
		ApprovalLine approvalLine = new ApprovalLine();
		approvalLine.setApprovalArr(approvalArr);
		approvalLine.setSignStat(signStat);
		approvalLine.setMberId(mberId);
		
		int updatedRows = documentMapper.approvalStateUpdate(approvalLine); 
		
		return updatedRows >0;
	}
	
	
}
