package egovframework.com.securing.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;  // 로그인 아이디
    private String password;  // 비밀번호
}
