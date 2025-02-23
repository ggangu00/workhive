package egovframework.com.menu.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor //Builder, All, No 세투세투
public class MenuInfoDTO {
	
	// 메뉴 정보
	private String menuCd;			// 메뉴 코드
	private String menuNm;			// 메뉴명
	private int depth;				// 깊이
	private String parentMenuCd;	// 바로 위 부모코드
	private String url;				// url	
	private String routerNm;		// vue router
	private String iconClass;		// 메뉴 아이콘
	private int seq;				// 순서
	private String useYn;			// 사용여부
	private String delYn;			// 삭제 여부		
	
	// 등록자, 등록일, 수정자, 수정일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;		// 등록일
	
	private String createId;	// 등록자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;		// 수정일
	
	private String updateId;	// 수정자
	
	
		
}
