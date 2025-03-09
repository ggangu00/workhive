package egovframework.com.department.service;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
	
	   private String deptCd;		// 부서코드
	   private int depth;			// 깊이
	   private String parentCd;		// 부모부서 코드
	   private int seq;				// 순서
	   private String deptNm;		// 부서명
	   private String description;	// 부서설명
	   private String manager;		// 부서장
	   private String delYn;		// 삭제여부
	   
	   @DateTimeFormat(pattern = "yyyy-MM-dd")
	   private Date createDt;		// 등록일
	   private String createId;		// 등록자
	   
	   @DateTimeFormat(pattern = "yyyy-MM-dd")
	   private Date updateDt;		// 수정일
	   private String updateId;		// 수정자
	   
	   private int level;
	   
	   private List<String> deptCdList;
}
