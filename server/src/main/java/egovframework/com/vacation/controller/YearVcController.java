package egovframework.com.vacation.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.vacation.service.YearVcDTO;
import egovframework.com.vacation.service.YearVcService;

@RestController
@RequestMapping("/yearVc")
public class YearVcController {

	@Resource
	private YearVcService service;
	
	// 연차 등록
	@PostMapping("/yearVcAdd")
	public boolean yearVcAdd(@ModelAttribute YearVcDTO yearVcDTO) {
		return service.yearVcInsert(yearVcDTO);
	}
	
	// 연차 수정
	@PostMapping("/yearVcModify")
	public boolean yearVcModify(@Validated YearVcDTO yearVcDTO) {
		return service.yearVcUpdate(yearVcDTO);
	}
	
	// 연차 단건 조회
	@GetMapping("/yearVcInfo")
	public YearVcDTO yearVcInfo(@ModelAttribute YearVcDTO searchDTO) {
		
		return service.yearVcSelect(searchDTO);
	}
	
	
	
	
}
