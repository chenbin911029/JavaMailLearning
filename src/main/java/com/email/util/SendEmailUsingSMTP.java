package com.email.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * smtp服务发送邮件
 *
 * @author chenbin at 2017/11/2 10:51
 */
public class SendEmailUsingSMTP {
    public void send(){

        final MailSenderProvider mailSenderProvider = MailSenderProvider.DEFAULT;

        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enbale","true");
        props.put("mail.smtp.host",mailSenderProvider.getHost());
        props.put("mail.smtp.port",mailSenderProvider.getPort());

        Session session = Session.getInstance(props,new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        mailSenderProvider.getAccount(),
                        mailSenderProvider.getPassword());
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailSenderProvider.getAccount()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("1724002777@qq.com"));
            message.setSubject("subject");
            message.setText("test.");
            Transport.send(message);

            System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
