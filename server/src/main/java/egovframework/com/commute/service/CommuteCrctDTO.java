package egovframework.com.commute.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommuteCrctDTO {
	private String crctCd;
	private String commuteCd;
	private String crctGoTime;
	private String crctLeaveTime;
	private String crctReason;
	private String atchFileId;
	private String createId;
	private String createDt;
	private String signId;
	private String signDt;
	private String signState;
	
	private String startDate;
	private String endDate;
	
	
}
