package egovframework.com.common.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.common.service.CommonDTO;
import egovframework.com.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/comm")
@Slf4j
public class CommonController {
	
	@Resource
	private  CommonService service;
	
	//공통함수 조회
	@GetMapping("/codeList")
	public List<CommonDTO> commonlist(@RequestParam(name="cd") String commCd) {
	  
	  List<CommonDTO> result = service.commonSelectAll(commCd);
	  
	  return result;
	}	
	
	//거래처 조회
	@GetMapping("/comList")
	public List<CommonDTO> comlist() {
	  
	  List<CommonDTO> result = service.comSelectAll();
	  
	  return result;
	}	
}