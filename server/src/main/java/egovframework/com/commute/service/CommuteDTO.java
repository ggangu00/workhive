package egovframework.com.commute.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommuteDTO {
	private String commuteCd;
	private String memCd;
	private String commuteDt;
	private String goTime;
	private String goState;
	private String leaveTime;
	private String leaveState;
	private long workTime;
	private long overWorkTime;
	
	private String startDate;
	private String endDate;
}
