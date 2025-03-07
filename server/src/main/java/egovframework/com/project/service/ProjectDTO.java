package egovframework.com.project.service;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import egovframework.com.common.util.CommonPaging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO extends CommonPaging {
	
	//======================프로젝트=====================
	
	private String mode;		// 작업수행 구분
	
	private String prCd;		// 프로젝트 코드
	
	@NotBlank
	private String typeCd;		// 프로젝트 구분
	
	@NotBlank
	private String prNm;		// 프로젝트명
	private Date aheadDt;		// 내부완료일
	
	@NotBlank
	private Date startDt;		// 시작일
	
	@NotBlank
	private Date endDt;			// 종료일
	private String price;		// 금액
	private String state;		// 완료여부
	private String updateId;	// 수정자 id
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;		// 수정일
	private String createId;	// 등록자 id
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;		// 프로젝트 등록일
	private String entrprsMberId;	// 거래처
	private String comNm;		// 거래처명
	private String delYn;		// 삭제여부
	
	private List<String> projectArr; 	// 다중삭제 코드 배열

    public List<String> getProjectArr() {
        return projectArr;
    }

    public void setProjectArr(List<String> projectArr) {
        this.projectArr = projectArr;
    }
	
	//======================프로젝트 과업=====================
	
    private List<ProjectWorkDTO> workArr;		// 프로젝트 과업 목록
    
    public List<ProjectWorkDTO> getWorkArr() {
        return workArr;
    }

    public void setWorkArr(List<ProjectWorkDTO> workArr) {
        this.workArr = workArr;
    }
    
	//======================프로젝트 일정=====================
	
	private String prPlanCd;	// 일정코드
	private String planNm;		// 일정명
	private String color;		// 일정 색상
	
	//======================프로젝트 참여자 관리=====================
	private int parent;			// 프로젝트별 참여자 트리에서 부모 여부
	private String mberId;		// 프로젝트별 참여자 아이디
	private String mberNm;		// 프로젝트별 참여자 이름
	private String deptNm;		// 프로젝트별 참여자 부서
	private String gradeNm;		// 프로젝트별 참여자 직급
	private String mgrSt;		// 프로젝트 담당자 여부
}