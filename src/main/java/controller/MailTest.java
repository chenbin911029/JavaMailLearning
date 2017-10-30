package controller;

import email.util.SendEmail;

/**
 * Mail test controller
 *
 * @author chenbin at 2017/10/30 11:17
 */
public class MailTest {
    public static void main(String[] args){
        SendEmail emailService = new SendEmail();
        emailService.sendSimpleText("13790432378@163.com",
                "1724002777@qq.com",
                "JavaMail Test",
                "Hello Java Mail.\n" +
                        "I hope your goal would be achieve soon");
    }
}
