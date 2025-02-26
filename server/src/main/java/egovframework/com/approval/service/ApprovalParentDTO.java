package egovframework.com.approval.service;

import java.util.List;

import egovframework.com.cmm.service.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalParentDTO {

	private Document document;
	private List<ApprovalLine> approvalLine;
	private List<Reception> reception;
	private List<FileVO> fileList; 

	private FileVO fileVO;
}
