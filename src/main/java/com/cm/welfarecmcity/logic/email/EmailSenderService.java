package com.cm.welfarecmcity.logic.email;

//import java.io.File;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public void sendSimpleEmail(String toEmail) {
    String text = "Test Email Register";
    String subject = "REGISTER CM CITY";

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(toEmail);
    message.setText(text);
    message.setSubject(subject);
    mailSender.send(message);

    System.out.println("Mail Send...");
  }
}
