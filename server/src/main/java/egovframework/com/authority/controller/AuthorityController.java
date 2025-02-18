package egovframework.com.authority.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.authority.service.AuthorityDTO;
import egovframework.com.authority.service.AuthorityService;
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
		log.info(authService.authoritySelectAll().toString());
		
		return authService.authoritySelectAll();
	}
	
	@GetMapping("/{authorityCd}")
	public Map<String, Object> boardInfo(@PathVariable String authorityCd) {
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
		log.info("response Data", dto.toString());
		
		boolean result = authService.authorityInsert(dto);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", authService.authoritySelectAll());

		return map;
	}
	
	// 권한 수정 처리
	@PutMapping("")
	public Map<String, Object> authorityModify(@RequestBody AuthorityDTO dto) {
		log.info("수정 권한 코드 출력 => " + dto.toString());
		
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
	    
	    // 서비스 로직 실행
		boolean result = authService.authorityDelete(authorityCd);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", authService.authoritySelectAll());
		
		return map;
	}
	
}
