package egovframework.com.menu.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.menu.mapper.MenuInfoMapper;
import egovframework.com.menu.service.MenuInfoDTO;
import egovframework.com.menu.service.MenuInfoService;

@Service("menuInfoService")
public class MenuInfoServiceImpl implements MenuInfoService {

	@Resource
	private MenuInfoMapper menuMapper;
	
	// 메뉴 목록 조회
	@Override
	public List<MenuInfoDTO> menuSelectAll() {
		return menuMapper.menuSelectAll();
	}
	
	// 권한에 대한 메뉴 목록 조회
	@Override
	public List<MenuInfoDTO> authorityMenuSelectAll(String authorityCd) {
		
		return menuMapper.authorityMenuSelectAll(authorityCd);
	}

	// 로그인한 사용자의 접근 권한에 따른 메뉴 목록
	@Override
	public List<MenuInfoDTO> memberAuthorityMenuSelect(String mberId) {

		return menuMapper.memberAuthorityMenuSelect(mberId);
	}
}
