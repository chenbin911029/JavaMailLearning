package com.email.util;

import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 发送邮件
 *
 * @author chenbin at 2017/10/30 10:37
 */
public class SendEmail {

    /**
     * 发送简单文本邮件
     * @param from
     * @param to
     * @param subject
     * @param content
     * @return
     */
    public Map<String,Object> sendSimpleText(String from,
                                         String to,
                                         String subject,
                                         String content) {
        return send(from,to,subject,content,null);
    }

    /**
     * 发送邮件带有附件
     * @param from
     * @param to
     * @param subject
     * @param content
     * @param attachments
     * @return
     */
    public Map<String,Object> sendMultipartMail(String from,
                                             String to,
                                             String subject,
                                             String content,
                                             MailAttachment[] attachments) {
        return send(from,to,subject,content,attachments);
    }

    /**
     * 发送邮件
     * @param from 发件人地址
     * @param to 收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public Map<String,Object> send(String from,
                                   String to,
                                   String subject,
                                   String content,
                                   MailAttachment[] attachments){
        Map<String,Object> result = new HashMap<String,Object>();
        if (StringUtils.isEmpty(from)) {
            result.put("result","发送人不能为空。");
            result.put("return",false);
            return result;
        }
        if (StringUtils.isEmpty(to)) {
            result.put("result","收件人不能为空。");
            result.put("return",false);
            return result;
        }
        if (StringUtils.isEmpty(subject)) {
            result.put("result","邮件主题不能为空。");
            result.put("return",false);
            return result;
        }


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

            //attachments
            if (attachments != null && attachments.length > 0) {
                Multipart multipart = new MimeMultipart();
                BodyPart messageBodyPart = new MimeBodyPart();
                // Now set the actual message
                messageBodyPart.setText(content);
                // Set text message part
                multipart.addBodyPart(messageBodyPart);
                for (MailAttachment att : attachments){
                    // Create the message part
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(att.getResource());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(att.getFileName());
                    multipart.addBodyPart(messageBodyPart);
                }
                //send the complete message parts
                message.setContent(multipart);
            }

            //send message
            Transport.send(message);
            result.put("result","发送邮件 successful。");
            result.put("return",true);

        } catch (MessagingException e) {
            result.put("result","发送出错："+e.getMessage());
            result.put("return",false);
            throw new RuntimeException(e);
        }
        return result;
    }


}
