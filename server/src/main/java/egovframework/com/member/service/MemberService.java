package egovframework.com.member.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.com.securing.service.UserDTO;

/**
 * 회원 관리 서비스 인터페이스 회원 정보 조회, 로그인 실패 처리, 계정 잠금 관리 등의 기능 제공
 */
public interface MemberService {

	// 회원 전체조회
	public List<UserDTO> memberSelectAll();

	// 회원 단건조회
	public UserDTO memberSelect(String mberId);

	// 회원 등록
	public boolean memberInsert(UserDTO dto);
	
	// 회원 수정
	public boolean memberUpdate(UserDTO dto);
	
	/**
	 * 로그인 실패 횟수 업데이트
	 * 
	 * @param mberId    업데이트할 회원의 아이디
	 * @param failCount 설정할 실패 횟수 (로그인 실패 누적 횟수)
	 */
	public void memberLoginFailCountUpdate(@Param("mberId") String mberId, @Param("failCount") int failCount);

	/**
	 * 계정 잠금 상태 업데이트
	 * 
	 * @param mberId 잠금 처리할 회원의 아이디
	 * @param lockAt 잠금 여부 (Y: 잠금, N: 잠금 해제)
	 */
	public void memberLockUpdate(@Param("mberId") String mberId, @Param("lockAt") String lockAt);

	// 부서별 사원조회
	public Map<String, Object> memberToDepartmentSelectAll(String deptCd);
}
