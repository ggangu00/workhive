package egovframework.com.todo.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.todo.mapper.TodoMapper;
import egovframework.com.todo.service.TodoDTO;
import egovframework.com.todo.service.TodoService;

@Service("TodoService")
public class TodoServiceImpl implements TodoService{
	
	@Resource
	private TodoMapper todoMapper;
	
	//일지 전체조회
	@Override
	public List<TodoDTO> todoSelectAll(Date todoDt) {
		return todoMapper.todoSelectAll(todoDt);
	}
	
	//일지 단건조회
	@Override
	public TodoDTO todoSelect(String todoCd) {
		return todoMapper.todoSelect(todoCd);
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

	//일지 삭제
	@Override
	public boolean todoDelete(String todoCd) {
		return todoMapper.todoDelete(todoCd) == 1 ? true : false;
	}
	
}
