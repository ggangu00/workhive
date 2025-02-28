package egovframework.com.securing.service;

import lombok.Getter;
import lombok.Setter;

// 로그인 요청이 들어온 username, password 정보를 담는 DTO개념
@Getter
@Setter
public class LoginRequest {
    private String username;  // 로그인 아이디
    private String password;  // 비밀번호
}
