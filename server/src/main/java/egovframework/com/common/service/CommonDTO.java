package egovframework.com.common.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor //Builder, All, No 세투세투
public class CommonDTO {
	private String commCd;
	private String commDtlNm;
	private String commDtlCd;
	
}
