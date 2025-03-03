package egovframework.com.department.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.department.service.DepartmantService;
import egovframework.com.department.service.DepartmentDTO;
import egovframework.com.securing.service.CustomerUser;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
	
	@Resource
	private DepartmantService deptService;
	
	// 부서 전체조회
	@GetMapping("")
	public List<DepartmentDTO> departmentList() {
		return deptService.departmentSelectAll();
	}
	
	@GetMapping("/{deptCd}")
	public Map<String, Object> departmentInfo(@PathVariable(name="deptCd") int deptCd) {
		log.info(Integer.toString(deptCd));
		
		Map<String, Object> map = new HashMap<>();
		
		// 수정하기 위한 정보 조회
		DepartmentDTO infoDto = deptService.departmentSelect(deptCd);
	    
		if (infoDto != null) {
	        map.put("result", true);
	        map.put("info", infoDto);
	    } else {
	    	map.put("result", false);
	    	map.put("info", "해당 권한이 없습니다.");
	    }
		
		return map;
	}
	
	// 부서 트리 조회 : 사용자의 부서 기준 관련 부서 검색
	@GetMapping("/treeList")
	public List<DepartmentDTO> deptTreeList() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String mberDeptCd = user.getUserDTO().getDeptCd();
        
        return deptService.deptTreeSelectAll(mberDeptCd);
	}
}
