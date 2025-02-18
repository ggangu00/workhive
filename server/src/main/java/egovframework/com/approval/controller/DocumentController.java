package egovframework.com.approval.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
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
	
	@GetMapping("/list")
	public Map<String, Object> getCompletedDocuments(@RequestParam(required = false) String status,
												   @RequestParam(required = false, defaultValue = "1") int page) throws JsonMappingException, JsonProcessingException {
		
		System.out.println("request DATA => " +  status + " page => " +  page);
	    SearchDTO searchDTO = new SearchDTO();
	    searchDTO.setStatus(status); // SearchDTO 객체에 값 설정
	    searchDTO.setPage(page);
	    
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
		pagination.put("page", 1);
		pagination.put("totalCount", 100);
		
		data.put("contents", documentService.documentSelectAll(searchDTO));
		
		return map;
	}
	 

}
