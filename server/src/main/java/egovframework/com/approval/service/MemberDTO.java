package egovframework.com.approval.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
	private String mberId;
	private String mberNm;
	private String respCd; //직책코드
	private String respNm; //직책코드
	private String deptCd;
	private String deptNm;
	private String gradeCd; //직급코드
	private String gradeNm; //직급이름
}
