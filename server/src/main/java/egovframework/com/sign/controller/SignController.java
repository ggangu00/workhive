package egovframework.com.sign.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.common.util.GridUtil;
import egovframework.com.securing.service.CustomerUser;
import egovframework.com.sign.service.SignDTO;
import egovframework.com.sign.service.SignService;

@RestController
@RequestMapping("/sign")
public class SignController {

	@Resource
	private SignService service;
	
	// 결재자 목록 조회
	@GetMapping("")
	public Map<String, Object> signerList(@ModelAttribute SignDTO signDTO,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String deptCd = user.getUserDTO().getDeptCd();
        signDTO.setDeptCd(deptCd);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		signDTO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		signDTO.setLastIndex(paginationInfo.getLastRecordIndex());
		signDTO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<SignDTO> result = service.signerSelectAll(signDTO);
		int total = service.signerSelectCnt(signDTO);
		
		return GridUtil.responseData(page, total, result);
	}
}
