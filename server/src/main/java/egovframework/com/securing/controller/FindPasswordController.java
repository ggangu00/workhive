package egovframework.com.securing.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.securing.service.FindPasswordRequest;
import egovframework.com.securing.service.FindPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@Slf4j
public class FindPasswordController {
	
	private final FindPasswordService passwordService;

	/**
	 * 비밀번호 찾기 요청 처리 아이디 기반으로 이메일 전송
	 */
//	@PostMapping("/find")
//	public ResponseEntity<?> findPassword(@RequestBody FindPasswordRequest request) {
//		passwordService.findPassword(request.getUsername());
//		return ResponseEntity.ok().body(Map.of("message", "임시 비밀번호가 이메일로 발송되었습니다."));
//	}
}
