package com.okiimport.persistencia.mail;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
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
		VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();
		velocityEngine.setVelocityProperties(getVelocityProperties());
		return velocityEngine.getObject();
	}
	
	private Properties getVelocityProperties(){
		Properties velocityProperties = new Properties();
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
		javaMailProperties.put("mail.smtp.port", 587);
		javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.quitwait", false);
		javaMailProperties.put("mail.smtp.starttls.enable", true);
		javaMailProperties.put("mail.debug", true);
		return javaMailProperties;
	}
}
