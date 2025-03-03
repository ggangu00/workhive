package egovframework.com.common.util;

import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 메일 발송 유틸 클래스
 * - 이가부의 EgovPropertyService에서 메일 설정 정보를 읽어와 메일을 전송한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {

    // 이가부 설정파일에서 메일 관련 설정 정보를 읽기 위한 서비스 주입
    @Resource(name = "propertiesService")
    private EgovPropertyService propService;

    /**
     * 임시 비밀번호 이메일 발송 메서드
     * - 지정된 수신자 이메일로 임시 비밀번호 안내 메일을 발송한다.
     *
     * @param to 수신자 이메일 주소
     * @param tempPassword 발급된 임시 비밀번호
     */
    public void sendTempPasswordEmail(String to, String tempPassword) {
        try {
            // 1. 메일 발송기 설정
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(propService.getString("mail.host"));        // SMTP 서버 주소
            mailSender.setPort(propService.getInt("mail.port"));            // SMTP 포트
            mailSender.setUsername(propService.getString("mail.username")); // 발신자 계정
            mailSender.setPassword(propService.getString("mail.password")); // 발신자 비밀번호

            // 2. SMTP 추가 속성 설정 (보안 관련 옵션 등)
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", propService.getBoolean("mail.smtp.auth"));                      // 인증 사용 여부
            props.put("mail.smtp.starttls.enable", propService.getBoolean("mail.smtp.starttls.enable")); // TLS 사용 여부
            props.put("mail.smtp.ssl.trust", propService.getString("mail.smtp.ssl.trust"));              // 신뢰할 수 있는 서버 주소

            // 3. 메일 내용 구성
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);                                            // 수신자 이메일 주소
            helper.setSubject("[WorkHive] 임시 비밀번호 안내");         // 메일 제목
            helper.setText(buildEmailContent(tempPassword), true);       // 메일 본문 (HTML 가능)

            // 4. 메일 발송
            mailSender.send(message);
            log.info("📧 임시 비밀번호 이메일 발송 완료 - 대상: {}", to);

        } catch (MessagingException e) {
            // 발송 실패 시 예외 로그 남기고, 런타임 예외로 변환해 던짐
            log.error("❗ 이메일 발송 실패 - 대상: {}", to, e);
            throw new RuntimeException("이메일 발송 중 오류 발생");
        }
    }

    /**
     * 이메일 본문 생성 메서드
     * - 임시 비밀번호를 포함한 안내 문구 생성
     *
     * @param tempPassword 발급된 임시 비밀번호
     * @return 이메일 본문 내용 (TEXT 형식)
     */
    private String buildEmailContent(String tempPassword) {
        return new StringBuilder()
                .append("안녕하세요, WORKHIVE 입니다.<br><br>")
                .append("요청하신 임시 비밀번호를 안내드립니다.<br><br>")
                .append("<b>임시 비밀번호:</b> ").append(tempPassword).append("<br><br>")
                .append("로그인 후 반드시 비밀번호를 변경해주세요.<br><br>")
                .append("감사합니다.")
                .toString();
    }
}

