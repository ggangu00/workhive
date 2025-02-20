package egovframework.com.commute.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.commute.mapper.CommuteCrctMapper;
import egovframework.com.commute.service.CommuteCrctDTO;
import egovframework.com.commute.service.CommuteCrctService;

@Service("CrctService")
public class CommuteCrctServiceImpl implements CommuteCrctService {

	@Resource
	private CommuteCrctMapper crctMapper;
	
	@Override
	public boolean crctInsert(CommuteCrctDTO crctDTO) {
		return crctMapper.crctInsert(crctDTO) == 1 ? true : false;
	}

	@Override
	public boolean crctUpdate(CommuteCrctDTO crctDTO) {
		return crctMapper.crctUpdate(crctDTO) == 1 ? true : false;
	}

	@Override
	public boolean crctDelete(String crctCd) {
		return crctMapper.crctDelete(crctCd) == 1 ? true : false;
	}

	@Override
	public CommuteCrctDTO crctSelect(String crctCd) {
		return crctMapper.crctSelect(crctCd);
	}

	@Override
	public List<CommuteCrctDTO> crctSelectAll(CommuteCrctDTO crctDTO) {
		return crctMapper.crctSelectAll(crctDTO);
	}

	@Override
	public List<CommuteCrctDTO> crctSelectSigner(CommuteCrctDTO crctDTO) {
		return crctMapper.crctSelectSigner(crctDTO);
	}

	@Override
	public List<CommuteCrctDTO> crctSelectSigned(CommuteCrctDTO crctDTO) {
		return crctMapper.crctSelectSigned(crctDTO);
	}

	@Override
	public boolean signUpdate(CommuteCrctDTO crctDTO) {
		return crctMapper.signUpdate(crctDTO) == 1 ? true : false;
	}

}
