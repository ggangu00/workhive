package egovframework.com.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // data
@RequestMapping("/member")
public class MemberController {

	@Resource
	private MemberService memberService;

	// 사원 전체조회
	@GetMapping("")
	public List<UserDTO> memberList() {

		return memberService.memberSelectAll();
	}

	// 사원 단건 조회
	@GetMapping("/info")
	public UserDTO memberInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerUser user = (CustomerUser) auth.getPrincipal();
		String userId = user.getUserDTO().getMberId();

		return memberService.memberSelect(userId);
	}
	
	@PostMapping("")
	public Map<String, Object> memberAdd(@RequestBody UserDTO dto) {
		log.info("🆕 회원정보 등록 => {}", dto.toString());
		
		boolean result = memberService.memberInsert(dto);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);

		return map;
	}
	
	@PutMapping("")
	public Map<String, Object> memberModify(@RequestBody UserDTO dto) {
		log.info("✅ 회원정보 수정 => {}", dto.toString());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerUser user = (CustomerUser) auth.getPrincipal();
		dto.setMberId(user.getUserDTO().getMberId());
		
		boolean result = memberService.memberUpdate(dto);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);

		return map;
	}
	
	// 부서별 사원 조회
	@GetMapping("/deptToMem/{deptCd}")
	public List<UserDTO> memberToDepartmentList(@PathVariable("deptCd") String deptCd) {
		log.info("✅ 회원정보 수정 => {}", deptCd);
		
		return memberService.memberToDepartmentSelect(deptCd);
	}
}