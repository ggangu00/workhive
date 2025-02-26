package egovframework.com.approval.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Document {
	//문서
    private String docCd;           
    private String docTitle;                 
    private String docCnEditor;     
    private String mberId;           
    private String crntSignStat;    
    private String draftDt;     
    private String completeDt;  
    private String docKind;         
    private String atchFileId;     
    private String formCd;
    private String deptNm;
    private String delYn;
    private String formNm;
}
