package egovframework.com.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.common.mapper.CommonMapper;
import egovframework.com.common.service.CommonDTO;
import egovframework.com.common.service.CommonService;

@Service("CommonService")
public class CommonServiceImpl implements CommonService{
	
	@Resource
	private CommonMapper commonMapper;
	
	@Override
	public List<CommonDTO> commonSelectAll(String commCd) {
		return commonMapper.commonSelectAll(commCd);
	}
}
