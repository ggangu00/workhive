package egovframework.com.sec.security.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import egovframework.com.cmm.LoginVO;

import org.egovframe.rte.fdl.security.userdetails.EgovUserDetails;
import org.egovframe.rte.fdl.security.userdetails.jdbc.EgovUsersByUsernameMapping;

import javax.sql.DataSource;

/**
 * mapRow 결과를 사용자 EgovUserDetails Object 에 정의한다.
 * 
 * @author ByungHun Woo
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    -------------    ----------------------
 *   2009.03.10  ByungHun Woo    최초 생성
 *   2009.03.20  이문준          UPDATE
 *
 * </pre>
 */

public class EgovSessionMapping extends EgovUsersByUsernameMapping {
	
	/**
	 * **생성자 (Constructor)**
	 * - 데이터베이스에서 사용자 정보를 조회하기 위한 SQL 쿼리를 설정하는 생성자
	 * - 부모 클래스(EgovUsersByUsernameMapping)의 생성자를 호출하여 DataSource와 쿼리를 전달
	 *
	 * @param ds DataSource 객체 (DB 연결 정보)
	 * @param usersByUsernameQuery 사용자 정보를 조회하는 SQL 쿼리
	 */
	public EgovSessionMapping(DataSource ds, String usersByUsernameQuery) {
        super(ds, usersByUsernameQuery); // 부모 클래스의 생성자를 호출하여 DataSource와 SQL 쿼리를 설정
    }

	/**
	 * **mapRow() 메서드 Override**
	 * - 데이터베이스에서 조회한 `ResultSet`을 `EgovUserDetails` 객체로 변환
	 * - `ResultSet`은 SQL 실행 결과를 나타내는 객체
	 * 
	 * @param rs SQL 쿼리 실행 결과 (`ResultSet`)
	 * @param rownum 현재 행(Row) 번호
	 * @return EgovUserDetails 객체 (로그인 정보 포함)
	 * @throws SQLException SQL 실행 중 예외가 발생하면 예외를 던짐
	 */
	@Override
    protected EgovUserDetails mapRow(ResultSet rs, int rownum) throws SQLException {
    	logger.debug("## EgovUsersByUsernameMapping mapRow ##"); // 디버그 로그 출력

        /* === DB에서 조회한 사용자 정보 가져오기 === */
        String strUserId    = rs.getString("user_id");    // 사용자 ID
        String strPassWord  = rs.getString("password");   // 비밀번호 (암호화되어 저장됨)
        boolean strEnabled  = rs.getBoolean("enabled");   // 계정 활성화 여부 (1 = 활성화, 0 = 비활성화)

        String strUserNm    = rs.getString("user_nm");    // 사용자 이름
        String strUserSe    = rs.getString("user_se");    // 사용자 유형 (관리자, 일반 사용자 등)
        String strUserEmail = rs.getString("user_email"); // 사용자 이메일
        String strOrgnztId  = rs.getString("orgnzt_id");  // 조직 ID (사용자가 속한 부서)
        String strUniqId    = rs.getString("esntl_id");   // 고유 식별 ID (사용자를 유일하게 구분하는 값)

        /** 2010.06.30 추가된 조직명 정보 */
        String strOrgnztNm  = rs.getString("orgnzt_nm");  // 조직명 (사용자가 속한 조직의 이름)

        /* === 세션에 저장할 로그인 정보 객체 생성 === */
        LoginVO loginVO = new LoginVO(); // `LoginVO` 객체 생성 (로그인 사용자 정보 저장)
        loginVO.setId(strUserId);       // 사용자 ID 설정
        loginVO.setPassword(strPassWord); // 비밀번호 설정 (DB에 암호화된 상태로 저장됨)
        loginVO.setName(strUserNm);      // 사용자 이름 설정
        loginVO.setUserSe(strUserSe);    // 사용자 유형 설정
        loginVO.setEmail(strUserEmail);  // 사용자 이메일 설정
        loginVO.setOrgnztId(strOrgnztId);// 조직 ID 설정
        loginVO.setUniqId(strUniqId);    // 고유 식별 ID 설정
        /** 2010.06.30 추가된 조직명 설정 */
        loginVO.setOrgnztNm(strOrgnztNm);// 조직명 설정

        /* === EgovUserDetails 객체 반환 === */
        return new EgovUserDetails(strUserId, strPassWord, strEnabled, loginVO);
        // EgovUserDetails 객체 생성 후 반환
        // - strUserId: 사용자 ID
        // - strPassWord: 비밀번호
        // - strEnabled: 계정 활성화 여부
        // - loginVO: 로그인 정보 (사용자 ID, 이름, 이메일, 조직 정보 포함)
    }
}
