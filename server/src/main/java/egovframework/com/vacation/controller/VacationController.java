package egovframework.com.vacation.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.commute.service.CommuteDTO;
import egovframework.com.vacation.service.VacationDTO;
import egovframework.com.vacation.service.VacationService;

@RestController
@RequestMapping("/vacation")
public class VacationController {

	@Resource
	private VacationService service;
	
	// 휴가 등록
	@PostMapping("/vcAdd")
	public boolean vcAdd(@Validated VacationDTO vcDTO) {
		
		return service.vcInsert(vcDTO);
	}
	
	// 휴가 수정
	@PostMapping("/vcModify")
	public boolean vcModify(@Validated VacationDTO vcDTO) {
		return service.vcUpdate(vcDTO);
	}
	
	// 휴가 삭제
	@PostMapping("/vcRemove")
	public boolean vcRemove(@RequestParam(name="vcCd") String vcCd) {
		return service.vcDelete(vcCd);
	}
	
	// 휴가 단건 조회
	
	// 휴가 목록 조회
	
	
	
	
}
