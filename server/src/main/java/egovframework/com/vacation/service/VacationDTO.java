package egovframework.com.vacation.service;

import egovframework.com.commute.service.CommuteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationDTO {
	private String vcCd;
	private String vcStartDt;
	private String vcEndDt;
	private String vcType;
	private String useDays;
	private String vcReason;
	private String atchFileId;
	private String createId;
	private String createDt;
	private String signId;
	private String signDt;
	private String signState;

	// 날짜 조회 조건
	private String startDate;
	private String endDate;
}
