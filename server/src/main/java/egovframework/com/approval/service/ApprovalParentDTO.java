package egovframework.com.approval.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalParentDTO {

		Document document;
		List<ApprovalLine> approvalLine;
		List<Reception> reception;
		List<File> file;
}
