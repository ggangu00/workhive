package egovframework.com.todo.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
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
	
	//일지 전체조회
	@GetMapping("/list/{todoDt}")
	public List<TodoDTO> todoList(@PathVariable("todoDt") String todoDt) {
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
	
	//일지 날짜별 건수조회
	@GetMapping("/list/cnt")
	public List<TodoDTO> todoListCnt(
								@RequestParam("year") String year,
					            @RequestParam("month") String month) {

        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("month", month);
        
        return todoService.todoSelectCnt(response);
	}	
	
	//일지 등록
	@PostMapping("")
	public Map<String, Object> todoAdd(@Validated TodoDTO todo) {
		
	  boolean result = todoService.todoInsert(todo);
	  
	  String todoDt = todo.getTodoDt().replaceAll("-", ""); //하이픈 제거
	  	  
	  Map<String, Object> map = new HashMap<>();
	  map.put("result", result);
	  map.put("list", todoService.todoSelectAll(todoDt));
		
	  return map;
	}
	
	//일지 수정
	@PutMapping("")
	public Map<String, Object> todoModify(@RequestBody TodoDTO todo) {
		
		Map<String, Object> map = new HashMap<>();
		
		boolean result = todoService.todoUpdate(todo);
		String todoDt = todo.getTodoDt();
		
		map.put("result", result);
		map.put("list", todoService.todoSelectAll(todoDt));
		
		return map;
	}
	
	//일지 상태변경
	@PutMapping("/state")
	public boolean todoStateModify(@RequestBody TodoDTO todo) {		
		List<String> todoArr = todo.getTodoArr();
	    String state = todo.getState();
	    log.info("결과아아ㅏㅏ ==> "+state);
	    
	    return todoService.todoStateUpdate(todoArr,state);
	}
	
	//일지 삭제
	@DeleteMapping("/delete")
	public boolean todoRemove(@RequestBody Map<String, List<String>> requestBody) {

	    List<String> todoArr = requestBody.get("todoArr");

	    if (todoArr == null || todoArr.isEmpty()) {
	        return false;
	    }

	    return todoService.todoDelete(todoArr);
	}
}