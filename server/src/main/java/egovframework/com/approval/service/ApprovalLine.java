package egovframework.com.approval.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalLine {
	
	private String mberId;
	private String mberNm;
	private String deptNm;
	private String docCd;
	private int signSeq;
	private String signStat;
	private String signOpn;
	private String signDt;
	private String signName;
	private String respNm;
	private String gradeNm;
	
	private List<String> approvalArr;
}
