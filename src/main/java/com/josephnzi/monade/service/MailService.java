package com.josephnzi.monade.service;

import com.josephnzi.monade.exception.EmailException;
import com.josephnzi.monade.model.NotificationEmail;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailBuilder mailBuilder;
    @Async
   void sendEmail(NotificationEmail notificationEmail) throws EmailException {
        MimeMessagePreparator messagePreparator = mimeMessage ->{
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("verify@monade.com");
                messageHelper.setTo(notificationEmail.getRecipient());
                messageHelper.setSubject(notificationEmail.getSubject());
                messageHelper.setText(mailBuilder.build(notificationEmail.getBody()));
    };
    try {
        mailSender.send(messagePreparator);
        log.info("activation email send");
    }catch (MailException e){
        throw new EmailException("Error occured when sending email to "+notificationEmail.getRecipient());
    }
    }
}
