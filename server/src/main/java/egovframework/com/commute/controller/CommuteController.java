package egovframework.com.commute.controller;

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
import egovframework.com.commute.service.CommuteService;
import egovframework.com.project.service.ProjectDTO;

@RestController
@RequestMapping("/commute")
public class CommuteController {

	@Resource
	private CommuteService service;
	
	// 출퇴근
	// 출퇴근 등록 : 출근
	@GetMapping("/cmtAdd")
	public void cmtAdd(CommuteDTO commuteDTO) {};
	@PostMapping("/cmtAdd")
	public String cmtAdd(@Validated CommuteDTO commuteDTO, BindingResult bindingResult, RedirectAttributes rttr) {

		if(bindingResult.hasErrors()) {
			return "commute/cmtAdd";
		}
		
		service.cmtInsert(commuteDTO);
		
		rttr.addFlashAttribute("result", true);
		
		return "redirect:/commute/cmtList"; //redirect라고 적어줘야 list함수 데이터 조회해서 값 뿌림
	}
	
	// 출퇴근 수정 : 퇴근 / 정정 요청 승인
	@PostMapping("/cmtModify")
	public boolean cmtModify(@Validated CommuteDTO commuteDTO, RedirectAttributes rttr) {

		return service.cmtUpdate(commuteDTO);
	}
	
	// 출퇴근 삭제
	
	// 출퇴근 단건 조회
	@GetMapping("/cmtInfo")
	public CommuteDTO cmtInfo(@RequestParam(name="commuteCd") String commuteCd) {
		System.out.println("cmtCd : " + commuteCd);
		CommuteDTO result = service.cmtSelect(commuteCd);
		System.out.println(result.getCommuteCd());
		return result;
	}
	
	// 출퇴근 전체 조회
	@GetMapping("/cmtList")
	public List<CommuteDTO> cmtList(@RequestParam(name="memCd") String memCd, 
									@RequestParam(name="startDate") String startDate,
									@RequestParam(name="endDate") String endDate) {
		
		CommuteDTO searchDTO = new CommuteDTO();
		searchDTO.setMemCd(memCd);
		searchDTO.setStartDate(startDate);
		searchDTO.setEndDate(endDate);
		
		List<CommuteDTO> result = service.cmtSelectAll(searchDTO);
		
		return result;
	}
	
	// 마지막 출퇴근 정보 조회
	@GetMapping("/lastCmtInfo")
	public CommuteDTO lastCmtInfo(@RequestParam(name="memCd") String memCd) {
		CommuteDTO result = service.lastCmtSelect(memCd);
		
		return result;
	}
	

}
