package egovframework.com.menu.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.menu.service.MenuInfoDTO;
import egovframework.com.menu.service.MenuInfoService;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/menu")
@Slf4j
public class MenuInfoController {
	
	@Resource
	private MenuInfoService menuService;
	
	// 메뉴정보
	@GetMapping("")
	public List<MenuInfoDTO> menuList() {
		log.info("메뉴 목록 조회" + menuService.menuSelectAll().toString());
		return  menuService.menuSelectAll();
	}
	
	// 권한에 대한 메뉴 목록 조회
	@GetMapping("/{authorityCd}")
	public List<MenuInfoDTO> authorityMenuList(@PathVariable String authorityCd) {
		log.info("권한에 대한 메뉴 목록 조회" + menuService.authorityMenuSelectAll(authorityCd).toString());
		
		return menuService.authorityMenuSelectAll(authorityCd);
	}
	
}	
