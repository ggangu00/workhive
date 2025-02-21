package egovframework.com.approval.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
import egovframework.com.approval.service.FormDTO;
import egovframework.com.approval.service.SearchDTO;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/document")
@Slf4j
public class DocumentController {

	@Resource DocumentService documentService;
	
	//문서기안
	@PostMapping("/register")
    public String register(@RequestBody DocumentDTO documentDTO) {
        int result = documentService.documentInsert(documentDTO);
		if(result>0) {
			return "success";
		}else {
			return "fail";
		}
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
}
