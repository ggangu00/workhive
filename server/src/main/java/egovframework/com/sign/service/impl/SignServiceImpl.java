package egovframework.com.sign.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.sign.mapper.SignMapper;
import egovframework.com.sign.service.SignDTO;
import egovframework.com.sign.service.SignService;

@Service("SignService") // 스프링 서비스 빈 등록
public class SignServiceImpl implements SignService {

	@Resource
	private SignMapper signMapper;
	
	@Override
	public List<SignDTO> signerSelectAll(SignDTO signDTO) {
		return signMapper.signerSelectAll(signDTO);
	}

	@Override
	public int signerSelectCnt(SignDTO signDTO) {
		return signMapper.signerSelectCnt(signDTO);
	}

}
