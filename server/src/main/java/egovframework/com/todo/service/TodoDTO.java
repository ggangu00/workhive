package egovframework.com.todo.service;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
	
	private String todoCd;		// 업무 코드
	private String mberId;		// 구성원 코드
	
	@NotBlank
	private String typeCd;		// 업무 구분
	private String typeNm;		// 업무 구분명
	
	@NotBlank
	private String title;		// 업무제목
	private String content;		// 업무 상세내용
	
	private String todoDt;		// 업무일자
	private String state;		// 완료여부
	private String year;		// 완료여부
	private String month;		// 완료여부
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;		// 프로젝트 등록일
	private Long dateCnt;		// 날짜별 건수
	
	private List<String> todoArr;

    public List<String> getTodoArr() {
        return todoArr;
    }

    public void setTodoArr(List<String> todoArr) {
        this.todoArr = todoArr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}