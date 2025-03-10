package egovframework.com.department.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	// 부서 단건조회
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
	
	// 부서 등록
	@PostMapping("")
	public Map<String, Object> departmentAdd(@RequestBody DepartmentDTO dto) {
		log.info("부서등록 정보 => ", dto.toString());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        dto.setCreateId(user.getUserDTO().getMberId());
        
        if(dto.getParentCd() == null) {
        	dto.setParentCd("0");
        	log.info("부모코드가 null이 들어옴 => ", dto.getParentCd());
        }
        
		log.info("로그인 한 아이디 => ", user.getUserDTO().getMberId());
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("result", deptService.departmentInsert(dto));
        map.put("deptList", deptService.departmentSelectAll());
        
		return map;
	}
	
	// 부서 수정
	@PutMapping("")
	public Map<String, Object> departmentModify(@RequestBody DepartmentDTO dto) {
		log.info("부서수정 정보 => ", dto.toString());
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("result", deptService.departmentUpdate(dto));
        map.put("deptList", deptService.departmentSelectAll());
        
		return map;
	}
	
	// 부서 삭제
	@DeleteMapping("")
	public Map<String, Object> departmentRemove(@RequestBody List<String> deptCdList) {
		log.info("부서삭제 정보 => ", deptCdList);
		
		Map<String, Object> map = new HashMap<>();
		
		int result = deptService.departmentDelete(deptCdList);
		
		map.put("result", result);
        map.put("deptList", deptService.departmentSelectAll());
        
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
