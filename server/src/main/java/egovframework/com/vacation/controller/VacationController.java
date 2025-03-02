package egovframework.com.vacation.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import egovframework.com.common.util.GridUtil;
import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.LoginRequest;
import egovframework.com.securing.service.UserDTO;
import egovframework.com.vacation.service.VacationDTO;
import egovframework.com.vacation.service.VacationService;

@RestController
@RequestMapping("/vacation")
public class VacationController {

	// 첨부파일 관련
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;

	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;

	@Resource
	private VacationService service;
	
	// 휴가 등록
	@PostMapping("/vcAdd")
	public boolean vcAdd(@Validated VacationDTO vcDTO,
	        @RequestPart(value = "files[]", required = false) List<MultipartFile> files) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
        vcDTO.setCreateId(userId);

		// 첨부파일 관련 첨부파일ID 생성
		List<FileVO> _result = null;
		String _atchFileId = "";
		
		if (!files.isEmpty()) {
			_result = fileUtil.parseFileInf(files, "DSCH_", 0, "", "");
			_atchFileId = fileMngService.insertFileInfs(_result); // 파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
		}
		// 리턴받은 첨부파일ID를 셋팅한다..
		vcDTO.setAtchFileId(_atchFileId); // 첨부파일 ID
		
		return service.vcInsert(vcDTO);
	}
	
	// 휴가 수정
	@PostMapping("/vcModify")
	public boolean vcModify(@Validated VacationDTO vcDTO,
	        @RequestPart(value = "files[]", required = false) List<MultipartFile> files) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
        vcDTO.setUpdateId(userId);

		// 첨부파일 관련 첨부파일ID 생성
		List<FileVO> _result = null;
		String _atchFileId = "";
		
		if (!files.isEmpty()) {
			_result = fileUtil.parseFileInf(files, "DSCH_", 0, "", "");
			_atchFileId = fileMngService.insertFileInfs(_result); // 파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
		}
		// 리턴받은 첨부파일ID를 셋팅한다..
		vcDTO.setAtchFileId(_atchFileId); // 첨부파일 ID
		
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
	public Map<String, Object> vcList(@ModelAttribute VacationDTO vcDTO,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		vcDTO.setCreateId(userId);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		vcDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		vcDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		vcDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<VacationDTO> result = service.vcSelectAll(vcDTO);
		int total = service.vcSelectAllCnt(vcDTO);
		
		return GridUtil.responseData(page, total, result);
	}
	
	// 사용 예정량 조회
	@GetMapping("/expectInfo")
	public VacationDTO expectInfo(@ModelAttribute VacationDTO vcDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
        vcDTO.setCreateId(userId);
        
		return service.expectSelect(vcDTO);
	}
	
	// 휴가 신청 결재 요청 조회
	@GetMapping("/signerList")
	public Map<String, Object> vcSignerList(@ModelAttribute VacationDTO vcDTO,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		vcDTO.setSignId(userId);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		vcDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		vcDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		vcDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<VacationDTO> result = service.vcSelectSigner(vcDTO);
		int total = service.vcSelectSignerCnt(vcDTO);
		
		return GridUtil.responseData(page, total, result);
	}
	
	// 휴가 신청 결재 내역 조회
	@GetMapping("/signedList")
	public Map<String, Object> vcSignedList(@ModelAttribute VacationDTO vcDTO,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
		vcDTO.setSignId(userId);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		vcDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		vcDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		vcDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<VacationDTO> result = service.vcSelectSigned(vcDTO);
		int total = service.vcSelectSignedCnt(vcDTO);
		
		return GridUtil.responseData(page, total, result);
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
