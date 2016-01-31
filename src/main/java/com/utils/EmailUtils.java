package com.utils;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.alibaba.fastjson.util.IOUtils;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailUtils {
    private Properties props;
    private String from;
    private String fromPwd;
    private Set<Address> tos = new HashSet();
    private Set<Address> ccs = new HashSet();
    private Set<Address> bccs = new HashSet();
    private String subject;
    private String body;
    private Set<String> attachmentPathList = new HashSet();
    private Map<String, InputStream> attachmentStreamMap = new HashMap();
    private Date sendDate;

    public static EmailUtils from(String from, String fromPwd) {
        return new EmailUtils(from, fromPwd);
    }

    public EmailUtils props(Properties props) {
        this.props = props;
        return this;
    }

    private EmailUtils(String from, String fromPwd) {
        this.from = from;
        this.fromPwd = fromPwd;
    }

    public EmailUtils to(String... tos) throws AddressException {
        String[] var2 = tos;
        int var3 = tos.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String to = var2[var4];
            if(!Is.empty(to)) {
                this.tos.add(new InternetAddress(to));
            }
        }

        return this;
    }

    public EmailUtils to(Collection<String> toList) throws AddressException {
        Iterator var2 = toList.iterator();

        while(var2.hasNext()) {
            String to = (String)var2.next();
            if(!Is.empty(to)) {
                this.tos.add(new InternetAddress(to));
            }
        }

        return this;
    }

    public EmailUtils cc(String... ccs) throws AddressException {
        String[] var2 = ccs;
        int var3 = ccs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String cc = var2[var4];
            if(!Is.empty(cc)) {
                this.ccs.add(new InternetAddress(cc));
            }
        }

        return this;
    }

    public EmailUtils cc(Collection<String> ccList) throws AddressException {
        Iterator var2 = ccList.iterator();

        while(var2.hasNext()) {
            String cc = (String)var2.next();
            if(!Is.empty(cc)) {
                this.ccs.add(new InternetAddress(cc));
            }
        }

        return this;
    }

    public EmailUtils bcc(String... bccs) throws AddressException {
        String[] var2 = bccs;
        int var3 = bccs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String bcc = var2[var4];
            if(!Is.empty(bcc)) {
                this.bccs.add(new InternetAddress(bcc));
            }
        }

        return this;
    }

    public EmailUtils bcc(Collection<String> bccList) throws AddressException {
        Iterator var2 = bccList.iterator();

        while(var2.hasNext()) {
            String bcc = (String)var2.next();
            if(!Is.empty(bcc)) {
                this.bccs.add(new InternetAddress(bcc));
            }
        }

        return this;
    }

    public EmailUtils subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailUtils body(String body) {
        this.body = body;
        return this;
    }

    public EmailUtils attachment(String... attachments) {
        String[] var2 = attachments;
        int var3 = attachments.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String attachment = var2[var4];
            if((new File(attachment)).exists()) {
                this.attachmentPathList.add(attachment);
            }
        }

        return this;
    }

    public EmailUtils attachment(Collection<String> attachmentList) {
        Iterator var2 = attachmentList.iterator();

        while(var2.hasNext()) {
            String attachment = (String)var2.next();
            if((new File(attachment)).exists()) {
                this.attachmentPathList.add(attachment);
            }
        }

        return this;
    }

    public EmailUtils attachment(String name, InputStream in) {
        this.attachmentStreamMap.put(name, in);
        return this;
    }

    public EmailUtils sendDate(Date sendDate) {
        this.sendDate = sendDate;
        return this;
    }

    private Session getSession() {
        return Is.empty(this.props)?Session.getDefaultInstance((Properties)null):Session.getInstance(this.props);
    }

    private MimeMessage buildMimeMessage(Session session) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(this.from));
        this.fillRecipients(mimeMessage);
        mimeMessage.setSubject(this.subject);
        mimeMessage.setSentDate(!Is.empty(this.sendDate) && (new Date()).getTime() <= this.sendDate.getTime()?this.sendDate:new Date());
        return mimeMessage;
    }

    private void fillRecipients(MimeMessage mimeMessage) throws MessagingException {
        mimeMessage.setRecipients(Message.RecipientType.TO, (Address[])this.tos.toArray(new InternetAddress[0]));
        if(!Is.empty(this.ccs)) {
            mimeMessage.setRecipients(Message.RecipientType.CC, (Address[])this.ccs.toArray(new InternetAddress[0]));
        }

        if(!Is.empty(this.bccs)) {
            mimeMessage.setRecipients(Message.RecipientType.BCC, (Address[])this.bccs.toArray(new InternetAddress[0]));
        }

    }

    private void addBody(Multipart multipart) throws MessagingException {
        if(!Is.empty(this.body)) {
            MimeBodyPart contentBodyPart = new MimeBodyPart();
            contentBodyPart.setContent(this.body, "text/html; charset=utf-8");
            multipart.addBodyPart(contentBodyPart);
        }

    }

    private void addAttachment(Multipart multipart) throws MessagingException, IOException {
        Iterator var2 = this.attachmentPathList.iterator();

        String fileName;
        while(var2.hasNext()) {
            fileName = (String)var2.next();
            if(!Is.empty(fileName)) {
                MimeBodyPart content = new MimeBodyPart();
                FileDataSource mimeType = new FileDataSource(fileName);
                content.setDataHandler(new DataHandler(mimeType));
                content.setFileName(MimeUtility.encodeWord(mimeType.getName(), "utf-8", (String)null));
                multipart.addBodyPart(content);
            }
        }

        var2 = this.attachmentStreamMap.keySet().iterator();

        while(var2.hasNext()) {
            fileName = (String)var2.next();
            if(!Is.empty(fileName)) {
                InputStream content1 = (InputStream)this.attachmentStreamMap.get(fileName);
                String mimeType1 = (new MimetypesFileTypeMap()).getContentType(fileName);
                MimeBodyPart bodyPart = new MimeBodyPart();
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(content1, mimeType1);
                bodyPart.setDataHandler(new DataHandler(byteArrayDataSource));
                bodyPart.setFileName(MimeUtility.encodeWord(fileName, "utf-8", (String)null));
                multipart.addBodyPart(bodyPart);
                IOUtils.close(content1);
            }
        }

    }

    private void removeInValid(Address[] invalidAddresses) {
        Address[] var2 = invalidAddresses;
        int var3 = invalidAddresses.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Address address = var2[var4];
            this.tos.remove(address);
            this.ccs.remove(address);
            this.bccs.remove(address);
        }

    }

    public void send() throws MessagingException, IOException {
        Session session = this.getSession();
        MimeMessage mimeMessage = this.buildMimeMessage(session);
        MimeMultipart multipart = new MimeMultipart();
        this.addBody(multipart);
        this.addAttachment(multipart);
        mimeMessage.setContent(multipart);
        Transport transport = session.getTransport();
        transport.connect(this.from, this.fromPwd);

        try {
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        } catch (SendFailedException var6) {
            this.removeInValid(var6.getInvalidAddresses());
            this.fillRecipients(mimeMessage);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        }

        transport.close();
    }

    static {
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.exmail.qq.com");
        p.put("mail.transport.protocol", "smtp");
        Session.getDefaultInstance(p);
    }

    public static void  main(String args[]) throws MessagingException, IOException {
        EmailUtils.from("发送邮箱地址","发送邮箱密码")
                .to("主收件人邮箱地址")
                .cc("抄送人邮箱地址")
                .bcc("暗送人邮箱地址")
                .body("正文内容")
                .attachment("附件,单个或多个文件地址")
                .attachment("附件名称(含文件后缀名)","输入流")
                .send();
    }
}
