package egovframework.com.approval.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
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
