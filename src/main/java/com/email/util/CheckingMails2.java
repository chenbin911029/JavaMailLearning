package com.email.util;

import javax.mail.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by chenbin on 2017\10\30 0030.
 */
public class CheckingMails2 {
    /**
     * 查询邮件
     * @param host
     * @param protocol
     * @param user
     * @param password
     */
    public void check(String host,
                      String protocol,
                      final String user,
                      final String password){
        try {
            //create properties field
            Properties properties = new Properties();
            properties.put("mail.pop3.host",host);//服务器
            properties.setProperty("mail.store.protocol",protocol); // 协议
            properties.put("mail.pop3.port","110");//端口
            properties.put("mail.pop3.starttls.enable","true");

            //setup authentication,get session
            Session emailSession = Session.getInstance(properties,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    user,
                                    password
                            );
                        }
                    });
            Store store = emailSession.getStore("pop3");
            store.connect();

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            if (messages.length > 0) {
                for (int i=0;i < 10;i++) {
                    Message message = messages[i];
                    System.out.println("---------------------------------");
                    System.out.println("Email Number " + (i + 1));
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println("From: " + message.getFrom()[0]);
                    System.out.println("Text: " + message.getContent().toString());
                }
            }
            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch(NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
