package egovframework.com.approval.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reception {

	private String receptCd;
	private String receptDt;
	private String receptYn;
	private String docCd;
	private String mberId;
	private String deptCd;
	private String mberNm;
	private String deptNm;
	private String respNm;
	private String gradeNm;
	
	private List<String> receptlArr;
}
