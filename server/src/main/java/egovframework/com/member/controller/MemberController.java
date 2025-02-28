package egovframework.com.member.controller;

import java.util.List;

import javax.annotation.Resource;

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
	@GetMapping("/memList")
	public List<UserDTO> memList() {
	  
	  List<UserDTO> result = memberService.memberSelectAll();
	  
	  return result;
	}
}
