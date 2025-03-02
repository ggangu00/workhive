package egovframework.com.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.com.securing.service.UserDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * 회원(Member) 관리 매퍼 인터페이스
 * 회원 조회, 로그인 실패 횟수 업데이트, 계정 잠금 업데이트 등 정의
 */
@Mapper("memberMapper")
public interface MemberMapper {

	/**
	 * 전체 회원 조회
	 * @return 회원 목록
	 */
	List<UserDTO> memberSelectAll();

	/**
	 * 회원 단건 조회
	 * @param mberId 회원ID
	 * @return 회원 정보
	 */
	UserDTO memberSelect(String mberId);

	/**
	 * 로그인 실패 횟수 업데이트
	 * @param mberId 회원ID
	 * @param lockCnt 실패 횟수
	 */
	void memberLoginFailCountUpdate(@Param("mberId") String mberId, @Param("failCount") int failCount);

	/**
	 * 계정 잠금 상태 업데이트
	 * @param mberId 회원ID
	 * @param lockAt 잠금 여부 (Y/N)
	 */
	void memberLockUpdate(@Param("mberId") String mberId, @Param("lockAt") String lockAt);
}