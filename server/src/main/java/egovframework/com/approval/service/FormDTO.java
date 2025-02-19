package egovframework.com.approval.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDTO {
	private String formCd; //양식코드
	private String formType; //양식유형
	private String formFile; //양식
}
