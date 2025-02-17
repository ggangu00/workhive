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
//
//	//수정
//	@GetMapping({"/modify","get"})
//	public void modify(@RequestParam(name="bno") Long bno, Model model) {
//		BoardDTO board = service.get(bno);
//		model.addAttribute("board", board);
//	}
//	
//	//수정
//	@PostMapping("/modify")
//	public String modify(BoardDTO board, RedirectAttributes rttr) {
//		log.info("modify:" + board);
//		service.modify(board); // -> mapper -> insert 
//		
//		rttr.addFlashAttribute("result", true); //addFlashAttribute 딱한번 어럴트 보임
//		return "redirect:/board/list";
//	}
	
	// 출퇴근 삭제
	
	// 출퇴근 단건 조회
	
	// 출퇴근 전체 조회
	@GetMapping("/cmtList")
	public List<CommuteDTO> cmtList(@RequestParam(name="memCd") String memCd) {
		List<CommuteDTO> result = service.cmtSelectAll(memCd);
		
		return result;
	}
	
	// 마지막 출퇴근 정보 조회
	@GetMapping("/lastCmtInfo")
	public CommuteDTO lastCmtInfo(@RequestParam(name="memCd") String memCd) {
		CommuteDTO result = service.lastCmtSelect(memCd);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("last cmt : " + result);
		return result;
	}
	
	
	// 출퇴근 정정
	// 출퇴근 정정 등록
	
	// 출퇴근 정정 수정
	
	// 출퇴근 정정 삭제
	
	// 출퇴근 정정 단건 조회
	
	// 출퇴근 정정 전체 조회
	

}
