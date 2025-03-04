package egovframework.com.vacation.service;

import egovframework.com.common.util.CommonPaging;
import egovframework.com.commute.service.CommuteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationDTO extends CommonPaging {
	private String vcCd;
	private String vcStartDt;
	private String vcEndDt;
	private String vcType;
	private String useDays;
	private String vcReason;
	private String atchFileId;
	private String createId;
	private String createDt;
	private String updateId;
	private String updateDt;
	private String signId;
	private String signDt;
	private String signState;

	// 날짜 조회 조건
	private String startDate;
	private String endDate;
	
	// 사용 예정량 조회
	private String signWait;
	private String signWaitDays;
	private String signSup;
	private String signSupDays;

	private String signNm;
}
