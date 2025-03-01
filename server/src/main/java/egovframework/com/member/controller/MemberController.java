package egovframework.com.member.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.UserDTO;
import lombok.extern.slf4j.Slf4j;

@RestController // data 
@RequestMapping("/member")
@Slf4j
public class MemberController {
	
	@Resource
	private  MemberService memberService;
	
	//사원 전체조회
	@GetMapping("")
	public UserDTO memList(HttpSession session) {
		log.info("===== [MemberController 호출됨] =====");

		UserDTO user = (UserDTO) session.getAttribute("loginUser");
		String userId = user.getMberId();
		log.info("세션에서 가져온 사용자 정보: {}", user);
	
	  return memberService.memberSelect(userId);
	}
}
