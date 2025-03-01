package egovframework.com.securing.service;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자 추가 (Spring이 객체 생성할 때 필요)
@AllArgsConstructor // 모든 필드를 초기화하는 생성자
public class LoginRequest {

	// 로그인 요청이 들어온 username, password 정보를 담는 DTO개념
	private String username; // 로그인 아이디
	private String password; // 비밀번호
}
