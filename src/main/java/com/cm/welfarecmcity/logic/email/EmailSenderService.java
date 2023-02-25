package com.cm.welfarecmcity.logic.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public void sendSimpleEmail(String toEmail, String empCode, String idCard) {
    String text = "Username: " + empCode + "\nPassword: " + idCard;
    String subject = "REGISTER CM CITY";

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(toEmail);
    message.setText(text);
    message.setSubject(subject);
    mailSender.send(message);

    System.out.println("Mail Send...");
  }

  public void sendSimpleEmailCancel(String toEmail, String remark) {
    String text = "ปฎิเสษคำขอการสมัครสมาชิก\n" + "\nรายละเอียดการปฎิเสษ: " + remark;
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
