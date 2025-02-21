package egovframework.com.todo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.todo.service.TodoDTO;
import egovframework.com.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/todo")
@Slf4j
public class TodoController {
	
	@Resource
	private  TodoService todoService;
	
	//======================프로젝트=====================
	
	//일지 전체조회
	@GetMapping("/list")
	public List<TodoDTO> todoList(@PathVariable("todoDt") Date todoDt) {	  
	  return todoService.todoSelectAll(todoDt);
	}	
	
	//일지 단건조회
	@GetMapping("/info/{todoCd}")
	public Map<String, Object> todoinfo(@PathVariable("todoCd") String todoCd) {
		
		Map<String, Object> map = new HashMap<>();
		
		//프로젝트 정보조회
		TodoDTO infoDto = todoService.todoSelect(todoCd);
		
		if (infoDto != null) {
	        map.put("result", true);
	        map.put("info", infoDto);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	//일지 등록
	@PostMapping("")
	public Map<String, Object> todoAdd(@Validated TodoDTO todo) {
		
	  boolean result = todoService.todoInsert(todo);
	  
	  Date todoDt = todo.getTodoDt();
	  	  
	  Map<String, Object> map = new HashMap<>();
	  map.put("result", result);
	  map.put("list", todoService.todoSelectAll(todoDt));
		
	  return map;
	}
	
	//일지 수정
	@PutMapping("")
	public Map<String, Object> todoModify(@RequestBody TodoDTO todo) {
		log.info("수정 권한 코드 출력 => " + todo.toString());
		
		Map<String, Object> map = new HashMap<>();
		
		boolean result = todoService.todoUpdate(todo);
		Date todoDt = todo.getTodoDt();
		
		map.put("result", result);
		map.put("list", todoService.todoSelectAll(todoDt));
		
		return map;
	}
	
	//일지 삭제
	@DeleteMapping("/{todoCd}")
	public Map<String, Object> todoRemove(@PathVariable(name="todoCd") String todoCd) {
		log.info("삭제 권한 코드 출력 => " + todoCd);
	    
	    // 서비스 로직 실행
		boolean result = todoService.todoDelete(todoCd);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		
		return map;
	}		
}