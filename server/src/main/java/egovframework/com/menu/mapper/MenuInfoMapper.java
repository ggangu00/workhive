package egovframework.com.menu.mapper;

import java.util.List;

import egovframework.com.menu.service.MenuInfoDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuInfoMapper")
public interface MenuInfoMapper {

	// 메뉴 목록 조회
	public List<MenuInfoDTO> menuSelectAll();

	// 권한에 대한 메뉴 목록 조회
	public List<MenuInfoDTO> authorityMenuSelectAll(String authorityCd);
}
