package egovframework.com.authority.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
