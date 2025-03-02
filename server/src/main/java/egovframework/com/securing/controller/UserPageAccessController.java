package egovframework.com.securing.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.authority.service.AuthorityService;
import egovframework.com.menu.service.MenuInfoService;
import egovframework.com.securing.service.CustomerUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 메뉴 접근 권한을 체크하는 컨트롤러
 */
@RestController
@RequestMapping("/access")
@Slf4j
public class UserPageAccessController {

	@Resource
	AuthorityService authService; // 권한 관련 서비스 의존성 주입
	@Resource
	MenuInfoService menuService; // 메뉴 정보 관련 서비스 의존성 주입

	/**
	 * 특정 메뉴에 대한 사용자 접근 권한 체크
	 * 
	 * @param menuCd 메뉴코드 (URL 경로 변수로 전달받음)
	 * @return 접근 가능 여부에 따른 응답 반환
	 */
	@GetMapping("/{menuCd}")
	public ResponseEntity<?> menuAccessCheck(@PathVariable("menuCd") String menuCd) {
		log.info(">>> [menuAccessCheck 호출됨] 메뉴코드: {}", menuCd);

		// 현재 인증 정보 조회
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 인증 정보가 없거나, 인증 주체가 기대한 타입이 아니면 로그인 필요 응답 반환
		if (auth == null || !(auth.getPrincipal() instanceof CustomerUser)) {
			log.warn("인증 정보가 없거나 올바르지 않습니다.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}

		// 사용자 정보 추출
		CustomerUser user = (CustomerUser) auth.getPrincipal();
		String userId = user.getUserDTO().getMberId();

		// 메뉴 접근 권한 체크
		boolean isAccess = authService.isMenuAccessible(menuCd, userId);
		log.info("메뉴 권한 체크 결과 = {}", isAccess);

		// 권한이 없으면 접근 거부 응답 반환
		if (!isAccess) {
			log.warn(">>> 접근 거부됨: 메뉴={}, 사용자={}", menuCd, userId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 메뉴에 대한 접근 권한이 없습니다.");
		}

		// 권한이 있으면 접근 허용 응답 반환
		log.info(">>> 접근 허용됨: 메뉴={}, 사용자={}", menuCd, userId);
		return ResponseEntity.ok(true);
	}
}
