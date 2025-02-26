package egovframework.com.approval.service;

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
}
