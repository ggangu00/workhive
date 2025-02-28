package egovframework.com.uss.olp.mgt.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingMemberVO {
	private String mtgId;	// 회의코드
	private String mberId;	// 회원ID
	private String gradeCd;	// 직책명
	private String deptCd;	// 부서명

}
