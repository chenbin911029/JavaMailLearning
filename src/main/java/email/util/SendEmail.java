package email.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送邮件
 *
 * @author chenbin at 2017/10/30 10:37
 */
public class SendEmail {
    /**
     * 发送简单文本邮件
     * @param from 发件人地址
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleText(String from, String to, String subject, String content){
        final MailSenderProvider mailSenderProvider = MailSenderProvider.DEFAULT;

        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enbale","true");
        props.put("mail.smtp.host",mailSenderProvider.getHost());
        props.put("mail.smtp.port",mailSenderProvider.getPort());

        //get the session object
        Session session = Session.getInstance(props,new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        mailSenderProvider.getAccount(),
                        mailSenderProvider.getPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("sent message successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
