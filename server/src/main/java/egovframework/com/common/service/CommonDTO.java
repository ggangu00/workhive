package egovframework.com.common.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor //Builder, All, No 세투세투
public class CommonDTO {
	
	// 공통함수
	private String commCd;		// 공통코드
	private String commDtlCd;	// 공통코드 디테일 코드
	private String commDtlNm;	// 공통코드 디테일명
	
	// 거래처
	private String entrprsMberId;		// 기업회원ID
	private String cmpnyNm;				// 회사명
	private String bizrno;				// 사업자등록번호
	private String cxfc;				// 대표이사
	private String zip;					// 우편번호
	private String areaNo;				// 지역번호
	private String entrprsMiddleTelno; 	// 중간번호
	private String entrprsEndTelno;		// 끝번호
	private String fxnum;				// 거래처 팩스
	
	
	// 홈 대시보드 건수 조회
	private String mberId;		// 구성원코드
	private String mberNm;		// 구성원이름
	private Long projectCnt;	// 진행중인 프로젝트 갯수
	private Long todoCnt;		// 미완료 일지 갯수
	private Long schCnt;		// 금일 예정 일정 갯수
	
	// 로그인 로그
	private String logCd;		// 로그코드
	private String logId;		// 로그인아이디
	private String logIp;		// 로그인ip
	private String state;		// 성공여부
	private String gradeNm;		// 직급명
	private String deptNm;		// 부서명
	private String lockAt;		// 로그인 잠금여부
	
	// 회사정보
	private String businessNo;	// 사업자등록번호
	private String compNm;		// 회사명
	private String ceoNm;		// 대표자
	private String tel;			// 대표번호
	private String email;		// 이메일
	private String telFax;		// 팩스
	private int post;			// 우편번호
	private String addr;		// 주소
	private String addrDtl;		// 상세주소
	
	@DateTimeFormat(pattern = "HH:mm:ss")
	private Date start_time;
	
	@DateTimeFormat(pattern = "HH:mm:ss")
	private Date end_time;
		
	// 이력 히스토리
	private String histCd;		// 이력히스토리 코드
	private String typeCd;		// CRUD 이력 타입
	private String tblNm;		// 이력이 일어난 테이블
	
	// 삭제 여부
	private String delYn;				
	
	// 등록자, 등록일, 수정자, 수정일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;		// 등록일
	
	private String createId;	// 등록자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;		// 수정일
	
	private String updateId;	// 수정자
		
}
