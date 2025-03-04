package egovframework.com.member.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import egovframework.com.member.mapper.MemberMapper;
import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.UserDTO;

/**
 * 회원(Member) 관리 서비스 구현 클래스
 * 회원 정보 조회, 로그인 실패 횟수 업데이트, 계정 잠금 관리 등의 기능을 실제 구현
 */
@Service("memberService") // 스프링 서비스 빈 등록
public class MemberServiceImpl implements MemberService {
	
	@Resource private MemberMapper memberMapper; // MemberMapper 주입

	// 회원 전체 조회
	@Override
	public List<UserDTO> memberSelectAll() {
		return memberMapper.memberSelectAll();
	}

	// 회원 단건조회
	@Override
	public UserDTO memberSelect(String userId) {
		return memberMapper.memberSelect(userId);
	}
	
	// 회원 등록
	@Override
	public boolean memberInsert(UserDTO dto) {
		return memberMapper.memberInsert(dto) == 1 ? true : false;
	}

	// 회원 수정
	@Override
	public boolean memberUpdate(UserDTO dto) {
		return memberMapper.memberUpdate(dto) == 1 ? true : false;
	}

	/**
	 * 로그인 실패 횟수 업데이트
	 * @param mberId 회원ID
	 * @param failCount 업데이트할 실패 횟수
	 */
	@Override
	public void memberLoginFailCountUpdate(@Param("mberId") String mberId, @Param("failCount") int failCount) {
	    memberMapper.memberLoginFailCountUpdate(mberId, failCount);
	}

	/**
	 * 계정 잠금 상태 업데이트
	 * @param mberId 회원ID
	 * @param lockAt 잠금 여부 (Y/N)
	 */
	@Override
	public void memberLockUpdate(@Param("mberId") String mberId, @Param("lockAt") String lockAt) {
	    memberMapper.memberLockUpdate(mberId, lockAt);
	}

}
