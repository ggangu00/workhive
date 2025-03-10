package egovframework.com.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.member.service.FindPasswordService;
import egovframework.com.member.service.PasswordRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 비밀번호 찾기 컨트롤러
 * 아이디 기반으로 등록된 이메일로 임시 비밀번호 발송 처리
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FindPasswordController {

	private final FindPasswordService passwordService;

	/**
	 * 비밀번호 찾기 요청 처리
	 * @param dto 비밀번호 찾기 요청 정보 (아이디 포함)
	 * @return 처리 결과 (성공/실패 메시지)
	 */
//	@PostMapping("/passwordProc")
//	public ResponseEntity<?> findPassword(@RequestBody PasswordRequestDTO dto) {
//		log.info("🔎 비밀번호 찾기 요청 - username: {}", dto.getMberId());
//
//		Map<String, Object> response = new HashMap<>();
//
//		try {
//			boolean result = passwordService.findPassword(dto.getMberId());
//
//			if (result) {
//				response.put("code", HttpStatus.OK.value());
//				response.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");
//				return ResponseEntity.ok(response);
//			} else {
//				response.put("code", HttpStatus.NOT_FOUND.value());
//				response.put("message", "입력하신 아이디로 등록된 이메일 정보가 없습니다.");
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//			}
//		} catch (Exception e) {
//			log.error("❗ 비밀번호 찾기 처리 중 예외 발생", e);
//			response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.put("message", "서버 내부 오류가 발생했습니다.");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}
	
	@PostMapping("/passwordProc")
	public ResponseEntity<?> findPassword(@RequestBody PasswordRequestDTO dto) {
		log.info("🔎 비밀번호 찾기 요청 - username: {}", dto.getMberId());

	    Map<String, Object> response = new HashMap<>();

	    try {
	    	passwordService.findPassword(dto.getMberId());

	        response.put("code", HttpStatus.OK.value());
	        response.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (NoSuchElementException e) {
	        log.warn("⚠️ 비밀번호 찾기 실패 - 존재하지 않는 아이디: {}", dto.getMberId());
	        response.put("code", HttpStatus.NOT_FOUND.value());
	        response.put("message", "아이디 또는 비밀번호를 확인하세요.");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    } catch (IllegalArgumentException e) {
	        log.warn("⚠️ 비밀번호 찾기 실패 - 이메일 미등록 사용자: {}", dto.getMberId());
	        response.put("code", HttpStatus.BAD_REQUEST.value());
	        response.put("message", "등록한 이메일이 없습니다. 관리자에게 문의하세요.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    } catch (Exception e) {
	        log.error("❗ 비밀번호 찾기 처리 중 예외 발생", e);
	        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        response.put("message", "서버 내부 오류가 발생했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}

