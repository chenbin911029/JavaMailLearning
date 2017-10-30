package com.email.util;

import javax.mail.*;
import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * 查询电子邮件
 *
 * @author chenbin at 2017/10/30 15:21
 */
public class CheckingMails {
    /**
     * 查询邮件
     * @param host
     * @param protocol
     * @param user
     * @param password
     */
   public void check(String host,
                     String protocol,
                     String user,
                     String password){
       try {
           //create properties field
           Properties properties = new Properties();
           properties.put("mail.pop3.host",host);//服务器
           properties.setProperty("mail.store.protocol",protocol); // 协议
           properties.put("mail.pop3.port","110");//端口
           properties.put("mail.pop3.starttls.enable","true");
           Session emailSession = Session.getDefaultInstance(properties);

           //create the POP3 store object and connect with the pop server
           Store store = emailSession.getStore("pop3");
           store.connect(host,user,password);

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
                   //writePart(message);

//                   String line = reader.readLine();
//                   if ("YES".equals(line)) {
//                       message.writeTo(System.out);
//                   } else if ("QUIT".equals(line)) {
//                       break;
//                   }

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

    /**
     * This method checks for content-type
     * based on which, it processes and
     * fetches the content of the message
     * @param p
     * @throws Exception
     */
   public void writePart(Part p) throws Exception{
       if (p instanceof Message) {
           writeEnvelope((Message) p);
       }

       System.out.println("Content-type: "+p.getContentType());
       //check if the content is plain text
       if (p.isMimeType("text/plain")) {
           System.out.println("This is plain text");
           System.out.println("---------------------------");
           System.out.println((String)p.getContent());
       } else if (p.isMimeType("multipart/*")){
           //check if the content has attachment
           System.out.println("This is a Multipart");
           System.out.println("---------------------------");
           Multipart mp = (Multipart) p.getContent();
           int count = mp.getCount();
           for (int i=0;i<count;i++){
               writePart(mp.getBodyPart(i));
           }
       } else if (p.isMimeType("message/rfc822")){
           //check if the content is a nested message
           System.out.println("This is a Nested Message");
           System.out.println("---------------------------");
           writePart((Part)p.getContent());
       } else if (p.isMimeType("image/jpeg")){
           //check if the content is an inline image
           System.out.println("--------> image/jpeg");
           Object o = p.getContent();
           InputStream x = (InputStream) o;
           //Construct the required byte array
           System.out.println("x.length="+x.available());
           int i = 0;
           byte[] bArray = new byte[x.available()];
           while ((i=(int)((InputStream)x).read(bArray)) > 0) {
               int result = (int)(((InputStream)x).read(bArray));
               if (result == -1) {
                   break;
               }
           }
           FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
           f2.write(bArray);
       }  else if (p.getContentType().contains("image/")) {
           System.out.println("content type" + p.getContentType());
           File f = new File("image" + new Date().getTime() + ".jpg");
           DataOutputStream output = new DataOutputStream(
                   new BufferedOutputStream(new FileOutputStream(f)));
           com.sun.mail.util.BASE64DecoderStream test =
                   (com.sun.mail.util.BASE64DecoderStream) p
                           .getContent();
           byte[] buffer = new byte[1024];
           int bytesRead;
           while ((bytesRead=test.read(buffer))!= -1){
               output.write(buffer,0,bytesRead);
           }
       } else {
          Object o = p.getContent();
          if (o instanceof String) {
              System.out.println("This is a string");
              System.out.println("---------------------------");
              System.out.println((String) o);
          } else if (o instanceof InputStream) {
              System.out.println("This is just an input stream");
              System.out.println("---------------------------");
              InputStream is = (InputStream) o;
              is = (InputStream) o;
              int c;
              while ((c = is.read()) != -1) {
                  System.out.write(c);
              }
          } else {
              System.out.println("This is an unknown type");
              System.out.println("---------------------------");
              System.out.println(o.toString());
          }
       }

   }

    /**
     * This method would print FROM,TO and SUBJECT of the message
     * @param m
     * @throws Exception
     */
   public void writeEnvelope(Message m) throws Exception {
       Address[] a;
       if ((a=m.getFrom()) != null){
           for (int j=0;j<a.length;j++) {
               System.out.println("From: "+a[j].toString());
           }
       }

       if ((a=m.getRecipients(Message.RecipientType.TO)) != null) {
           for (int j=0;j<a.length;j++) {
               System.out.println("To: "+a[j].toString());
           }
       }

       if ((m.getSubject()) != null) {
           System.out.println("subject: "+m.getSubject());
       }
   }
}
