package rastle.dev.rastle_backend.domain.Member.application;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCertificationService {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String, String> redisTemplate;

    public static final long EMAIL_CERTIFICATION_TIME = 1000 * 60 * 5; // 하루
    // 인증 번호
    private String ePw;

    /**
     * 이메일 인증 메시지 발송
     * 
     * @param to 수신자 이메일
     * @throws Exception 발송 실패 예외
     */
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey();
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            mimeMessage.addRecipients(Message.RecipientType.TO, to);
            mimeMessage.setSubject("rastle_ 이메일 인증");
            mimeMessage.setFrom(new InternetAddress("rastle.fashion@gmail.com", "rastle_admin"));

            Context context = new Context();
            context.setVariable("ePw", ePw);
            String emailContent = templateEngine.process("confirm", context);
            mimeMessage.setText(emailContent, "utf-8", "html");

            emailSender.send(mimeMessage);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(to, ePw);
            redisTemplate.expire(to, EMAIL_CERTIFICATION_TIME, TimeUnit.MILLISECONDS);
        } catch (MailException es) {
            throw new IllegalArgumentException(es.getMessage());
        }

        return ePw;
    }

    /**
     * 이메일 인증 번호 확인
     * 
     * @param email 이메일
     * @param ePw   인증 번호
     * @return 인증 번호 일치 여부
     */
    public boolean checkEmailCertification(String email, String ePw) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(email);
        log.info(value);
        if (value == null) {
            return false;
        }
        if (value.equals(ePw)) {
            return true;
        }
        return false;
    }

    /**
     * 이메일 인증 번호 생성
     * 
     * @return 인증번호
     */
    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((random.nextInt(10)));
        }

        return key.toString();
    }

}