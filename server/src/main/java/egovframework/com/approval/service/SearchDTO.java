package egovframework.com.approval.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
	private int page=1;       
    private int pageUnit=5;       
    private String keyword; 
    private String status; //현재결재상태
    private String formCd;
    
    public int getStart() {
        return (page - 1) * pageUnit + 1; // 시작 번호
    }

    public int getEnd() {
        return page * pageUnit; // 끝 번호
    }
    
    
}
