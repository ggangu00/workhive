package egovframework.com.vacation.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.vacation.mapper.VacationMapper;
import egovframework.com.vacation.service.VacationDTO;
import egovframework.com.vacation.service.VacationService;

@Service("VacationService")
public class VacationServiceImpl implements VacationService {

	@Resource
	private VacationMapper vacationMapper;
	
	@Override
	public boolean vcInsert(VacationDTO vcDTO) {
		return vacationMapper.vcInsert(vcDTO) == 1 ? true : false;
	}

	@Override
	public boolean vcUpdate(VacationDTO vcDTO) {
		return vacationMapper.vcUpdate(vcDTO) == 1 ? true : false;
	}

	@Override
	public boolean vcDelete(String vcCd) {
		return vacationMapper.vcDelete(vcCd) == 1 ? true : false;
	}

	@Override
	public VacationDTO vcSelect(String vcCD) {
		return vacationMapper.vcSelect(vcCD);
	}

	@Override
	public List<VacationDTO> vcSelectAll(VacationDTO vcDTO) {
		return vacationMapper.vcSelectAll(vcDTO);
	}

	@Override
	public VacationDTO expectSelect(VacationDTO vcDTO) {
		return vacationMapper.expectSelect(vcDTO);
	}

	@Override
	public List<VacationDTO> vcSelectSigner(VacationDTO vcDTO) {
		return vacationMapper.vcSelectSigner(vcDTO);
	}

	@Override
	public List<VacationDTO> vcSelectSigned(VacationDTO vcDTO) {
		return vacationMapper.vcSelectSigned(vcDTO);
	}

	@Override
	public boolean signUpdate(VacationDTO vcDTO) {
		return vacationMapper.signUpdate(vcDTO) == 1 ? true : false;
	}

}
