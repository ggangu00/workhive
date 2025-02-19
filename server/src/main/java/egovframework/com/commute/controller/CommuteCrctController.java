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

import egovframework.com.commute.service.CommuteCrctDTO;
import egovframework.com.commute.service.CommuteCrctService;

@RestController
@RequestMapping("/commute")
public class CommuteCrctController {

	@Resource
	private CommuteCrctService service;
	
	// 출퇴근 정정
	// 출퇴근 정정 등록
	@GetMapping("/crctAdd")
	public void crctAdd(CommuteCrctDTO crctDTO) {};
	@PostMapping("/crctAdd")
	public String crctAdd(@Validated CommuteCrctDTO crctDTO, BindingResult bindingResult, RedirectAttributes rttr) {

		if(bindingResult.hasErrors()) {
			return "commute/crctAdd";
		}
		
		service.crctInsert(crctDTO);
		
		rttr.addFlashAttribute("result", true);
		
		return "redirect:/commute/crctList";
	}
	
	// 출퇴근 정정 수정
	
	// 출퇴근 정정 삭제
	
	// 출퇴근 정정 단건 조회
	@GetMapping("/crctInfo")
	public CommuteCrctDTO crctInfo(@RequestParam(name="crctCd") String crctCd) {
		CommuteCrctDTO result = service.crctSelect(crctCd);
		return result;
	}
	
	// 출퇴근 정정 전체 조회
	@GetMapping("/crctList")
	public List<CommuteCrctDTO> crctList(@RequestParam(name="createId") String createId, 
										 @RequestParam(name="startDate") String startDate,
										 @RequestParam(name="endDate") String endDate) {
		
		CommuteCrctDTO searchDTO = new CommuteCrctDTO();
		searchDTO.setCreateId(createId);
		searchDTO.setStartDate(startDate);
		searchDTO.setEndDate(endDate);
		
		List<CommuteCrctDTO> result = service.crctSelectAll(searchDTO);
		
		return result;
	}
	
}
