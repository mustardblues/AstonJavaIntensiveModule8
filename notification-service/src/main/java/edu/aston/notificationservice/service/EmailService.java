package edu.aston.notificationservice.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(final JavaMailSender mailSender, final TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMessageToEmail(final String template, final String email) {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            Context context = new Context();

            helper.setTo(email);
            helper.setText(this.templateEngine.process(template, context), true);
            mailSender.send(mimeMessage);
        }
        catch(Exception exception) {
            throw new RuntimeException("Failed to send message to email", exception);
        }
    }
}
