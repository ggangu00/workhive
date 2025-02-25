package egovframework.com.vacation.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.vacation.mapper.YearVcMapper;
import egovframework.com.vacation.service.YearVcDTO;
import egovframework.com.vacation.service.YearVcService;

@Service("YearVcService")
public class YearVcServiceImpl implements YearVcService {
	
	@Resource
	private YearVcMapper yearVcMapper;

	@Override
	public boolean yearVcInsert(YearVcDTO yearVcDTO) {
		return yearVcMapper.yearVcInsert(yearVcDTO) == 1 ? true : false;
	}

	@Override
	public boolean yearVcUpdate(YearVcDTO yearVcDTO) {
		return yearVcMapper.yearVcUpdate(yearVcDTO) == 1 ? true : false;
	}

	@Override
	public YearVcDTO yearVcSelect(YearVcDTO yearVcDTO) {
		return yearVcMapper.yearVcSelect(yearVcDTO);
	}

}
