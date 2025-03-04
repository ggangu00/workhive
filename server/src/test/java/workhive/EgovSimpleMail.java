package workhive;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.mail.MailException;

public class EgovSimpleMail {
	  public static void main(String args[])throws MailException {   
		  SimpleEmail email = new SimpleEmail();
		  // setHostName에 실제 메일서버정보
	 
		  email.setCharset("euc-kr"); // 한글 인코딩  
		  email.setSmtpPort(587);
		  email.setHostName("smtp.gmail.com"); //SMTP서버 설정
		  email.setAuthentication("kmj135792@gmail.com", "qfadnmpeqabyuxdg");
		  email.setStartTLSRequired(true);
		  try {
			email.addTo("kmj135792@gamil.com", "WorkHive"); // 수신자 추가
		} catch (EmailException e) {
			e.printStackTrace();
		}
		  try {
			email.setFrom("kmj135792@gamil", "Me"); // 보내는 사람
		} catch (EmailException e) {
			e.printStackTrace();
		}
		  email.setSubject("Test message"); // 메일 제목
		  email.setContent("simple 메일 Test입니다", "text/plain; charset=euc-kr");
		  try {
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	  } 
}
