package com.email.util;

import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

/**
 * 邮箱限额
 *
 * @author chenbin at 2017/11/2 11:48
 */
public class QuotaExample {
    public  void quota(){
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.port", "143");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            Store store = emailSession.getStore("imap");
            //change the user and password accordingly
            store.connect("imap.163.com",
                    "13790432378@163.com",
                    "chenbin911029");
            IMAPStore imapStore = (IMAPStore) store;
            System.out.println("imapStore ---" + imapStore);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            if (messages.length != 0) {
                //for (int i = 0, n = messages.length; i < n; i++) {
                for (int i = 0, n = 12; i < n; i++) {
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
                }
            }
            //get quota
//            Quota[] quotas = imapStore.getQuota("INBOX");
//            //Iterate through the Quotas
//            for (Quota quota : quotas) {
//                System.out.println(String.format("quotaRoot:'%s'",quota.quotaRoot));
//                for (Quota.Resource resource : quota.resources){
//                    System.out.println(String.format("name:'%s', limit:'%s',usage:'%s'",
//                            resource.name,resource.limit,resource.usage));
//                }
//            }

        }  catch (NoSuchProviderException e1) {
            e1.printStackTrace();
        } catch (Exception e)  {
            e.printStackTrace();
        }
    }

}
