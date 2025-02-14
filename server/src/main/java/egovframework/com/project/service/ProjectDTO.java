package egovframework.com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor //Builder, All, No 세투세투
public class ProjectDTO {
	private long prCd;
	private long typeCd;
	private String prNm;
	private String aheadDt;
	private String endDt;
	private String price;
	private long state;
	private long updateId;
	private String updateDt;
	private long createId;
	private String createDt;
	private String entrprsMberId;
}