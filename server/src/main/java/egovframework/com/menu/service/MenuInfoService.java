package egovframework.com.menu.service;

import java.util.List;


public interface MenuInfoService {
	
	// 메뉴 목록 조회
	public List<MenuInfoDTO> menuSelectAll();
	
	// 권한에 대한 메뉴 목록 조회
	public List<MenuInfoDTO> authorityMenuSelectAll(String authorityCd);

	// 로그인한 사용자의 접근 권한에 따른 메뉴 목록
	public List<MenuInfoDTO> memberAuthorityMenuSelect(String mberId);
	
}
