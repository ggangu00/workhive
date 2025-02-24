package egovframework.com.approval.service;

import lombok.Data;

@Data
public class File {
	//head
	private String atchFileId;
	private String createDt;
	private String useAt;
	//detail
	private String fileSn;
	private String fileStreCours;
	private String streFileNm;
	private String orignlFileNm;
	private String fileExtsn;
	private String fileCn;
	private String fileSize;
}
