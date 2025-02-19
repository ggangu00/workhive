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
	
	private String prCd;
	
	@NotBlank
	private String typeCd;
	
	@NotBlank
	private String prNm;
	private Date aheadDt;
	
	@NotBlank
	private Date startDt;
	
	@NotBlank
	private Date endDt;
	private String price;
	private String state;
	private String updateId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;
	private String createId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;
	private String entrprsMberId;
	private String comNm;
	private String delYn;
	
	//======================프로젝트 과업=====================
	
	private String prWorkCd;
	private String prWorkNm;
	private Long seq;
	private Long progress;
	
	//======================프로젝트 일정=====================
	
	private String prPlanCd;
	private String planNm;
	private String startWeek;
	private String endWeek;
	private String color;
}