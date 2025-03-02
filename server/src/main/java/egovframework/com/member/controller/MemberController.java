package egovframework.com.member.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.UserDTO;

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
	public ResponseEntity<?> memberInfo(@AuthenticationPrincipal CustomerUser customerUser) {
	    UserDTO user = customerUser.getUserDTO();
	    return ResponseEntity.ok(user);
	}
}