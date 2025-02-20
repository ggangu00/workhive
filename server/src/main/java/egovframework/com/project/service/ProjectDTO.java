package egovframework.com.project.service;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
	
	//======================프로젝트=====================
	
	private String prCd;		// 프로젝트 코드
	
	@NotBlank
	private String typeCd;		// 프로젝트 구분
	
	@NotBlank
	private String prNm;		// 프로젝트명
	private Date aheadDt;		// 내부완료일
	
	@NotBlank
	private Date startDt;		// 시작일
	
	@NotBlank
	private Date endDt;			// 종료일
	private String price;		// 금액
	private String state;		// 완료여부
	private String updateId;	// 수정자 id
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;		// 수정일
	private String createId;	// 등록자 id
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;		// 프로젝트 등록일
	private String entrprsMberId;	// 거래처
	private String comNm;		// 거래처명
	private String delYn;		// 삭제여부
	
	//======================프로젝트 과업=====================
	
	private String prWorkCd;	// 과업코드
	private String prWorkNm;	// 과업명
	private Long seq;			// 과업순서
	private Long progress;		// 과업 진행률
	
	//======================프로젝트 일정=====================
	
	private String prPlanCd;	// 일정코드
	private String planNm;		// 일정명
	private String color;		// 일정 색상
}