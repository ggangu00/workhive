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
	private String delYn;				// 삭제 여부
	
	// 이력 히스토리
	private String histCd;	// 이력히스토리 코드
	private String typeCd;	// CRUD 이력 타입
	private String tblNm;	// 이력이 일어난 테이블
	
	// 등록자, 등록일, 수정자, 수정일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;		// 등록일
	
	private String createId;	// 등록자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;		// 수정일
	
	private String updateId;	// 수정자
	
	// 홈 대시보드 건수 조회 (진행중인 프로젝트, 금일 예정 일정, 미완료 일지)
	private String memCd;
	private Long projectCtn;
	private Long todo_cnt;
	private Long sch_cnd;	
	
}
