package com.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.InputStream;
import java.util.*;

/**
 * 邮件工具类
 * Created by kevensong on 15/12/10.
 */
public class EmailUtils {
    private String to;
    private List<String> toList = new ArrayList();
    private String cc;
    private List<String> ccList = new ArrayList();
    private String bcc;
    private List<String> bccList = new ArrayList();
    private String subject;
    private String content;
    private List<String> attachList = new ArrayList();
    private String attach;
    private String dateStr;
    private Date date;
    private Map<String, InputStream> map = new HashMap();
    private static String senderEmail;
    private static String senderPwd;
    private String mailHost;
    private String mailAuth;

    public EmailUtils to(String to) {
        this.toList.add(to);
        return this;
    }

    public EmailUtils to(List<String> toList) {
        if(this.toList.size() > 0) {
            this.toList.addAll(toList);
        } else {
            this.toList = toList;
        }

        return this;
    }

    public static EmailUtils senderEmail(String senderEmail, String senderPwd) {
        return new EmailUtils(senderEmail, senderPwd);
    }

    public EmailUtils(String senderEmail, String senderPwd) {
        this.senderEmail = senderEmail;
        this.senderPwd = senderPwd;
    }

    public EmailUtils cc(String cc) {
        if(cc.equals("")) {
            return this;
        } else {
            this.ccList.add(cc);
            return this;
        }
    }

    public EmailUtils cc(List<String> ccList) {
        if(this.ccList.size() > 0) {
            this.ccList.addAll(ccList);
        } else {
            this.ccList = ccList;
        }

        return this;
    }

    public EmailUtils bcc(String bcc) {
        if(bcc.equals("")) {
            return this;
        } else {
            this.bccList.add(bcc);
            return this;
        }
    }

    public EmailUtils bcc(List<String> bccList) {
        if(this.bccList.size() > 0) {
            this.bccList.addAll(bccList);
        } else {
            this.bccList = bccList;
        }

        return this;
    }

    public EmailUtils subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailUtils content(String content) {
        this.content = content;
        return this;
    }

    public EmailUtils attach(List<String> attachList) {
        this.attachList = attachList;
        return this;
    }

    public EmailUtils attach(String attach) {
        this.attachList.add(attach);
        return this;
    }

    public EmailUtils attach(String key, InputStream value) {
        this.map.put(key, value);
        return this;
    }

    public EmailUtils date(String dateStr) {
        this.dateStr = dateStr;
        return this;
    }

    public EmailUtils date(Date date) {
        this.date = date;
        return this;
    }

    public EmailUtils mailHost(String mailHost) {
        this.mailHost = mailHost;
        return this;
    }

    public EmailUtils mailAuth(String mailAuth) {
        this.mailAuth = mailAuth;
        return this;
    }

    public void send() throws Exception {
        if(Is.empty(senderEmail)) {
            throw new Exception("发送人邮箱地址不能为空，请初始化发件人邮箱地址");
        } else if(Is.empty(senderPwd)) {
            throw new Exception("发送人邮箱密码不能为空，请初始化发件人邮箱密码");
        } else {
            if(Is.empty(this.mailHost)) {
                this.mailHost = "smtp.exmail.qq.com";
            }

            if(Is.empty(this.mailAuth)) {
                this.mailAuth = "true";
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", this.mailHost);
            properties.put("mail.smtp.auth", this.mailAuth);
            Session session = Session.getDefaultInstance(properties);
            Message mimeMessage = getMailMessage(session, this.toList == null?null:EmailListToAddress(this.toList), this.ccList == null?null:EmailListToAddress(this.ccList), this.bccList == null?null:EmailListToAddress(this.bccList), this.subject, this.date);
            MimeMultipart multipart = new MimeMultipart();
            if(this.content != null && !this.content.equals("")) {
                MimeBodyPart transport = new MimeBodyPart();
                transport.setContent(this.content, "text/html;charest=UTF-8");
                multipart.addBodyPart(transport);
            }

            addAttach(multipart, mimeMessage, this.map, this.attachList);
            Transport transport1 = session.getTransport("smtp");
            transport1.connect(this.mailHost, senderEmail, senderPwd);

            try {
                transport1.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            } catch (SendFailedException var8) {
                Address[] inValidEmail = var8.getInvalidAddresses();
                this.toList = returnToEmail(inValidEmail, this.toList);
                if(Is.empty(this.toList)) {
                    throw new Exception("主收件人的邮箱地址错误，请重新设置主收件人的邮箱地址");
                }

                this.ccList = returnToEmail(inValidEmail, this.ccList);
                this.bccList = returnToEmail(inValidEmail, this.bccList);
                this.send();
            }

            transport1.close();
        }
    }

    private static List<String> returnToEmail(Address[] inValidEmail, List<String> emailsList) throws Exception {
        ArrayList list = new ArrayList();
        ArrayList addreList = new ArrayList();
        Address[] var4 = inValidEmail;
        int email = inValidEmail.length;

        for(int var6 = 0; var6 < email; ++var6) {
            Address address = var4[var6];
            addreList.add(address.toString());
        }

        Iterator var8 = emailsList.iterator();

        while(var8.hasNext()) {
            String var9 = (String)var8.next();
            if(!addreList.contains(var9)) {
                list.add(var9);
            }
        }

        return getNewList(list);
    }

    public static List<String> getNewList(List<String> li) {
        ArrayList list = new ArrayList();

        for(int i = 0; i < li.size(); ++i) {
            String str = (String)li.get(i);
            if(!list.contains(str)) {
                list.add(str);
            }
        }

        return list;
    }

    private static Message getMailMessage(Session session, Address[] toEmail, Address[] ccEmails, Address[] bccEmail, String subject, Date date) throws Exception {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(senderEmail));
        if(Is.empty(toEmail)) {
            throw new Exception("主收件人的邮箱地址不能为空，请设置主收件人的邮箱地址");
        } else {
            mimeMessage.setRecipients(Message.RecipientType.TO, toEmail);
            if(!Is.empty(ccEmails)) {
                mimeMessage.setRecipients(Message.RecipientType.CC, ccEmails);
            }

            if(!Is.empty(bccEmail)) {
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccEmail);
            }

            if(!Is.empty(subject)) {
                mimeMessage.setSubject(subject);
            }

            if(Is.empty(date)) {
                mimeMessage.setSentDate(new Date());
            } else {
                Date now = new Date();
                if(now.getTime() > date.getTime()) {
                    mimeMessage.setSentDate(new Date());
                } else {
                    mimeMessage.setSentDate(date);
                }
            }

            return mimeMessage;
        }
    }

    private static void addAttach(Multipart multipart, Message mimeMessage, Map<String, InputStream> map, List<String> urlPath) throws Exception {
        Iterator var4;
        String fileName;
        if(!Is.empty(urlPath)) {
            var4 = urlPath.iterator();

            while(var4.hasNext()) {
                fileName = (String)var4.next();
                if(Is.empty(fileName)) {
                    break;
                }

                MimeBodyPart content = new MimeBodyPart();
                FileDataSource mimeType = new FileDataSource(fileName);
                content.setDataHandler(new DataHandler(mimeType));
                content.setFileName(MimeUtility.encodeWord(mimeType.getName(), "utf-8", (String)null));
                multipart.addBodyPart(content);
            }
        }

        MimeBodyPart bodyPart;
        if(!Is.empty(map)) {
            for(var4 = map.keySet().iterator(); var4.hasNext(); multipart.addBodyPart(bodyPart)) {
                fileName = (String)var4.next();
                if(Is.empty(fileName)) {
                    break;
                }

                InputStream content1 = (InputStream)map.get(fileName);
                String mimeType1 = (new MimetypesFileTypeMap()).getContentType(fileName);
                bodyPart = new MimeBodyPart();
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(content1, mimeType1);
                bodyPart.setDataHandler(new DataHandler(byteArrayDataSource));
                if(fileName != null && !fileName.equals("")) {
                    bodyPart.setFileName(MimeUtility.encodeWord(fileName, "utf-8", (String)null));
                }
            }
        }

        mimeMessage.setContent(multipart);
    }

    private static InternetAddress[] EmailListToAddress(List<String> emailList) throws AddressException {
        if(Is.empty(emailList)) {
            return null;
        } else {
            InternetAddress[] emails = new InternetAddress[emailList.size()];
            int i = 0;

            for(Iterator var3 = emailList.iterator(); var3.hasNext(); ++i) {
                String email = (String)var3.next();
                emails[i] = new InternetAddress(email);
            }

            return emails;
        }
    }

    private static InternetAddress[] EmailStringToAddress(String emails) throws AddressException {
        if(Is.empty(emails)) {
            return null;
        } else {
            int len = emails.split(",").length;
            String[] str = emails.split(",");
            int i = 0;
            InternetAddress[] emailsAddre = new InternetAddress[len];
            String[] var5 = str;
            int var6 = str.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String email = var5[var7];
                emailsAddre[i] = new InternetAddress(email);
                ++i;
            }

            return emailsAddre;
        }
    }

}
