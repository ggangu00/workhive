package egovframework.com.securing.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.authority.service.AuthorityService;
import egovframework.com.menu.service.MenuInfoService;
import egovframework.com.securing.service.UserDTO;
import lombok.extern.slf4j.Slf4j;

@RestController // data
@RequestMapping("/access")
@Slf4j
public class UserPageAccessController {
	@Resource AuthorityService authService;
	@Resource MenuInfoService menuService;

	@GetMapping("/{menuCd}")
	public ResponseEntity<?> menuAccessCheck(@PathVariable("menuCd") String menuCd, HttpSession session) {
		log.info("메뉴 코드 => {}", menuCd);

		// ✅ 세션에서 사용자 정보 가져오기
		UserDTO user = (UserDTO) session.getAttribute("loginUser");
		if (user == null) {
			log.warn("미인증 사용자!!");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}

		String userId = user.getMberId();
		log.info("로그인 사용자 => {}", userId);

		boolean isAccess = authService.isMenuAccessible(menuCd, userId);
		log.info("접근 권한 가능 ? => {}", isAccess);

		if (!isAccess) {
			log.warn("접근 권한 없음: 메뉴={}, 사용자={}", menuCd, userId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 메뉴에 대한 접근 권한이 없습니다.");
		}

		return ResponseEntity.ok(true); // 접근 가능
	}
}
