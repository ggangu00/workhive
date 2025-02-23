package egovframework.com.menu.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.menu.mapper.MenuInfoMapper;
import egovframework.com.menu.service.MenuInfoDTO;
import egovframework.com.menu.service.MenuInfoService;

@Service("MenuInfoService")
public class MenuInfoServiceImpl implements MenuInfoService {

	@Resource
	private MenuInfoMapper menuMapper;
	
	// 메뉴 목록 조회
	@Override
	public List<MenuInfoDTO> menuSelectAll() {
		return menuMapper.menuSelectAll();
	}

}
