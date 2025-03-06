package egovframework.com.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.approval.service.ApprovalLine;
import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.common.service.CommonDTO;
import egovframework.com.common.service.CommonService;
import egovframework.com.common.util.GridUtil;
import egovframework.com.commute.service.CommuteDTO;
import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.CustomerUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // data 
@RequestMapping("/comm")
public class CommonController {
	
	@Resource private CommonService service;	// 공통	
	@Resource private MemberService memberService;	// 회원
	
	// 공통코드 디테일 전체조회
	@GetMapping("/codeList")
	public List<CommonDTO> commonList(@RequestParam(name="cd") String commCd) {
	  
	  List<CommonDTO> result = service.commonSelectAll(commCd);
	  
	  return result;
	}	
	
	// 거래처 전체조회
	@GetMapping("/comList")
	public List<CommonDTO> comList() {
	  
	  List<CommonDTO> result = service.comSelectAll();
	  
	  return result;
	}
	
	// 로그인 로그 조회
	@GetMapping("/loginLog")
	public Map<String, Object> loginLogList( @ModelAttribute ComDefaultVO searchVO, 
											 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
											 @RequestParam(name = "perPage", required = false, defaultValue = "5") int perPage ) {	
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(perPage);

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<CommonDTO> result = service.loginLogSelectAll(searchVO);
		int totalCnt = service.loginLogSelectAllCnt(searchVO);

		return GridUtil.responseData(page, totalCnt, result);
	}
	
	// 로그인 잠금해제
	@PutMapping("/loginLog/{mberId}")
	public void memberLockUpdate(@PathVariable("mberId") String mberId) {
		memberService.memberLockUpdate(mberId, "A02");
	}
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	@GetMapping("/homeInfo")
	public Map<String, Object> homeInfo() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String mberId = user.getUserDTO().getMberId();
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 정보조회
		CommonDTO result = service.homeInfo(mberId);
		
		if (result != null) {
	        map.put("result", true);
	        map.put("info", result);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	// 홈 게시글 조회
	@GetMapping("/bbsList")
	public List<CommonDTO> bbsList() {
		  
		List<CommonDTO> result = service.bbsSelect();
		 
		return result;
	}
	
	// 홈 일정 조회
	@GetMapping("/calList/{calDt}")
	public List<CommonDTO> calList(@PathVariable("calDt") String calDt) {
		  
		List<CommonDTO> result = service.calSelect(calDt);
		 
		return result;
	}	
	
	// 홈 일자별 일정 건수 조회
	@GetMapping("/calList/cnt/{calDt}")
	public List<CommonDTO> calCntList(@PathVariable("calDt") String calDt) {
		  
		List<CommonDTO> result = service.calCntSelect(calDt);
		 
		return result;
	}	
	
	// 사용자 ip 출력	
	@GetMapping("/log")
    public String myApi(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        
        return ipAddress;
    }
	
	// 회사 정보 조회
	@GetMapping("/company")
	public CommonDTO companyInfo() {
		CommonDTO dto = service.companyInfo();
		
		return dto;
	}
	
	//회사 정보 변경
	@PutMapping("/company")
	public int updateCompanyInfo(@ModelAttribute CommonDTO commonDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerUser user = (CustomerUser) auth.getPrincipal();
		
		String userId = user.getUserDTO().getMberId();
		commonDTO.setMberId(userId);
		
	    System.out.println("받은 요청 데이터: " + commonDTO.toString());

	    int result = service.updateCompanyInfo(commonDTO);

	    // 📌 서비스 메서드 수행 결과 로그
	    System.out.println("DB 업데이트 결과: " + result);
		service.updateCompanyInfo(commonDTO);
		
		return 1;	
	}
}