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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.common.util.CommonFileManage;
import egovframework.com.common.util.GridUtil;
import egovframework.com.commute.service.CommuteCrctDTO;
import egovframework.com.commute.service.CommuteCrctService;
import egovframework.com.commute.service.CommuteDTO;
import egovframework.com.securing.service.CustomerUser;

@RestController
@RequestMapping("/commute")
public class CommuteCrctController {

	// 첨부파일 관련
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;

	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;

	@Resource
	private CommuteCrctService service;
	
	// 출퇴근 정정
	// 출퇴근 정정 등록
	@GetMapping("/crctAdd")
	public void crctAdd(CommuteCrctDTO crctDTO) {};
	@PostMapping("/crctAdd")
	public String crctAdd(@Validated CommuteCrctDTO crctDTO, BindingResult bindingResult, RedirectAttributes rttr,
	        @RequestPart(value = "files[]", required = false) List<MultipartFile> files) throws Exception {

		if(bindingResult.hasErrors()) {
			return "commute/crctAdd";
		}

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		crctDTO.setCreateId(userId);
		
		// 첨부파일 관련 첨부파일ID 생성
		List<FileVO> _result = null;
		String _atchFileId = "";
		
		if (!files.isEmpty()) {
			_result = fileUtil.parseFileInf(files, "DSCH_", 0, "", "");
			_atchFileId = fileMngService.insertFileInfs(_result); // 파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
		}
		// 리턴받은 첨부파일ID를 셋팅한다..
		crctDTO.setAtchFileId(_atchFileId); // 첨부파일 ID
		
		service.crctInsert(crctDTO);
		
		rttr.addFlashAttribute("result", true);
		
		return "redirect:/commute/crctList";
	}
	
	// 출퇴근 정정 수정
	@PostMapping("/crctModify")
	public boolean crctModify(@Validated CommuteCrctDTO crctDTO, RedirectAttributes rttr) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		crctDTO.setUpdateId(userId);
		
		return service.crctUpdate(crctDTO);
	}
	
	// 출퇴근 정정 삭제
	@PostMapping("/crctRemove")
	public boolean crctRemove(@RequestParam(name="crctCd") String crctCd) {

		return service.crctDelete(crctCd);
	}
	
	// 출퇴근 정정 단건 조회
	@GetMapping("/crctInfo")
	public CommuteCrctDTO crctInfo(@RequestParam(name="crctCd") String crctCd) {
		CommuteCrctDTO result = service.crctSelect(crctCd);
		return result;
	}
	
	// 출퇴근 정정 전체 조회
	@GetMapping("/crctList")
	public Map<String, Object> crctList(@ModelAttribute CommuteCrctDTO crctDTO,
										 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
										 @RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		crctDTO.setCreateId(userId);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		crctDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		crctDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		crctDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<CommuteCrctDTO> result = service.crctSelectAll(crctDTO);
		int total = service.crctSelectAllCnt(crctDTO);
		
		return GridUtil.responseData(page, total, result);
	}

	// 출퇴근 정정 요청 조회 : 결재자
	@GetMapping("/signerList")
	public Map<String, Object> crctSignList(@ModelAttribute CommuteCrctDTO crctDTO,
											 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
											 @RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		crctDTO.setSignId(userId);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		crctDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		crctDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		crctDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<CommuteCrctDTO> result = service.crctSelectSigner(crctDTO);
		int total = service.crctSelectSignerCnt(crctDTO);
		
		return GridUtil.responseData(page, total, result);
	}

	// 출퇴근 정정 결재 내역 조회
	@GetMapping("/signedList")
	public Map<String, Object> crctSignedList(@ModelAttribute CommuteCrctDTO crctDTO,
											   @RequestParam(name = "page", required = false, defaultValue = "1") int page,
											   @RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		crctDTO.setSignId(userId);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		crctDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		crctDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		crctDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<CommuteCrctDTO> result = service.crctSelectSigned(crctDTO);
		int total = service.crctSelectSignedCnt(crctDTO);

		return GridUtil.responseData(page, total, result);
	}
	
	// 출퇴근 정정 결재
	@PostMapping("/signModify")
	public void signModify(@RequestBody List<CommuteCrctDTO> crctList, RedirectAttributes rttr) {

		crctList.forEach(crctDTO -> {
			service.signUpdate(crctDTO);
		});
		return;
	}
}
