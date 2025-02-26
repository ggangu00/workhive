package egovframework.com.vacation.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.commute.service.CommuteCrctDTO;
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
	@GetMapping("/vcInfo")
	public VacationDTO vcInfo(@RequestParam(name="vcCd") String vcCd) {
		return service.vcSelect(vcCd);
	}
	
	// 휴가 전제 조회
	@GetMapping("/vcList")
	public List<VacationDTO> vcList(@ModelAttribute VacationDTO searchDTO) {
		
		List<VacationDTO> result = service.vcSelectAll(searchDTO);
		
		return result;
	}
	
	// 사용 예정량 조회
	@GetMapping("/expectInfo")
	public VacationDTO expectInfo(@ModelAttribute VacationDTO searchDTO) {
		return service.expectSelect(searchDTO);
	}
	
	// 휴가 신청 결재 요청 조회
	@GetMapping("/signerList")
	public List<VacationDTO> vcSignerList(@ModelAttribute VacationDTO searchDTO) {
		
		List<VacationDTO> result = service.vcSelectSigner(searchDTO);
		
		return result;
	}
	
	// 휴가 신청 결재 내역 조회
	@GetMapping("/signedList")
	public List<VacationDTO> vcSignedList(@ModelAttribute VacationDTO searchDTO) {
		
		List<VacationDTO> result = service.vcSelectSigned(searchDTO);
		
		return result;
	}
	
	// 휴가 신청 결재
	@PostMapping("/signModify")
	public void signModify(@RequestBody List<VacationDTO> vcList) {
		vcList.forEach(vcDTO -> {
			service.signUpdate(vcDTO);
		});
		return;
	}
	
}
