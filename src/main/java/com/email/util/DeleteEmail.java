package com.email.util;

import javax.mail.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 删除邮件
 *
 * @author chenbin at 2017/11/2 10:11
 */
public class DeleteEmail {
    public void delete(String pop3Host,
                       String storeType,
                       String user,
                       String password){
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "110");
            properties.put("mail.pop3.starttls.enable", "true");

            // create the POP3 store object and connect with the pop server
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3");
            store.connect(pop3Host,user,password);

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            //for (int i = 0;i < messages.length; i++) {
            for (int i = 0;i < 3; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);

                String subject = message.getSubject();
                System.out.print("Do you want to delete this message [y/n] ? ");
                String ans = reader.readLine();
                if ("Y".equals(ans) || "y".equals(ans)) {
                    // set the DELETE flag to true
                    message.setFlag(Flags.Flag.DELETED,true);
                    System.out.println("Marked DELETE for message: " + subject);
                } else if ("n".equals(ans)) {
                    break;
                }
            }
            // expunges the folder to remove messages which are marked deleted
            emailFolder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
