package egovframework.com.commute.service;

import egovframework.com.common.util.CommonPaging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommuteDTO extends CommonPaging {
	private String commuteCd;
	private String mberId;
	private String commuteDt;
	private String goTime;
	private String goState;
	private String leaveTime;
	private String leaveState;
	private long workTime;
	private long overWorkTime;
	
	// 날짜 조회 조건
	private String startDate;
	private String endDate;
	
	// 출퇴근 시간
	private String startTime;
	private String endTime;
}
