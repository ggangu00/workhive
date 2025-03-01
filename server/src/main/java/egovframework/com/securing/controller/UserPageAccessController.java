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

@RestController
@RequestMapping("/access")
@Slf4j
public class UserPageAccessController {

    @Resource 
    AuthorityService authService;

    @Resource 
    MenuInfoService menuService;

    @GetMapping("/{menuCd}")
    public ResponseEntity<?> menuAccessCheck(@PathVariable("menuCd") String menuCd) {

        log.info(">>> [menuAccessCheck 호출됨] 메뉴코드: {}", menuCd);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerUser user = (CustomerUser) auth.getPrincipal();
        String userId = user.getUserDTO().getMberId();
        String deptCd = user.getUserDTO().getDeptCd();

        log.info(">>> SecurityContext 인증 객체: {}", auth);
        log.info(">>> 인증 객체 타입: {}", (auth != null ? auth.getClass().getName() : "null"));
        log.info(">>> Principal: {}", (auth != null ? auth.getPrincipal() : "null"));

        if (auth == null || !(auth.getPrincipal() instanceof CustomerUser)) {
            log.warn("인증 정보가 없거나 올바르지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        log.info("메뉴 권한 체크 호출 - menuCd={}, userId={}", menuCd, userId);

        boolean isAccess = authService.isMenuAccessible(menuCd, userId);
        log.info("메뉴 권한 체크 결과 = {}", isAccess);

        if (!isAccess) {
            log.warn(">>> 접근 거부됨: 메뉴={}, 사용자={}", menuCd, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 메뉴에 대한 접근 권한이 없습니다.");
        } else {
            log.info(">>> 접근 허용됨: 메뉴={}, 사용자={}", menuCd, userId);
            return ResponseEntity.ok(true);
        }
    }


}

