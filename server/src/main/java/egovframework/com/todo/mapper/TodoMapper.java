package egovframework.com.todo.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.todo.service.TodoDTO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("todoMapper")
public interface TodoMapper {
	
	// 일지 전체조회
	public List<TodoDTO> todoSelectAll(String todoDt);
	
	// 일지 단건조회
	public TodoDTO todoSelect(String todoCd);
	
	// 일지 날짜별 건수조회
	public List<TodoDTO> todoSelectCnt(Map<String, Object> response);
	
	// 일지 등록
	public int todoInsert(TodoDTO todoDTO);
	
	// 일지 수정
	public int todoUpdate(TodoDTO todoDTO);
	
	// 일지 상태수정
	public int todoStateUpdate(TodoDTO todoDTO);

	// 일지 삭제
	public int todoDelete(List<String> todoArr);

}
