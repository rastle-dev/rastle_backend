package rastle.dev.rastle_backend.domain.member.application;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCertificationService {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    public static final long EMAIL_CERTIFICATION_TIME = 1000 * 60 * 5; // 하루
    // 인증 번호
    private String ePw;

    /**
     * 이메일 인증 메시지 발송
     *
     * @param to 수신자 이메일
     * @throws Exception 발송 실패 예외
     */
    public void sendConfirmMessage(String to) throws Exception {
        ePw = RandomStringUtils.randomNumeric(6);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            mimeMessage.addRecipients(Message.RecipientType.TO, to);
            mimeMessage.setSubject("recordy slow 이메일 인증");
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
    }

    /**
     * 이메일 인증 번호 확인
     *
     * @param email 이메일
     * @param code  인증 번호
     * @return 인증 번호 일치 여부
     */
    public boolean checkEmailCertification(String email, String code) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(email);
        log.info(value);
        log.info(code);
        if (value == null) {
            return false;
        }
        if (value.equals(code)) {
            return true;
        }
        return false;
    }

    /**
     * 비밀번호 초기화 메일 발송
     *
     * @param to
     * @return
     * @throws Exception
     */
    @Transactional
    public String sendPasswordResetMessage(String to) throws Exception {
        // Optional<Member> memberOptional = memberRepository.findByEmail(to)
        // .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        // Member member = memberOptional.orElseThrow(() -> new
        // UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // String temporaryPassword = RandomStringUtils.randomAlphanumeric(12);
        // String encodedPassword = passwordEncoder.encode(temporaryPassword);

        // member.updatePassword(encodedPassword);
        Optional<Long> memberIdOptional = memberRepository.findUserIdByEmail(to);
        Long memberId = memberIdOptional.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        String temporaryPassword = RandomStringUtils.randomAlphanumeric(12);

        memberService.changePassword(memberId, temporaryPassword);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            mimeMessage.addRecipients(Message.RecipientType.TO, to);
            mimeMessage.setSubject("recordy slow 비밀번호 초기화");
            mimeMessage.setFrom(new InternetAddress("rastle.fashion@gmail.com",
                    "rastle_admin"));

            Context context = new Context();
            context.setVariable("password", temporaryPassword);
            String emailContent = templateEngine.process("password", context);
            mimeMessage.setText(emailContent, "utf-8", "html");

            emailSender.send(mimeMessage);
            log.info("new Password" + temporaryPassword);

        } catch (MailException es) {
            throw new IllegalArgumentException(es.getMessage());
        }

        return temporaryPassword;
    }

}