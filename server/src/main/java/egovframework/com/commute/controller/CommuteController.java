package egovframework.com.commute.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import egovframework.com.common.util.GridUtil;
import egovframework.com.commute.service.CommuteDTO;
import egovframework.com.commute.service.CommuteService;
import egovframework.com.securing.service.CustomerUser;

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
	
	// 출퇴근 수정 : 퇴근
	@PostMapping("/cmtModify")
	public boolean cmtModify(@Validated CommuteDTO commuteDTO, RedirectAttributes rttr) {

		return service.cmtUpdate(commuteDTO);
	}
	// 출퇴근 수정 : 결재
	@PostMapping("/crctSignModify")
	public void crctSignModify(@RequestBody List<CommuteDTO> cmtList, RedirectAttributes rttr) {

		cmtList.forEach(cmtDTO -> {
			service.crctSignUpdate(cmtDTO);
		});
		return;
	}
	
	// 출퇴근 삭제
	
	// 출퇴근 단건 조회
	@GetMapping("/cmtInfo")
	public CommuteDTO cmtInfo(@RequestParam(name="commuteCd") String commuteCd) {
		return service.cmtSelect(commuteCd);
	}
	
	// 출퇴근 전체 조회
	@GetMapping("/cmtList")
	public Map<String, Object> cmtList(@ModelAttribute CommuteDTO cmtDTO,
									@RequestParam(name = "page", required = false, defaultValue = "1") int page,
									@RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {
		
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
        cmtDTO.setMberId(userId);
        
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		cmtDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		cmtDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		cmtDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<CommuteDTO> result = service.cmtSelectAll(cmtDTO);
		int totalCnt = service.cmtSelectAllCnt(cmtDTO);

		return GridUtil.responseData(page, totalCnt, result);
	}
	
	// 마지막 출퇴근 정보 조회
	@GetMapping("/lastCmtInfo")
	public CommuteDTO lastCmtInfo(@RequestParam(name="mberId") String mberId) {
		CommuteDTO result = service.lastCmtSelect(mberId);
		
		return result;
	}

	// 날짜 기준 출퇴근 단건 조회
	@GetMapping("/dateCmtInfo")
	public CommuteDTO dateCmtInfo(@RequestParam(name="commuteDt") String commuteDt) {
		CommuteDTO result = service.dateCmtSelect(commuteDt);
		
		return result;
	}

	// 지정된 출퇴근 시간
	@GetMapping("/cmtTimeInfo")
	public CommuteDTO cmtTimeInfo() {
		CommuteDTO result = service.cmtTimeSelect();
		
		return result;
	}
	
}
