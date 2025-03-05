package egovframework.com.authority.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.authority.service.AuthorityDTO;
import egovframework.com.authority.service.AuthorityService;
import egovframework.com.securing.service.CustomerUser;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/authority")
@Slf4j
public class AuthorityController {
	
	@Resource
	private AuthorityService authService;
	
	// 권한 조회 처리
	@GetMapping("")
	public List<AuthorityDTO> authorityList() {
				
		log.info("권한 목록 조회" + authService.authoritySelectAll().toString());
		
		return authService.authoritySelectAll();
	}
	
	@GetMapping("/{authorityCd}")
	public Map<String, Object> authorityInfo(@PathVariable String authorityCd) {
		log.info(authorityCd.toString());
		
		Map<String, Object> map = new HashMap<>();
		
		// 수정하기 위한 정보 조회
		AuthorityDTO infoDto = authService.authoritySelect(authorityCd);
	    
		if (infoDto != null) {
	        map.put("result", true);
	        map.put("info", infoDto);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	// 권한 등록 처리
	@PostMapping("")
	public Map<String, Object> authorityAdd(@RequestBody AuthorityDTO dto) {
		boolean result = authService.authorityInsert(dto);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        dto.setCreateId(user.getUserDTO().getMberId());
        
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", authService.authoritySelectAll());

		return map;
	}
	
	// 권한 수정 처리
	@PutMapping("")
	public Map<String, Object> authorityModify(@RequestBody AuthorityDTO dto) {
		log.info("수정 권한 코드 출력 => " + dto.toString());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        dto.setUpdateId(user.getUserDTO().getMberId());
		
		Map<String, Object> map = new HashMap<>();
		
		boolean result = authService.authorityUpdate(dto);
		
		map.put("result", result);
		map.put("list", authService.authoritySelectAll());
		
		return map;
	}
	
	// 권한 삭제 처리
	@DeleteMapping("/{authorityCd}")
	public Map<String, Object> authorityRemove(@PathVariable(name="authorityCd") String authorityCd) {
		log.info("삭제 권한 코드 출력 => " + authorityCd);
	    
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String mberId = user.getUserDTO().getMberId();
        
		boolean result = authService.authorityDelete(authorityCd, mberId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", authService.authoritySelectAll());
		
		return map;
	}
	
}
