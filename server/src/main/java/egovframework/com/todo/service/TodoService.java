package egovframework.com.todo.service;

import java.util.List;
import java.util.Map;

public interface TodoService {
	
	//일지 전체조회
	public List<TodoDTO> todoSelectAll(String todoDt);
	
	//일지 단건조회
	public TodoDTO todoSelect(String todoCd);
	
	//일지 날짜별 건수조회
	public List<TodoDTO> todoSelectCnt(Map<String, Object> response);
	
	//일지 등록
	public boolean todoInsert(TodoDTO todo);
	
	//일지 수정
	public boolean todoUpdate(TodoDTO todo);
	
	//일지 삭제
	public boolean todoDelete(List<String> todoArr);
}
