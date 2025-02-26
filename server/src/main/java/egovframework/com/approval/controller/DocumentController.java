package egovframework.com.approval.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.approval.service.ApprovalLine;
import egovframework.com.approval.service.ApprovalParentDTO;
import egovframework.com.approval.service.Document;
import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
import egovframework.com.approval.service.FormDTO;
import egovframework.com.approval.service.MemberDTO;
import egovframework.com.approval.service.Reception;
import egovframework.com.approval.service.SearchDTO;
import egovframework.com.cmm.service.FileVO;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/document")
@Slf4j
public class DocumentController {

	@Resource DocumentService documentService;
	
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String register(
	        @RequestParam("document") String documentJson,
	        @RequestParam("approvalLine") String approvalLineJson,
	        @RequestParam("reception") String receptionJson,
	        @RequestPart(value = "files[]", required = false) List<MultipartFile> files) {

	    ApprovalParentDTO approvalParentDTO = new ApprovalParentDTO();
	    ObjectMapper objectMapper = new ObjectMapper();

	    try {
	        // JSON 문자열을 객체로 변환 (TypeReference 없이 배열 변환 후 리스트로 변경)
	        Document document = objectMapper.readValue(documentJson, Document.class);

	        ApprovalLine[] approvalLineArray = objectMapper.readValue(approvalLineJson, ApprovalLine[].class);
	        List<ApprovalLine> approvalLines = Arrays.asList(approvalLineArray); // 배열을 리스트로 변환

	        Reception[] receptionArray = objectMapper.readValue(receptionJson, Reception[].class);
	        List<Reception> receptions = Arrays.asList(receptionArray); // 배열을 리스트로 변환

	        // DTO에 값 설정
	        approvalParentDTO.setDocument(document);
	        approvalParentDTO.setApprovalLine(approvalLines);
	        approvalParentDTO.setReception(receptions);

	        // 파일 처리
	        List<FileVO> fileVOList = new ArrayList<>();
	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                FileVO fileVO = new FileVO();
	                fileVO.setOrignlFileNm(file.getOriginalFilename()); // 원본 파일명
	                fileVO.setFileMg(String.valueOf(file.getSize())); // 파일 크기
	                fileVO.setFileExtsn(getFileExtension(file.getOriginalFilename())); // 확장자 추출
	                fileVO.setFileCn(new String(file.getBytes())); // 파일 내용을 문자열로 저장 (DB 저장 시 필요)
	                fileVOList.add(fileVO);
	            }
	            approvalParentDTO.setFileList(fileVOList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "fail";
	    }

	    // ServiceImpl의 `approvalInsert` 메소드 호출
	    int result = documentService.approvalInsert(approvalParentDTO);
	    return result > 0 ? "success" : "fail";
	}

	// 파일 확장자 추출 유틸리티 메소드
	private String getFileExtension(String fileName) {
	    return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
	}


	
//	//문서기안(문서정보, 수신정보, 결재선정보, 첨부파일)
//	@PostMapping("/register")
//    public String register(@RequestBody ApprovalParentDTO approvalParentDTO) {
//        int result = documentService.approvalInsert(approvalParentDTO);
//		if(result>0) {
//			return "success";
//		}else {
//			return "fail";
//		}
//    }
	
//	@PostMapping(value="/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public String register(@ModelAttribute ApprovalParentDTO approvalParentDTO) {
//	    // ServiceImpl의 approvalInsert 메소드 호출
//	    int result = documentService.approvalInsert(approvalParentDTO);
//	    return result > 0 ? "success" : "fail";
//	}

	//결재선정보 조회
	@GetMapping("/approvalList")
	public List<ApprovalLine> approvalList(@RequestParam(name="docCd") String docCd){
		return documentService.approvalSelectAll(docCd);
	}
	
	//리스트조회(조건별)
	@GetMapping("/list")
	public Map<String, Object> getCompletedDocuments(@RequestParam(required = false) String status,
												     @RequestParam(required = false) int page,
												     @RequestParam(required = false) int perPage,
												     @ModelAttribute SearchDTO searchDTO) throws JsonMappingException, JsonProcessingException {
		
		searchDTO.setPageUnit(perPage);

		// 페이징 조건
		searchDTO.setStartPage(searchDTO.getFirst());
		searchDTO.setEndPage(searchDTO.getLast());
		searchDTO.setStatus(status);
		
		// 페이징처리
		searchDTO.setTotalRecord(documentService.getCount(searchDTO));
		
		System.out.println("request DATA => " +  status + " page => " +  page);
		
	    
	    String str = """
						{
						  "result": true,
						  "data": {
						    "contents": [],
						    "pagination": {
						      "page": 1,
						      "totalCount": 100
						    }
						  }
						}
										""";
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<String, Object> map = objectMapper.readValue(str, Map.class);
		
		Map<String, Object> data = (Map) map.get("data");
		Map<String, Object> pagination = (Map) data.get("pagination");
		
		// 페이징처리
		pagination.put("page", searchDTO.getPage());
		pagination.put("totalCount", documentService.getCount(searchDTO));
		
		data.put("contents", documentService.documentSelectAll(searchDTO));
		
		return map;
	}
	
	//문서양식조회
	@GetMapping("/form")
	public List<FormDTO> formList() {
		return documentService.formSelectAll();
	}
	
	//부서별사원조회
	@GetMapping("/deptMember")
	public List<MemberDTO> memberList(@ModelAttribute SearchDTO searchDTO) {
		return documentService.memberSelectAll(searchDTO);
	}
	
	//문서회수(상태변경회수 상태로)
	@PutMapping("/retrieve/{docCd}")
		public ResponseEntity<String> documentUpdate(
				@RequestBody DocumentDTO documentDTO,
				@PathVariable(name="docCd") String docCd){
		documentDTO.setDocCd(docCd);
		return documentService.documentUpdate(documentDTO) == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	//문서삭제(DelYn값 변경)
	@PutMapping("/delete/{docCd}")
		public ResponseEntity<String> documentDelete(
				@RequestBody DocumentDTO documentDTO,
				@PathVariable(name="docCd") String docCd){
		documentDTO.setDocCd(docCd);
		return documentService.documentDelete(documentDTO) == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	//결재선 상태변경
	@PutMapping("/state")
	public boolean todoStateModify(@RequestBody ApprovalLine approvalLine) {		
		List<String> approvalArr = approvalLine.getApprovalArr();
	    String signStat = approvalLine.getSignStat();
	    String mberId = approvalLine.getMberId();
	    log.info("결과아아ㅏㅏ ==> "+signStat);
	    
	    return documentService.approvalStateUpdate(approvalArr,signStat, mberId);
	}
}
