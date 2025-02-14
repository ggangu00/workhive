package egovframework.com.project.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor //Builder, All, No 세투세투
public class ProjectDTO {
	private String prCd;
	private String typeCd;
	private String prNm;
	private String aheadDt;
	private String endDt;
	private String price;
	private String state;
	private String updateId;
	private String updateDt;
	private String createId;
	private String createDt;
	private String entrprsMberId;
}