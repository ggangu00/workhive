package egovframework.com.sign.service;

import egovframework.com.common.util.CommonPaging;
import lombok.Data;

@Data
public class SignDTO extends CommonPaging {

	private String mberId;			// 회원ID
	private String mberNm;			// 회원명
	private String mbtlnum;			// 이동전화번호
	
	private String respCd;			// 직책코드
	private String respNm;      	// 직책명
	private String gradeCd;			// 직급코드
	private String gradeNm;			// 직급명
	private String deptCd;			// 부서코드
	private String deptNm;     		// 부서명
}
