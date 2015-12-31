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
public class FluentEmailUtils {
    //主收件人
    private String to;
    private List<String> toList = new ArrayList<String>();
    //抄送人
    private String cc;
    private List<String> ccList = new ArrayList<String>();
    //秘密抄送人
    private String bcc;
    private List<String> bccList = new ArrayList<String>();
    //邮件主题
    private String subject;
    //邮件正文
    private String content;
    //附件路径
    private List<String> attachList = new ArrayList<String>();
    //单个附件路径
    private String attach;
    //发送日期  字符串类型
    private String dateStr;
    //发送日期  Date类型
    private Date date;
    //是否删除附件（临时文件）,默认不删除
    private Boolean isDelTemp = false;
    //存放需要发送的附件名称和流
    private Map<String, InputStream> map = new HashMap<String, InputStream>();
    //发送人邮箱地址
    private static String senderEmail;
    //发送人邮箱密码
    private static String senderPwd;
    //邮箱服务器信息
    private String mailHost;
    private String mailAuth;

    /**
     *  起始节点
     * @param to
     * @return
     */
    public static FluentEmailUtils to(String to){
        return new FluentEmailUtils(to);
    }

    public static FluentEmailUtils to(List<String> toList){
        return new FluentEmailUtils(toList);
    }

    /**
     * 单个收件人
     * @param to
     */
    public FluentEmailUtils(String to){
        this.toList.add(to);
    }

    /**
     * 多个主收件人
     * @param toList
     * @return
     */
    public FluentEmailUtils(List<String> toList){
        if(this.toList.size() > 0 ){
            this.toList.addAll(toList);
        }else{
            this.toList = toList;
        }
    }

    /**
     * 单个抄送人
     * @param cc
     * @return
     */
    public FluentEmailUtils cc(String cc){
        this.ccList.add(cc);
        return this;
    }

    /**
     * 多个抄送人
     * @param ccList
     * @return
     */
    public FluentEmailUtils cc(List<String> ccList){
        if(this.ccList.size() > 0 ){
            this.ccList.addAll(ccList);
        }else{
            this.ccList = ccList;
        }
        return this;
    }

    /**
     * 单个秘密抄送人
     * @param bcc
     * @return
     */
    public FluentEmailUtils bcc(String bcc){
        this.bccList.add(bcc);
        return this;
    }

    /**
     * 多个秘密抄送人
     * @param bccList
     * @return
     */
    public FluentEmailUtils bcc(List<String> bccList){
        if(this.bccList.size() > 0 ){
            this.bccList.addAll(bccList);
        }else{
            this.bccList = bccList;
        }
        return this;
    }

    /**
     * 邮件主题
     * @param subject
     * @return
     */
    public FluentEmailUtils subject(String subject){
        this.subject = subject;
        return this;
    }

    /**
     * 邮件正文内容
     * @param content
     * @return
     */
    public FluentEmailUtils content(String content){
        this.content = content;
        return this;
    }

    /**
     * 传多个附件
     * @param attachList
     * @return
     */
    public FluentEmailUtils attach(List<String> attachList){
        this.attachList = attachList;
        return this;
    }

    /**
     * 传单个附件
     * @param attach
     * @return
     */
    public FluentEmailUtils attach(String attach){
        this.attachList.add(attach);
        return this;
    }
    /**
     * key 为文件名     value 为附件流
     * @param key
     * @param value
     * @return
     */
    public FluentEmailUtils attach(String key, InputStream value){
        map.put(key, value);
        return this;
    }


    /**
     * 设置发送日期   String类型
     * @param dateStr
     * @return
     */
    public FluentEmailUtils dateStr(String dateStr){
        this.dateStr = dateStr;
        return this;
    }

    /**
     * 设置邮件发送日期 Date类型
     * @param date
     * @return
     */
    public FluentEmailUtils date(Date date){
        this.date = date;
        return this;
    }

    public FluentEmailUtils senderEmail(String senderEmail){
        this.senderEmail = senderEmail;
        return this;
    }

    public FluentEmailUtils senderPwd(String senderPwd){
        this.senderPwd = senderPwd;
        return this;
    }

    public FluentEmailUtils mailHost(String mailHost){
        this.mailHost = mailHost;
        return this;
    }

    public FluentEmailUtils mailAuth(String mailAuth){
        this.mailAuth = mailAuth;
        return this;
    }

    public void send() throws Exception {
        if(senderEmail == null || senderEmail.equals("")){
            throw new Exception("发送人邮箱地址不能为空，请初始化发件人邮箱地址");
        }
        if(senderPwd == null || senderPwd.equals("")){
            throw new Exception("发送人邮箱密码不能为空，请初始化发件人邮箱密码");
        }
        if(mailHost == null || mailHost.equals("")){
            mailHost = "smtp.exmail.qq.com";
        }
        if(mailAuth == null || mailAuth.equals("")){
            mailAuth = "true";
        }
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailHost);
        properties.put("mail.smtp.auth", mailAuth);
        Session session = Session.getDefaultInstance(properties);
        //设置邮件信息
        Message mimeMessage = getMailMessage(session, toList==null?null:EmailListToAddress(toList),
                ccList==null?null:EmailListToAddress(ccList),
                bccList==null?null:EmailListToAddress(bccList), subject, date);

        Multipart multipart = new MimeMultipart();
        //添加正文
        if(content != null && !content.equals("")){
            BodyPart contentBodyPart = new MimeBodyPart();
            contentBodyPart.setContent(content, "text/html;charest=UTF-8");
            multipart.addBodyPart(contentBodyPart);
        }
        //添加附件，附件内容为空，则不需要添加附件
        addAttach(multipart, mimeMessage, map, attachList);

        Transport transport = session.getTransport("smtp");
        transport.connect(mailHost, senderEmail, senderPwd);
        try{
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        }catch(SendFailedException e){
            //无效地址
            Address[] inValidEmail = e.getInvalidAddresses();
            toList = returnToEmail(inValidEmail, toList);
            if(toList == null || toList.size() == 0){
                throw new Exception("主收件人的邮箱地址错误，请重新设置主收件人的邮箱地址");
            }
            ccList = returnToEmail(inValidEmail, ccList);
            bccList = returnToEmail(inValidEmail, bccList);
            send();
        }
        transport.close();
    }


    /**
     *  获取有效的邮箱地址
     * @param inValidEmail  有效的邮箱地址
     * @param emailsList      发送人类型
     * @return
     * @throws Exception
     */
    private static List<String> returnToEmail(Address[] inValidEmail, List<String> emailsList) throws Exception {
        List<String> list = new ArrayList<String>();
        List<String> addreList = new ArrayList<String>();
        for(Address address : inValidEmail){
            addreList.add(address.toString());
        }
        for(String email : emailsList){
            if(!addreList.contains(email)){
                list.add(email);
            }
        }
        return getNewList(list);
    }

    /**
     * 取出list中重复的数据
     * @param li
     * @return
     */
    public static List<String> getNewList(List<String> li){
        List<String> list = new ArrayList<String>();
        for(int i=0; i<li.size(); i++){
            String str = li.get(i);  //获取传入集合对象的每一个元素
            if(!list.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;
    }

    /**
     * 设置message的内容
     * @param session
     * @param toEmail
     * @param ccEmails
     * @param bccEmail
     * @param subject
     * @param date
     * @return
     * @throws Exception
     */
    private static Message getMailMessage(Session session, Address[] toEmail, Address[] ccEmails, Address[] bccEmail,
                                          String subject, Date date) throws Exception {
        MimeMessage mimeMessage = new MimeMessage(session);
        //设置发送人
        mimeMessage.setFrom(new InternetAddress(senderEmail));
        //设置主接受人
        if(toEmail == null || toEmail.length == 0 ){
            throw new Exception("主收件人的邮箱地址不能为空，请设置主收件人的邮箱地址");
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, toEmail);
        //设置抄送人
        if(ccEmails != null && ccEmails.length != 0){
            mimeMessage.setRecipients(Message.RecipientType.CC, ccEmails);
        }
        //设置秘密抄送人
        if(bccEmail != null && bccEmail.length != 0) {
            mimeMessage.setRecipients(Message.RecipientType.BCC, bccEmail);
        }
        //设置邮件主题
        if(!subject.equals("") && subject != null) {
            mimeMessage.setSubject(subject);
        }
        //设置邮件发送时间，如果传入的时间小于当前的时间，则发送时间为当前时间，否则为传入的时间
        if(date == null){
            mimeMessage.setSentDate(new Date());
        }else{
            Date now = new Date();
            if(now.getTime() > date.getTime()){
                mimeMessage.setSentDate(new Date());
            }else{
                mimeMessage.setSentDate(date);
            }
        }
        return mimeMessage;
    }

    /**
     *  添加附件
     * @param multipart
     * @param mimeMessage
     * @param map   key为附件名，value为附件内容
     * @param urlPath
     * @throws Exception
     */
    private static void addAttach(Multipart multipart, Message mimeMessage,
               Map<String,InputStream> map, List<String> urlPath) throws Exception{
        if(urlPath != null && urlPath.size() > 0){
            for(String path : urlPath){
                BodyPart bodyPart = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(path);
                bodyPart.setDataHandler(new DataHandler(fileDataSource));
                bodyPart.setFileName(MimeUtility.encodeWord(fileDataSource.getName(), "utf-8", null));
                multipart.addBodyPart(bodyPart);
            }
        }
        if(map != null && map.size() > 0){
            for(String fileName : map.keySet()){
                InputStream content = map.get(fileName);
                //根据文件名获取mimeType
                String mimeType = new MimetypesFileTypeMap().getContentType(fileName);
                BodyPart bodyPart = new MimeBodyPart();
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(content, mimeType);
                bodyPart.setDataHandler(new DataHandler(byteArrayDataSource));
                //设置附件名称
                if(fileName != null && !fileName.equals("")){
                    bodyPart.setFileName(MimeUtility.encodeWord(fileName, "utf-8", null));
                }
                multipart.addBodyPart(bodyPart);
            }
        }
        mimeMessage.setContent(multipart);
    }

    /**
     * List类型的邮件地址转换成Address[]
     * @param emailList
     * @return
     * @throws javax.mail.internet.AddressException
     */
    private static InternetAddress[] EmailListToAddress (List<String> emailList) throws AddressException {
        if(emailList == null) return null;
        InternetAddress[] emails = new InternetAddress[emailList.size()];
        int i = 0 ;
        for(String email : emailList){
            emails[i] = new InternetAddress(email);
            i ++ ;
        }
        return emails;
    }

    /**
     * String类型的邮件地址（用“,”隔开）转换成Address[]
     * @param emails
     * @return
     * @throws javax.mail.internet.AddressException
     */
    private static InternetAddress[] EmailStringToAddress (String emails) throws AddressException {
        if(emails.equals("") || emails == null) return null;
        int len = emails.split(",").length ;
        String[] str = emails.split(",");
        int i = 0 ;
        InternetAddress[] emailsAddre = new InternetAddress[len] ;
        for(String email : str){
            emailsAddre[i] = new InternetAddress(email) ;
            i ++ ;
        }
        return emailsAddre;
    }

}
