package rastle.dev.rastle_backend.domain.Member.application;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCertificationService {
    private final JavaMailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;

    public static final long EMAIL_CERTIFICATION_TIME = 1000 * 60 * 5; // 하루
    // 인증 번호
    private String ePw;

    /**
     * 이메일 인증 메시지 생성
     * 
     * @param to 수신자 이메일
     * @return 이메일 인증 메시지
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public MimeMessage createMessage(String to) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.addRecipients(Message.RecipientType.TO, to);
        mimeMessage.setSubject("rastle_ 이메일 인증");

        String msg = "";
        msg += """
                <meta charset='UTF-8'>
                <table align='center' border='0' cellpadding='0' cellspacing='0' width='100%'
                       style='padding:60px 0 60px 0;color:#555;font-size:16px;word-break:keep-all;'>
                    <tbody>
                    <tr>
                        <td style='padding:0 0 0 0; border-top:none; border-bottom:none; border-left:none; border-right:none;'>
                            <table align='center' border='0' cellpadding='0' cellspacing='0'
                                   style='padding:0px 0px 0px 0px; width:100%; max-width:800px; margin:0 auto; background:#fff;'>
                                <tbody>
                                <tr>
                                    <td style='border:10px solid #f2f2f2; padding:90px 14px 90px 14px; margin-left:auto; margin-right:auto;'>
                                        <table align='center' border='0' cellpadding='0' cellspacing='0'
                                               style='width:100%; max-width:630px; margin-left:auto; margin-right:auto; letter-spacing:-1px;'>
                                            <tbody>
                                            <tr>
                                                <td style='font-size:44px;line-height:48px;font-weight:bold;color:#000000;padding-bottom:60px;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <span>rastle_ 이메일 인증 코드</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='padding-bottom:50px;font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <p style='margin:0;padding:0;'>rastle_ 회원 가입 과정 중, 사용자 인증을 위해
                                                        발송된 이메일입니다. </p>
                                                    <p style='margin:0;padding:0;'>인증 과정을 완료하기 위해 아래 인증 코드를 입력해주세요.</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='padding-bottom:50px;'>
                                                    <table align='center' border='0' cellpadding='0' cellspacing='0'
                                                           style='width:100%;'>
                                                        <tbody>
                                                        <tr>
                                                            <td style='padding:50px 30px;border:1px solid #eeeeee;background:#fbfbfb;text-align:center;'>
                                                                <p style='margin:0;padding:0;font-size:18px;font-weight:bold;color:#000000;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>""";
        msg += ePw;
        msg += """
                </p>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style='font-size:14px;line-height:22px;font-weight:normal;color:#000000;text-align:left;letter-spacing:-1px;font-family:나눔고딕, NanumGothic, 맑은고딕, Malgun Gothic, 돋움, Dotum, Helvetica, Apple SD Gothic Neo, Sans-serif;'>
                                                    <p style='margin:0;padding:0;'>감사합니다.<br>rastle_</p>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>""";
        mimeMessage.setText(msg, "utf-8", "html");
        mimeMessage.setFrom(new InternetAddress("rastle.fashion@gmail.com", "rastle_admin"));
        return mimeMessage;
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

    /**
     * 이메일 인증 메시지 발송
     * 
     * @param to 수신자 이메일
     * @throws Exception 발송 실패 예외
     */
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey();
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
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

}