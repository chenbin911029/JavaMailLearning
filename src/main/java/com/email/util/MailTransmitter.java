package com.email.util;

import com.email.support.FreeMarkerBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service("mailTransmitter")
public class MailTransmitter implements ApplicationContextAware{
    protected ApplicationContext applicationContext;

    protected final static Logger LOG = LogManager.getLogger(MailTransmitter.class);
    @Resource
    FreeMarkerBean freeMarkerBean;

    /**
     * 发送邮件
     * @param from
     * @param to
     * @param subject
     * @param msg
     */
    public void sendMail(String from,String to,String subject,String msg){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);

        JavaMailSenderImpl mailSender = getMailSender();
        mailSender.send(message);
    }

    /**
     * 以HTML格式发送邮件，若模板不为空，则使用模板。
     * @param provider 邮件服务提供商名称
     * @param from 发送方
     * @param to 收件方
     * @param subject 主题
     * @param content 邮件内容
     * @param template 邮件模板名称
     * @param params 邮件内容中的参数
     * @return
     */
    public Map<String,Object> sendSimpleHTML(
            String provider,
            String from,
            String to,
            String subject,
            String content,
            String template,
            Map<String,Object> params){
        return send(
                provider,
                from,
                new String[]{to},
                null,
                null,
                subject,
                content,
                template,
                true,
                null,
                params);
    }

    /**
     * 以普通文本格式发送邮件。
     *
     * @param provider  邮件服务提供商名称。
     * @param from      发送方。
     * @param to        收件方。
     * @param subject   主题。
     * @param content   邮件内容。
     * @see com.email.util.MailAttachment
     */
    public Map<String, Object> sendSimpleTEXT(
            String provider,
            String from,
            String to,
            String subject,
            String content) {
        return send(provider, from, new String[]{to}, null, null, subject, content);
    }

    /**
     * 发送邮件。
     * @param provider 邮件服务提供商名称
     * @param from 发送方
     * @param toES 收件方
     * @param bccES 抄送方
     * @param ccES 密送方
     * @param subject 主题
     * @param content 邮件模板名称
     * @param template 邮件内容，若此不为空，则不使用模板
     * @param htmlOr true：HTML格式；false：普通文本格式
     * @param attachments 附件
     * @param params 邮件内容参数
     * @return
     */
    public Map<String,Object> send(
            String provider,
            String from,
            String[] toES,
            String[] bccES,
            String[] ccES,
            String subject,
            String content,
            String template,
            boolean htmlOr,
            MailAttachment[] attachments,
            Map<String, Object> params){
        Map<String,Object> result = new HashMap<String,Object>();
        if (toES == null || toES.length < 1) {
            result.put("sendResult","false");
            result.put("sendReturn","收件人不能为空。");
            return result;
        }

        if (StringUtils.isEmpty(subject.trim())) {
            result.put("sendResult","false");
            result.put("sendReturn","邮件主题不能为空");
            return result;
        }
        //邮件服务器参数
        JavaMailSenderImpl mailSender = getMailSender();
        MailSenderProvider mailSenderProvider = MailSenderProvider.create(provider);
        mailSender.setHost(mailSenderProvider.getHost());
        mailSender.setPort(mailSenderProvider.getPort());
        mailSender.setUsername(mailSenderProvider.getAccount());
        mailSender.setPassword(mailSenderProvider.getPassword());

        if (from == null) {
            from = mailSenderProvider.getAccount();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage,true,"UTF-8");
            if (StringUtils.isEmpty(mailSenderProvider.getNickname())) {
                mimeMessageHelper.setFrom(from);
            } else {
                mimeMessageHelper.setFrom(
                        new InternetAddress(from,mailSenderProvider.getNickname()));
            }
            mimeMessageHelper.setTo(toES);

            if (bccES != null && bccES.length > 0) {
                mimeMessageHelper.setBcc(bccES);
            }
            if (ccES != null && ccES.length > 0) {
                mimeMessageHelper.setCc(ccES);
            }

            mimeMessageHelper.setSentDate(Calendar.getInstance().getTime());
            mimeMessageHelper.setSubject(subject);
            //附件
            if (attachments != null) {
                for (MailAttachment attachment : attachments) {
                    FileSystemResource file =  new FileSystemResource(attachment.getResource());
                    mimeMessageHelper.addAttachment(file.getFilename(),file);
                }
            }

            if (StringUtils.isEmpty(template)) {
                mimeMessageHelper.setText(content,htmlOr);
            } else {
                mimeMessageHelper.setText(freeMarkerBean.getText(template+".ftl",params),htmlOr);
            }

            //发送邮件
            mailSender.send(mimeMessage);
            result.put("sendResult","success");
            result.put("sendReturn","发送成功");
        } catch (Exception ex) {
            LOG.error(String.format("发送邮件出现异常：%s",ex.getMessage()));
            result.put("sendResult","false");
            result.put("sendReturn",String.format("发送邮件出现异常：%s",ex.getMessage()));
        }
        return result;
    }

    /**
     * 发送邮件。
     *
     * @param provider  邮件服务提供商名称。
     * @param from      发送方。
     * @param toES      收件方。
     * @param bccES     抄送方。
     * @param ccES      密送方。
     * @param subject   主题。
     * @param content   邮件内容。
     */
    public Map<String, Object> send(
            String provider,
            String from,
            String[] toES,
            String[] bccES,
            String[] ccES,
            String subject,
            String content) {

        Map<String, Object> result = new HashMap<String, Object>();
        if (toES == null || toES.length < 1) {
            result.put("sendResult", "false");
            result.put("sendReturn", "收件人不能为空。");
            return result;
        }

        if (subject == null || subject.trim().equals("")) {
            result.put("sendResult", "false");
            result.put("sendReturn", "邮件主题不能为空。");
            return result;
        }

        try {
            JavaMailSenderImpl mailSender = getMailSender();
            MailSenderProvider mailSenderProvider = MailSenderProvider.create(provider);
            mailSender.setHost(mailSenderProvider.getHost());
            mailSender.setPort(mailSenderProvider.getPort());
            mailSender.setUsername(mailSenderProvider.getAccount());
            mailSender.setPassword(mailSenderProvider.getPassword());

            if (from == null) {
                from = mailSenderProvider.getAccount();
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toES);
            message.setSubject(subject);
            message.setSentDate(Calendar.getInstance().getTime());

            if (bccES != null && bccES.length > 0) {
                message.setBcc(bccES);
            }

            if (ccES != null && ccES.length > 0) {
                message.setCc(ccES);
            }

            message.setText(content);
            mailSender.send(message);

            result.put("sendResult", "success");
            result.put("sendReturn", "success");
        } catch (Exception ex) {
            LOG.error("发送邮件出现异常：" + ex.getMessage());
            result.put("sendResult", "false");
            result.put("sendReturn", "发送邮件出现异常：" + ex.getMessage());
        }

        return result;

    }



    /**
     * 利用Spring底层beanFactory接口基于原型(prototype)配置每次
     * 获取一个新的 JavaMailSenderImpl 实例
     * @return
     */
    protected JavaMailSenderImpl getMailSender() {
        return (JavaMailSenderImpl) applicationContext.getBean("mailSender");
    }

    /**
     * {@link ApplicationContextAware#setApplicationContext(ApplicationContext)}。
     * @param applicationContext
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
}
