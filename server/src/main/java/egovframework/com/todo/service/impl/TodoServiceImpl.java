package egovframework.com.todo.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.todo.mapper.TodoMapper;
import egovframework.com.todo.service.TodoDTO;
import egovframework.com.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("TodoService")
public class TodoServiceImpl implements TodoService{
	
	@Resource
	private TodoMapper todoMapper;
	
	//일지 전체조회
	@Override
	public List<TodoDTO> todoSelectAll(String todoDt) {
		return todoMapper.todoSelectAll(todoDt);
	}
	
	//일지 단건조회
	@Override
	public TodoDTO todoSelect(String todoCd) {
		return todoMapper.todoSelect(todoCd);
	}
	
	//일지 날짜별 건수조회
	@Override
	public List<TodoDTO> todoSelectCnt(Map<String, Object> response) {
		return todoMapper.todoSelectCnt(response);
	}

	//일지 등록
	@Override
	public boolean todoInsert(TodoDTO todo) {
		return todoMapper.todoInsert(todo) == 1 ? true : false;
	}	

	//일지 수정
	@Override
	public boolean todoUpdate(TodoDTO todo) {
		return todoMapper.todoUpdate(todo) == 1 ? true : false;
	}
	
	@Override
	public boolean todoStateUpdate(List<String> todoArr, String state) {

	    TodoDTO todo = new TodoDTO();
	    todo.setTodoArr(todoArr);
	    todo.setState(state);

	    int updatedRows = todoMapper.todoStateUpdate(todo);

	    return updatedRows > 0;
	}

	//일지 삭제
	public boolean todoDelete(String todoCd) {

        return todoMapper.todoDelete(todoCd) == 1 ? true : false;
    }
}
