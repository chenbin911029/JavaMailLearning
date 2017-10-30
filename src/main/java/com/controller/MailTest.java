package com.controller;

import com.email.util.CheckingMails;
import com.email.util.SendEmail;

/**
 * Mail test controller
 *
 * @author chenbin at 2017/10/30 11:17
 */
public class MailTest {
    public static void main(String[] args){
        SendEmail emailService = new SendEmail();
//        Map<String,Object> result = emailService.sendSimpleText("13790432378@163.com",
//                "1724002777@qq.com",
//                "JavaMail Test",
//                "Hello Java Mail.\n" +
//                        "I hope your goal would be achieve soon");
//        System.out.println("result: "+result.get("result")+" return:"+result.get("return"));

//        MailAttachment[] attachments = new MailAttachment[2];
//        MailAttachment att1 = new MailAttachment();
//        att1.setContentType("text/html");
//        att1.setFileName("伴奏曲目");
//        att1.setResource("E:\\test\\files\\伴奏曲目.txt");
//
//        MailAttachment att2 = new MailAttachment();
//        att2.setFileName("1688体验金");
//        att2.setContentType("text/html");
//        att2.setResource("E:\\test\\files\\TzctgBan1018.png");
//
//        attachments[0] = att1;
//        attachments[1] = att2;
//        Map<String,Object> result2 = emailService.sendMultipartMail(
//                "13790432378@163.com",
//                "1724002777@qq.com",
//                "JavaMail with attments",
//                "Hello Java Mail.\n"
//                        +"I hope your goal would be achieve soon",
//                attachments);
//        System.out.println("result: "+result2.get("result")+" return:"+result2.get("return"));

        String host = "pop3.163.com";// change accordingly
        String protocol = "pop3";
        String username = "13790432378@163.com";// change accordingly
        String password = "chenbin911029";// change accordingly
        CheckingMails checkingMails = new CheckingMails();
        checkingMails.check(host, protocol, username, password);
    }
}
