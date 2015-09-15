package com.okiimport.persistencia.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

public class ConfigMail {
	
	private String user;
	
	private String password;

	public ConfigMail(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	public VelocityEngine getVelocityEngine(){
		VelocityEngine velocityEngine = null;
		VelocityEngineFactoryBean velocityEngineFactory = new VelocityEngineFactoryBean();
		velocityEngineFactory.setVelocityPropertiesMap(getVelocityProperties());
		try {
			velocityEngine = velocityEngineFactory.createVelocityEngine();
		} catch (VelocityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return velocityEngine;
	}
	
	private Map<String, Object> getVelocityProperties(){
		Map<String, Object> velocityProperties = new HashMap<String, Object>();
		velocityProperties.put("resource.loader", "class");
		velocityProperties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		return velocityProperties;
	}

	public JavaMailSenderImpl getMailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setProtocol("smtp");
		mailSender.setUsername(user);
		mailSender.setPassword(password);
		mailSender.setJavaMailProperties(getJavaMailProperties());
		return mailSender;
	}
	
	private Properties getJavaMailProperties(){
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.host", "smtp.gmail.com");
		javaMailProperties.put("mail.smtp.user", this.user);
		javaMailProperties.put("mail.smtp.password", this.password);
		javaMailProperties.put("mail.smtp.port", "587");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.smtp.quitwait", "false");
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.debug", "true");
		return javaMailProperties;
	}
}
