package egovframework.com.common.service;

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
	
	//공통함수
	private String commCd;
	private String commDtlNm;
	private String commDtlCd;
	
	//거래처
	private String entrprsMberId;
	private String cmpnyNm;
	private String bizrno;
	private String cxfc;
	private String zip;
	private String areaNo;
	private String entrprsMiddleTelno;
	private String entrprsEndTelno;
	private String fxnum;
	private String updateId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String updateDt;
	private String createId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String createDt;
	private String delYn;
	
	
}
