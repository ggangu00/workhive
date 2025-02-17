package egovframework.com.authority.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/list")
	public List<AuthorityDTO> authorityList() {
		log.info(authService.authoritySelectAll().toString());
		
		return authService.authoritySelectAll();
	}
	
	@PostMapping("/add")
	public Map<String, Object> authorityAdd(@RequestBody AuthorityDTO dto) {
		log.info("response Data", dto.toString());
		
		boolean result = authService.authorityInsert(dto);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("list", authService.authoritySelectAll());

		return map;
	}
	
	@PutMapping("/modify")
	public boolean authorityModify(@RequestBody String authorityCd) {
		log.info("수정 권한 코드 출력 => " + authorityCd);
		
		boolean result = authService.authorityUpdate(authorityCd);
		
		return result;
	}
	
	@DeleteMapping("/remove")
	public boolean authorityRemove(@RequestParam(name="authorityCd") String authorityCd) {
		log.info("삭제 권한 코드 출력 => " + authorityCd);
	    
	    // 서비스 로직 실행
	    return authService.authorityDelete(authorityCd);
	}
	
}
