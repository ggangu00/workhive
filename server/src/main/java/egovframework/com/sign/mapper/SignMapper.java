package egovframework.com.sign.mapper;

import java.util.List;

import egovframework.com.sign.service.SignDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("signMapper")
public interface SignMapper {
	// 결재자 목록 조회
	List<SignDTO> signerSelectAll(SignDTO signDTO);
	// 결재자 목록 카운트
	int signerSelectCnt(SignDTO signDTO);
}
