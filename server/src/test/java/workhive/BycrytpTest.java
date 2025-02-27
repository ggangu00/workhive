package workhive;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BycrytpTest {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String rawPassword = "1234";
		String hashedPassword = encoder.encode(rawPassword);

		System.out.println("Hashed Password: " + hashedPassword);

		// 비밀번호 비교
		boolean isMatch = encoder.matches(rawPassword, hashedPassword);
		System.out.println("Password Match: " + isMatch);
	}
}
