package egovframework.com.sign.service;

import java.util.List;

public interface SignService {

	// 결재자 목록 조회
	public List<SignDTO> signerSelectAll(SignDTO signDTO);
	// 결재자 목록 카운트
	public int signerSelectCnt(SignDTO signDTO);
}
