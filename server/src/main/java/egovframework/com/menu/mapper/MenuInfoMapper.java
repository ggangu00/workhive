package egovframework.com.menu.mapper;

import java.util.List;

import egovframework.com.menu.service.MenuInfoDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuInfoMapper")
public interface MenuInfoMapper {

	// 메뉴 목록 조회
	public List<MenuInfoDTO> menuSelectAll();
	
	// 1뎁스 메뉴 목록 조회
	public List<MenuInfoDTO> menu1DepthSelectAll();
	
	// 2뎁스 메뉴 목록 조회
	public List<MenuInfoDTO> menu2DepthSelectAll();
	
	// 3뎁스 메뉴 목록 조회
	public List<MenuInfoDTO> menu3DepthSelectAll();
}
