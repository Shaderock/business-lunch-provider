package com.shaderock.lunch.backend.communication.mail.service;

import static jakarta.mail.Message.RecipientType.TO;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

  private final JavaMailSender mailSender;
  @Value(value = "${spring.mail.username}")
  private String fromAccount;
  @Value(value = "${lunch.backend.url}")
  private String backendUrl;

  public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();

    message.setFrom(new InternetAddress(fromAccount));
    message.setRecipients(TO, to);
    message.setSubject(subject);

    message.setContent(htmlContent, "text/html; charset=utf-8");
    LOGGER.info("Sending [{}]", message);
    mailSender.send(message);
  }

  public void sendConfirmationEmail(AppUserDetails userDetails) throws MessagingException {
    LOGGER.info("Preparing confirmation email for [{}]", userDetails);
    String subject = "Please verify your registration";
    String content =
        "Dear " + userDetails.getFirstName() + " " + userDetails.getLastName() + ",<br>" +
            "Please click the link below to verify your registration:<br>" +
            "<h3><a href=\"" + backendUrl + "/api/register/confirm-email?token="
            + userDetails.getRegistrationToken() +
            "\" target=\"_self\">VERIFY</a></h3>" +
            "Thank you,<br>" +
            "BLP Team.";
    sendEmail(userDetails.getEmail(), subject, content);
  }
}
