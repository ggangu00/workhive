package egovframework.com.commute.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.commute.mapper.CommuteMapper;
import egovframework.com.commute.service.CommuteDTO;
import egovframework.com.commute.service.CommuteService;

@Service("CommuteService")
public class CommuteServiceImpl implements CommuteService {

	@Resource
	private CommuteMapper commuteMapper;
	
	// 출퇴근
	@Override
	public boolean cmtInsert(CommuteDTO commuteDTO) {
		return commuteMapper.cmtInsert(commuteDTO) == 1 ? true : false;
	}

	@Override
	public boolean cmtUpdate(CommuteDTO commuteDTO) {
		return commuteMapper.cmtUpdate(commuteDTO) == 1 ? true : false;
	}

	@Override
	public boolean cmtDelete(String commuteCd) {
		return commuteMapper.cmtDelete(commuteCd) == 1 ? true : false;
	}

	@Override
	public CommuteDTO cmtSelect(String commuteCd) {
		return commuteMapper.cmtSelect(commuteCd);
	}

	@Override
	public List<CommuteDTO> cmtSelectAll(CommuteDTO commuteDTO) {
		return commuteMapper.cmtSelectAll(commuteDTO);
	}

	@Override
	public CommuteDTO lastCmtSelect(String memCd) {
		return commuteMapper.lastCmtSelect(memCd);
	}
	
}
