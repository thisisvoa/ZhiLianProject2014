package com.zhilian.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail extends javax.mail.Authenticator {   
	//smtp.163.com 994 25 smtp.qq.com 465 25
	//"740884752@qq.com", "aaa12345616"
	private String _user = "zhilianproject2014@qq.com"; // username 
	private String _pass = "zhilianproject2014"; // password    
	private String _from = "zhilianproject2014@qq.com"; // email sent from    
	private String _to;   
	private String _port = "25"; // default smtp port     
	private String _sport = "465"; // default socketfactory port     
	private String _host = "smtp.qq.com"; // default smtp server
	private String _subject;   
	private String _body;    
	private boolean _auth = true; // smtp authentication - default on  
	private boolean _debuggable = false; // debug mode on or off - default off     
	private Multipart _multipart;     
	
	public SendMail(String to, String subject, String body) {    
		this._to = to;
		this._subject = subject;
		this._body = body;
		_multipart = new MimeMultipart();      // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.    
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");  
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");   
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");  
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");  
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");     
		CommandMap.setDefaultCommandMap(mc);  
	}    
	
	public boolean send() throws Exception { 
		Properties props = _setProperties(); 
		if(!_user.equals("") && !_pass.equals("") && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {     
			Session session = Session.getInstance(props, this);      
			MimeMessage msg = new MimeMessage(session);    
			
			msg.setFrom(new InternetAddress(_from));        
			InternetAddress addressTo = new InternetAddress(_to);   
			msg.setRecipient(MimeMessage.RecipientType.TO, addressTo);
			msg.setSubject(_subject);   
			msg.setSentDate(new Date());        // setup message body   
			BodyPart messageBodyPart = new MimeBodyPart();    
//			messageBodyPart.setText(_body);
			messageBodyPart.setContent(_body, "text/html; charset=utf-8");
			_multipart.addBodyPart(messageBodyPart, 0);        // Put parts in message   
			
			msg.setContent(_multipart);
			try {
				Transport.send(msg);// send email  
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;   
			} else {  
				return false;     
			} 
		}    
	
	 public void addAttachment(String filename) throws Exception {   
			BodyPart messageBodyPart = new MimeBodyPart();  
			DataSource source = new FileDataSource(filename);  
			messageBodyPart.setDataHandler(new DataHandler(source));  
			
			String[] zhong=filename.split("/");
			String my_filename=zhong[zhong.length-1];
			messageBodyPart.setFileName(my_filename);    
			_multipart.addBodyPart(messageBodyPart); 
		}   
	
	
		@Override   
		public PasswordAuthentication getPasswordAuthentication() { 
			return new PasswordAuthentication(_user, _pass);  
		} 
		
		private Properties _setProperties() {  
			Properties props = new Properties();  
			props.put("mail.smtp.host", _host);     
			if(_debuggable) {  
				props.put("mail.debug", "true");     
			}   
			if(_auth) {     
				props.put("mail.smtp.auth", "true");   
			}      
			props.put("mail.smtp.port", _port);   
			props.put("mail.smtp.socketFactory.port", _sport);  
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
			props.put("mail.smtp.socketFactory.fallback", "false");   
			return props;   
		}
		
	}
	

