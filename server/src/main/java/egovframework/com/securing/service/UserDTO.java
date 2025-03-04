package egovframework.com.securing.service;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private List<String> roles;
	
	private String mberId;			// 회원ID	
	
	@JsonIgnore //json 응답에서 빠짐
	private String password;		//비밀번호
	
	@JsonIgnore //json 응답에서 빠짐
	private String password_hint;	// 비밀번호 힌트
	
	@JsonIgnore //json 응답에서 빠짐
	private String password_cnsr;	// 비밀번호 정답
	
	@JsonIgnore //json 응답에서 빠짐
	private String ihidnum;			// 주민등록 번호
	private String mberNm;			// 회원명
	
	@JsonIgnore //json 응답에서 빠짐
	private String zip;				// 우편번호
	
	@JsonIgnore //json 응답에서 빠짐
	private String adres;			// 주소
	private String areaNo;			// 지역번호
	
	@JsonIgnore //json 응답에서 빠짐
	private String mberSttus;		// 회원상태
	
	@JsonIgnore //json 응답에서 빠짐
	private String detailAdres;		// 상세주소
	private String endTelno;		// 끝 전화번호
	
	private String mbtlnum;			// 이동전화번호
	private String groupId;			// 그룹ID
	private String mberFxnum;		// 회원 팩스 번호
	
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String mberEmailAdres;	// 회원 이메일 주소
	
	private String middleTelno;		// 중간 전화번호
			
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date sbscrbDe;			// 가입일
	
	@JsonIgnore //json 응답에서 빠짐
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date quitDt;			// 퇴사일
	
	private String sexdstnCode;		// 성별코드
	private String esntlId;			// 사번코드
	
	@JsonIgnore //json 응답에서 빠짐
	private String lockAt;			// 계정 잠금 여부
	
	@JsonIgnore //json 응답에서 빠짐
	private int lockCnt;			// 잠금 횟수
	
	@JsonIgnore //json 응답에서 빠짐
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String lockLastPnttm;	// 마지막 잠금 일시
	
	@JsonIgnore //json 응답에서 빠짐
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String unlockId;		// 잠금 해제 관리자
	
	@JsonIgnore //json 응답에서 빠짐
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date unlockDt;			// 잠금 해제 일시
	
	@JsonIgnore //json 응답에서 빠짐
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;			// 등록일시
	
	@JsonIgnore //json 응답에서 빠짐
	private String createId;		// 등록자
	
	@JsonIgnore //json 응답에서 빠짐
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date update_dt;			// 수정일시
	
	@JsonIgnore //json 응답에서 빠짐
	private String updateId;		// 수정자
	
	@JsonIgnore //json 응답에서 빠짐
	private String delYn;			// 삭제여부
	
	private String respCd;			// 직책코드
	private String respNm;      	// 직책명
	private String gradeCd;			// 직급코드
	private String deptCd;			// 부서코드
	private String deptNm;     		// 부서명
	
	// 직급정보
	private String gradeNm;

	public int getLoginFailCount() {
	    return lockCnt;
	}

	public void setLoginFailCount(int loginFailCount) {
		this.lockCnt = loginFailCount;
	}

	public Object getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

}
