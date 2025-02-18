package egovframework.com.project.service;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	@NotBlank
	private String typeCd;
	
	@NotBlank
	private String prNm;
	private String aheadDt;
	
	@NotBlank
	private String startDt;
	
	@NotBlank
	private String endDt;
	private String price;
	private String state;
	private String updateId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String updateDt;
	private String createId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String createDt;
	private String entrprsMberId;
	private String delYn;
}