package com.qi.mail;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sun.mail.util.MailSSLSocketFactory;



public class MailSendClient {
	private static Logger logger = Logger
			.getLogger(MailSendClient.class);
	private String host="";
	private String port="";
	private String username="";
	private String password="";
	private String protocol = "" ;
	private String auth = "" ;
	private String debug = "" ;
	private String fromAddress = "" ;
	private Authenticator authenticator = null;
	private Properties property = null;
	
	public MailSendClient() {
		init();
	}
	//初始化一些信息
    private void init()
    {
    	host = ConfigUtil.getConfig("mail.host",Constant.MAIL_CONFIG);
    	username = ConfigUtil.getConfig("mail.username",Constant.MAIL_CONFIG);
    	password=ConfigUtil.getConfig("mail.password",Constant.MAIL_CONFIG);
    	protocol = ConfigUtil.getConfig("mail.transport.protocol",Constant.MAIL_CONFIG);
    	fromAddress = ConfigUtil.getConfig("mail.default.from",Constant.MAIL_CONFIG);
    	port = ConfigUtil.getConfig("mail.smtp.port",Constant.MAIL_CONFIG);
    	auth = ConfigUtil.getConfig("mail.smtp.auth",Constant.MAIL_CONFIG);
    	debug = ConfigUtil.getConfig("mail.debug",Constant.MAIL_CONFIG);
    	
    	property = new Properties();
    	property.put("mail.transpost.protocol", protocol);
    	property.put("mail.smtp.host", host);
    	property.put("mail.smtp.port", port);
    	property.put("mail.smtp.auth", auth);
    	property.put("mail.debug", debug);
    	
    	MailSSLSocketFactory sf;
    	try {
    		sf = new MailSSLSocketFactory();
    		sf.setTrustAllHosts(true);
    		property.put("mail.smtp.ssl.enable", "true");
    		property.put("mail.smtp.ssl.socketFactory", sf);
    	} catch (GeneralSecurityException e) {
    		e.printStackTrace();
    	}
    	
    	authenticator = new Authenticator() {
    		@Override
    		protected PasswordAuthentication getPasswordAuthentication() {
    			return new PasswordAuthentication(username, password);
    		}
		
		};
    
    }
  
	public void sendMail(String toAddress,String theme,String content) throws Exception{
		Session session = Session.getInstance(property,authenticator);

		MimeMessage message = new MimeMessage(session); 
		//设置发信人 
		message.setFrom(new InternetAddress(fromAddress,"婚庆流程系统")); 
		//收信人 
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress)); 
		//邮件标题 
		message.setSubject(theme); 		
        //邮件时间
	    message.setSentDate(new Date());	    
	    //设置邮件的文本内容 
	    Multipart multipart = new MimeMultipart();    
	    BodyPart contentPart = new MimeBodyPart();   
	    if("".equals(content) || content == null){
    		contentPart.setText("helloword!");
    	}else{
    		contentPart.setContent(content,"text/html;charset=UTF-8");
    	}
	    multipart.addBodyPart(contentPart);
	    
	    //将multipart对象放到message中
	    message.setContent(multipart);
	    //保存邮件
	    message.saveChanges();
	    //发送邮件
	    Transport transport = session.getTransport(protocol);
	    //连接服务器的邮箱
        transport.connect(host,username,password);
        //把邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
        logger.info( "邮件发送给:" + toAddress + ";主题：" + theme + ";内容："+content +";时间："+sdf.format(date));
        transport.close(); 
	}

	public static void main(String args[]){
		MailSendClient mailsend = new MailSendClient();
		try {
			mailsend.sendMail("872741098@qq.com", "邮件测试", "这是一封带html的邮件<a href=\"http://www.baidu.com\">百度</a>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
