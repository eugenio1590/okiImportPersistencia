package com.okiimport.app.service.mail;

import java.io.File;  
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
  
public interface MailService {

	//Config
	public void setVelocityEngine(VelocityEngine velocityEngine);
	
	public void setMailSender(JavaMailSender mailSender);
	
	//Sender
	public void send(String to, String subject, String text);  

	public void send(String to, String subject, String text, File... attachments);  

	public void send(String to, String subject, String template, Map<String, Object> model);

	public void send(String to, String subject, String template, Map<String, Object> model, 
			File... attachments);
}  