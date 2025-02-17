package egovframework.com.approval.controller;

import java.lang.System.Logger;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.approval.service.DocumentDTO;
import egovframework.com.approval.service.DocumentService;
import egovframework.com.approval.service.SearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/document")
@Slf4j
@RequiredArgsConstructor
public class DocumentController {

	private final DocumentService documentService;
	
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
	public List<DocumentDTO> getCompletedDocuments(@RequestParam(required = false) String status,
													@RequestParam(required = false) String delYn,
													@RequestParam(required = false) int page) {
	    SearchDTO searchDTO = new SearchDTO();
	    searchDTO.setStatus(status); // SearchDTO 객체에 값 설정
	    searchDTO.setDelYn(delYn);
	    searchDTO.setPage(page);
	    return documentService.getList(searchDTO);
	}
	 

}
