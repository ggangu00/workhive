package egovframework.com.member.service.impl;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.com.common.util.MailUtil;
import egovframework.com.member.mapper.MemberMapper;
import egovframework.com.member.service.FindPasswordService;
import egovframework.com.securing.service.UserDTO;
import lombok.RequiredArgsConstructor;

/**
 * 비밀번호 찾기 서비스 구현체
 * 아이디 기반으로 회원 정보 조회 후, 등록된 이메일로 임시 비밀번호 발송 및 비밀번호 갱신 처리
 */
@Service("findPasswordService")
@RequiredArgsConstructor
public class FindPasswordServiceImpl implements FindPasswordService {

	private final MemberMapper memMapper;		// 회원 데이터 조회 및 갱신 Mapper
	private final MailUtil mailUtil;			// 이메일 발송 유틸
	private final PasswordEncoder passwordEncoder;	// 비밀번호 암호화

	/**
	 * 비밀번호 찾기 처리
	 * @param mberId 회원 아이디
	 * @return 처리 성공 여부 (이메일 발송 성공 시 true)
	 */
	@Override
	public boolean findPassword(String mberId) {
		// 1. 아이디로 회원 정보 조회
		UserDTO user = memMapper.memberSelect(mberId);

		// 2. 회원이 없거나 이메일이 등록 안되어 있으면 false 반환
		if (user == null || user.getMberEmailAdres() == null) {
			return false;
		}

		// 3. 임시 비밀번호 생성
		String tempPassword = generateTempPassword();

		// 4. 임시 비밀번호 암호화 후 DB 저장
		String encodedPassword = passwordEncoder.encode(tempPassword);
		memMapper.userPasswordUpdate(mberId, encodedPassword);

		// 5. 이메일 발송 (템플릿 적용 가능)
		mailUtil.sendTempPasswordEmail(user.getMberEmailAdres(), tempPassword);

		return true;
	}

	/**
	 * 랜덤 임시 비밀번호 생성 (영문+숫자 조합)
	 * @return 생성된 임시 비밀번호
	 */
	private String generateTempPassword() {
		return UUID.randomUUID().toString().substring(0, 8);  // 8자리 랜덤 비번
	}
}

