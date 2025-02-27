package egovframework.com.project.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWorkDTO {
	private String prCd;	// 과업코드
	private String prWorkCd;	// 과업코드
	private String prWorkNm;	// 과업명
	private Long seq;			// 과업순서
	private Long progress;		// 과업 진행률
	private String state;		// 과업 완료여부
	
	public void setPrCd(String prCd) {}
}
