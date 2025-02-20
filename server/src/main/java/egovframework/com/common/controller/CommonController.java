package egovframework.com.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.common.service.CommonDTO;
import egovframework.com.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/comm")
@Slf4j
public class CommonController {
	
	@Resource
	private  CommonService service;
	
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
	public List<CommonDTO> loginLogList() {
	  
	  List<CommonDTO> result = service.loginLogSelectAll();
	  
	  return result;
	}
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	@GetMapping("/homeInfo/{memCd}")
	public Map<String, Object> homeInfo(@PathVariable("memCd") String memCd) {
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 정보조회
		CommonDTO result = service.homeInfo(memCd);
		
		if (result != null) {
	        map.put("result", true);
	        map.put("info", result);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	// 사용자 ip 출력	
	@GetMapping("/log")
    public String myApi(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        
        return ipAddress;
    }
}