package com.email.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

/**
 * Created by chenbin on 2017\10\30 0030.
 */
public class ReplyToEmail {
    public void replay(){
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
           Store store = session.getStore("pop3");
           store.connect(
                   "pop3.163.com",
                   "13790432378@163.com",
                   "chenbin911029");
            Folder folder = store.getFolder("inbox");
            if (!folder.exists()) {
                System.out.println("inbox not found");
                System.exit(0);
            }
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));

            if (messages.length != 0) {

                for (int i = 0, n = messages.length; i < n; i++) {
                    Message message = messages[i];
                    Date date = message.getSentDate();
                    // Get all the information from the message
                    String from = InternetAddress.toString(message.getFrom());
                    if (from != null) {
                        System.out.println("From: " + from);
                    }
                    String replyTo = InternetAddress.toString(message
                            .getReplyTo());
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

                        Message replyMessage = new MimeMessage(session);
                        replyMessage = (MimeMessage) message.reply(false);
                        replyMessage.setFrom(new InternetAddress(to));
                        replyMessage.setText("Java Mail Replay test.\n" +
                                "Wish you a happiness day.\n" +
                                "Thanks.");
                        replyMessage.setReplyTo(message.getReplyTo());

                        // Send the message by authenticating the SMTP server
                        // Create a Transport instance and call the sendMessage
                        Transport t = session.getTransport("smtp");
                        try {
                            //connect to the smpt server using transport instance
                            //change the user and password accordingly
                            t.connect("13790432378@163.com",
                                    "chenbin911029");
                            t.sendMessage(replyMessage,
                                    replyMessage.getAllRecipients());
                        } finally {
                            t.close();
                        }
                        System.out.println("message replied successfully ....");

                        // close the store and folder objects
                        folder.close(false);
                        store.close();

                    } else if ("n".equals(ans)) {
                        break;
                    }
                }//end of for loop

            } else {
                System.out.println("There is no msg....");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
