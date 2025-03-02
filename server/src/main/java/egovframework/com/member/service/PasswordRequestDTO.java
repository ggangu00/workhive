package egovframework.com.member.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequestDTO {
	private String mberId;	// 비밀번호 찾을 아이디
}
