package com.email.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

/**
 * 转发邮件
 *
 * @author chenbin at 2017/10/31 9:29
 */
public class ForwardEmail {
    public void forward(){
        Properties properties = new Properties();
        properties.put("mail.pop3.host","pop3.163.com");//服务器
        properties.setProperty("mail.store.protocol","pop3"); // 协议
        properties.put("mail.pop3.port","110");//端口
        properties.put("mail.pop3.starttls.enable","true");

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enbale","true");
        properties.put("mail.smtp.host","smtp.163.com");
        properties.put("mail.smtp.port",25);

        Session session = Session.getDefaultInstance(properties);
        try {
            // Get a Store object and connect to the current host
            Store store = session.getStore("pop3");
            store.connect("pop3.163.com",
                    "13790432378@163.com",
                    "chenbin911029");

            // Create a Folder object and open the folder
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            Message[] messages = folder.getMessages();
            if (messages.length != 0) {
                //for (int i = 0, n = messages.length; i < n; i++) {
                for (int i = 10, n = 12; i < n; i++) {
                    Message message = messages[i];
                    // Get all the information from the message
                    String from = InternetAddress.toString(message.getFrom());
                    if (from != null) {
                        System.out.println("From: " + from);
                    }
                    String replyTo = InternetAddress.toString(message.getReplyTo());
                    if (replyTo != null) {
                        System.out.println("Reply-to: " + replyTo);
                    }
                    String to = InternetAddress.toString(message
                            .getRecipients(Message.RecipientType.TO));
                    if (to != null) {
                        System.out.println("To: " + to);
                    }
                    String subject = message.getSubject();
                    if (subject != null) {
                        System.out.println("Subject: " + subject);
                    }
                    Date sent = message.getSentDate();
                    if (sent != null) {
                        System.out.println("Sent: " + sent);
                    }
                    System.out.print("Do you want to reply [y/n] : ");
                    String ans = reader.readLine();
                    if ("Y".equals(ans) || "y".equals(ans)) {
                        Message forward = new MimeMessage(session);
                        // Fill in header 要转发的收件人
                        forward.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse("1724002777@qq.com"));
                        forward.setSubject("Fwd: " + message.getSubject());
                        forward.setFrom(new InternetAddress(to));
                        // Create the message part
                        MimeBodyPart messageBodyPart = new MimeBodyPart();
                        // Create a multipart message
                        Multipart multipart = new MimeMultipart();
                        // set content
                        messageBodyPart.setContent(message, "message/rfc822");
                        // Add part to multi part
                        multipart.addBodyPart(messageBodyPart);
                        // Associate multi-part with message
                        forward.setContent(multipart);
                        forward.saveChanges();

                        // Send the message by authenticating the SMTP server
                        // Create a Transport instance and call the sendMessage
                        Transport t = session.getTransport("smtp");
                        try {
                            //connect to the smpt server using transport instance
                            //change the user and password accordingly
                            t.connect("13790432378@163.com", "chenbin911029");
                            t.sendMessage(forward, forward.getAllRecipients());
                        } finally {
                            t.close();
                        }
                        System.out.println("message forwarded successfully....");
                    }
                }
            }
            // close the store and folder objects
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
