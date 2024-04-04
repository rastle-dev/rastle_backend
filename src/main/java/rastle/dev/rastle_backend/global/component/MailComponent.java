package rastle.dev.rastle_backend.global.component;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

@Component
@RequiredArgsConstructor
public class MailComponent {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendBankIssueMessage(PaymentResponse paymentResponse) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            mimeMessage.addRecipients(Message.RecipientType.TO, paymentResponse.getBuyerEmail());
            mimeMessage.setSubject("recordy slow 가상계좌 발급 안내");
            mimeMessage.setFrom(new InternetAddress("rastle.fashion@gmail.com", "rastle_admin"));

            Context context = new Context();
            String emailContent = templateEngine.process(" vbank", context);
            mimeMessage.setText(emailContent, "utf-8", "html");

            emailSender.send(mimeMessage);

        } catch (MailException es) {
            throw new IllegalArgumentException(es.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
