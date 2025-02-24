package egovframework.com.approval.service;

import lombok.Data;

@Data
public class ApprovalLine {
	
	private String mberId;
	private String docCd;
	private int signSeq;
	private String signStat;
	private String signOpn;
	private String signDt;
	private String signName;
}
