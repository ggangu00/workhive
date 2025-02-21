package egovframework.com.common;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice // json
public class GlobalRestControllerExceptionHandler {

	// Java 예외
	@ExceptionHandler(NullPointerException.class) // Null값이 들어왔을 때
	public ResponseEntity<String> NullPointerException(NullPointerException ex) throws JsonProcessingException {
		System.err.println("⚠️ 예외 발생: " + ex.getMessage()); // 로그 출력

		Map<String, Object> map = Collections.singletonMap("error", "Null 체크 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(IndexOutOfBoundsException.class) // 배열, 리스트의 범위 초과
	public ResponseEntity<String> IndexOutOfBoundsException(IndexOutOfBoundsException ex)
			throws JsonProcessingException {
		Map<String, Object> map = Collections.singletonMap("error", "	배열, 리스트의 범위 초과 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(ClassCastException.class) // 잘못된 타입 변환
	public ResponseEntity<String> ClassCastException(ClassCastException ex) throws JsonProcessingException {
		Map<String, Object> map = Collections.singletonMap("error", " 잘못된 타입 변환 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(NumberFormatException.class) // 문자열 → 숫자 변환 오류
	public ResponseEntity<String> NumberFormatException(NumberFormatException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 문자열 → 숫자 변환 오류 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(IllegalArgumentException.class) // 잘못된 인자 전달
	public ResponseEntity<String> IllegalArgumentException(IllegalArgumentException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 잘못된 인자 전달 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	// 데이터베이스 예외
	@ExceptionHandler(DuplicateKeyException.class) // DB 중복 키
	public ResponseEntity<String> DuplicateKeyException(DuplicateKeyException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 중복 키 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(EmptyResultDataAccessException.class) // 결과가 없을 때
	public ResponseEntity<String> EmptyResultDataAccessException(EmptyResultDataAccessException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 트랜잭션 관련 오류 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(TransactionSystemException.class) // 트랜잭션 관련 오류
	public ResponseEntity<String> TransactionSystemException(TransactionSystemException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 트랜잭션 관련 오류 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class) // SQL INTO NULL
	public ResponseEntity<String> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " Null값 오류 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}
	
	@ExceptionHandler(SQLSyntaxErrorException.class) // SQL Syntax Error
	public ResponseEntity<String> SQLSyntaxErrorException(SQLSyntaxErrorException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " SQL 문법 오류 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}
	
	@ExceptionHandler(DataAccessException.class) // DB 접근 오류
	public ResponseEntity<String> DataAccessException(DataAccessException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " DB 접근 오류 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	// 인증 오류
	@ExceptionHandler(AuthenticationException.class) // 로그인 인증 실패
	public ResponseEntity<String> AuthenticationException(AuthenticationException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 로그인 인증 실패 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(AccessDeniedException.class) // 권한 없는 리소스 접근
	public ResponseEntity<String> AccessDeniedException(AccessDeniedException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 권한 없는 리소스 접근 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(UsernameNotFoundException.class) // 존재하지 않는 계정
	public ResponseEntity<String> UsernameNotFoundException(UsernameNotFoundException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 존재하지 않는 계정 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(BadCredentialsException.class) // 비밀번호 불일치
	public ResponseEntity<String> BadCredentialsException(BadCredentialsException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 비밀번호 불일치 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	// Spring MVC 예외
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 지원되지 않는 HTTP 메서드 사용
	public ResponseEntity<String> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 지원되지 않는 HTTP 메서드 사용 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class) // 지원되지 않는 Content-Type
	public ResponseEntity<String> HttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 지원되지 않는 Content-Type : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class) // 필수 요청 파라미터 누락
	public ResponseEntity<String> MissingServletRequestParameterException(MissingServletRequestParameterException ex)
			throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 필수 요청 파라미터 누락 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(TypeMismatchException.class) // 요청값의 타입 불일치
	public ResponseEntity<String> TypeMismatchException(TypeMismatchException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 요청값의 타입 불일치 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(BindException.class) // 유효성 검사 실패
	public ResponseEntity<String> BindException(BindException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 유효성 검사 실패 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	@ExceptionHandler(NotFoundException.class) // 없는 URL
	public ResponseEntity<String> NotFoundException(NotFoundException ex) throws JsonProcessingException {

		Map<String, Object> map = Collections.singletonMap("error", " 없는 URL : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

	// 나머지의 예외는 여기서 처리된다!
	// 항상 마지막에 존재해야함 왜냐? 위에서 쭉 실행되면서 선언되어있지 않은 에러는 여기로 들어옴
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> Exception(Exception ex) throws JsonProcessingException {

		System.err.println("⚠️ 예외 발생 마지막 : " + ex.getMessage()); // 로그 출력
		Map<String, Object> map = Collections.singletonMap("error", "등록 실패 : " + ex.getMessage());
		ObjectMapper objMap = new ObjectMapper(); // json 형태

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(objMap.writeValueAsString(map));
	}

}