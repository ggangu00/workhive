package egovframework.com.cmm;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestControllerExceptionHandler {
	
	// Java 예외
	@ExceptionHandler(NullPointerException.class) // Null값이 들어왔을 때
	public ResponseEntity<Map<String, Object>> NullPointerException(NullPointerException ex) {
		System.err.println("⚠️ 예외 발생: " + ex.getMessage()); // 로그 출력
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Java error", "Null 체크 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(IndexOutOfBoundsException	.class) // 	배열, 리스트의 범위 초과
	public ResponseEntity<Map<String, Object>> IndexOutOfBoundsException (IndexOutOfBoundsException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Java error", "	배열, 리스트의 범위 초과 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(ClassCastException.class) // 잘못된 타입 변환
	public ResponseEntity<Map<String, Object>> ClassCastException (ClassCastException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Java error", " 잘못된 타입 변환 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(NumberFormatException.class) // 문자열 → 숫자 변환 오류
	public ResponseEntity<Map<String, Object>> NumberFormatException (NumberFormatException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Java error", " 문자열 → 숫자 변환 오류 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(IllegalArgumentException.class) // 잘못된 인자 전달
	public ResponseEntity<Map<String, Object>> IllegalArgumentException (IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Java error", " 잘못된 인자 전달 : " + ex.getMessage()));
	}
	
	// 데이터베이스 예외
	@ExceptionHandler(DuplicateKeyException.class) // DB 중복 키
	public ResponseEntity<Map<String, Object>> DuplicateKeyException (DuplicateKeyException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("DB error", " 중복 키 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(DataAccessException.class) // DB 접근 오류
	public ResponseEntity<Map<String, Object>> DataAccessException (DataAccessException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("DB error", " DB 접근 오류 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class) // 결과가 없을 때
	public ResponseEntity<Map<String, Object>> EmptyResultDataAccessException (EmptyResultDataAccessException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("DB error", " 결과가 없을 때 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(TransactionSystemException.class) // 트랜잭션 관련 오류
	public ResponseEntity<Map<String, Object>> TransactionSystemException (TransactionSystemException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("DB error", " 트랜잭션 관련 오류 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class) // SQL INTO NULL
	public ResponseEntity<Map<String, Object>> SQLIntegrityConstraintViolationException (SQLIntegrityConstraintViolationException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("DB error", " Null값 오류 : " + ex.getMessage()));
	}
	
	// 인증 오류
	@ExceptionHandler(AuthenticationException.class) // 로그인 인증 실패
	public ResponseEntity<Map<String, Object>> AuthenticationException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Security error", " 로그인 인증 실패 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(AccessDeniedException.class) // 권한 없는 리소스 접근
	public ResponseEntity<Map<String, Object>> AccessDeniedException (AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Security error", " 권한 없는 리소스 접근 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(UsernameNotFoundException.class) // 	존재하지 않는 계정
	public ResponseEntity<Map<String, Object>> UsernameNotFoundException (UsernameNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Security error", " 존재하지 않는 계정 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(BadCredentialsException.class) // 비밀번호 불일치
	public ResponseEntity<Map<String, Object>> BadCredentialsException (BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("Security error", " 비밀번호 불일치 : " + ex.getMessage()));
	}
	
	
	// Spring MVC 예외
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 지원되지 않는 HTTP 메서드 사용
	public ResponseEntity<Map<String, Object>> HttpRequestMethodNotSupportedException (HttpRequestMethodNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("MVC error", " 지원되지 않는 HTTP 메서드 사용 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class) // 지원되지 않는 Content-Type
	public ResponseEntity<Map<String, Object>> HttpMediaTypeNotSupportedException (HttpMediaTypeNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("MVC error", " 지원되지 않는 Content-Type : " + ex.getMessage()));
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class) // 필수 요청 파라미터 누락
	public ResponseEntity<Map<String, Object>> MissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("MVC error", " 필수 요청 파라미터 누락 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(TypeMismatchException.class) // 요청값의 타입 불일치
	public ResponseEntity<Map<String, Object>> TypeMismatchException(TypeMismatchException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("MVC error", " 요청값의 타입 불일치 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(BindException.class) // 유효성 검사 실패
	public ResponseEntity<Map<String, Object>> BindException(BindException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("MVC error", " 유효성 검사 실패 : " + ex.getMessage()));
	}
	
	@ExceptionHandler(NotFoundException.class) // 없는 URL
	public ResponseEntity<Map<String, Object>> NotFoundException(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("MVC error", "없는 URL : " + ex.getMessage()));
	}
	
	// 나머지의 예외는 여기서 처리된다! 
	// 항상 마지막에 존재해야함 왜냐? 위에서 쭉 실행되면서 선언되어있지 않은 에러는 여기로 들어옴
	@ExceptionHandler(Exception.class) 
	public ResponseEntity<Map<String, Object>> Exception(Exception ex) {
		
		System.err.println("⚠️ 예외 발생 마지막: " + ex.getMessage()); // 로그 출력
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("에러용~~", "등록 실패 : " + ex.getMessage()));
	}

}