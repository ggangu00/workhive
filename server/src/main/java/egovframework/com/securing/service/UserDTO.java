package egovframework.com.securing.service;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserDTO {
	
	private List<String> roles;
	
	private String mberId;			// 회원ID	
	private String password;		//비밀번호
	private String password_hint;	// 비밀번호 힌트
	private String password_cnsr;	// 비밀번호 정답
	private String ihidnum;			// 주민등록 번호
	private String mberNm;			// 회원명
	private String zip;				// 우편번호
	private String adres;			// 주소
	private String areaNo;			// 지역번호
	private String mberSttus;		// 회원상태
	private String detailAdres;		// 상세주소
	private String endTelno;		// 끝 전화번호
	private String mbtlnum;			// 이동전화번호
	private String groupId;			// 그룹ID
	private String mberFxnum;		// 회원 팩스 번
	
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String mberEmailAdres;	// 회원 이메일 주소
	private String meddleTelno;		// 중간 전화번호
			
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date sbscrbDe;			// 가입일
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date quitDt;			// 퇴사일
	private String sexdstnCode;		// 성별코드
	private String esntlId;			// 고유 ID
	private String lockAt;			// 계정 잠금 여부
	private int lockCnt;			// 잠금 횟수
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String lockLastPnttm;	// 잠금 최종 시점
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;			// 등록일시
	
	private String createId;		// 등록자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date update_dt;			// 수정일시
	
	private String updateId;		// 수정자
	private String delYn;			// 삭제여부
	private String respCd;			// 직책코드
	private String respNm;      	// 직책명
	private String gradeCd;			// 직급코드
	private String deptCd;			// 부서코드
	private String deptNm;     		// 부서명
	
	// 직급정보
	private String gradeNm;
}
