package com.okiimport.app.service.mail.impl;

import java.io.File;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.okiimport.app.service.mail.MailService;

public class MailServiceImpl implements MailService {

	private VelocityEngine velocityEngine;
	private JavaMailSender mailSender;
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void send(String to, String subject, String text) {
		// TODO Auto-generated method stub
		System.out.println("Send 1");
		send(to, subject, text, null, null, null);
	}
	
	public void send(String to, String subject, String text, File... attachments) {
		// TODO Auto-generated method stub
		System.out.println("Send 2");
		send(to, subject, text, null, null, attachments);
	}
	
	public void send(String to, String subject, String template,
			final Map<String, Object> model) {
		// TODO Auto-generated method stub
		System.out.println("Send 3");
		send(to, subject, null, template, model);
	}
	
	public void send(String to, String subject, String template,
			final Map<String, Object> model, File... attachments) {
		// TODO Auto-generated method stub
		System.out.println("Send 4");
		send(to, subject, null, template, model, attachments);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private void send(final String to, final String subject, final String text, 
			final String template, final Map<String, Object> model, final File... attachments){
//		System.out.println("NULO To: "+(to==null)+" "+to);
//		System.out.println("NULO Subject: "+(null==subject)+" "+subject);
//		System.out.println("NULO Text: "+(null==text)+" "+text);
//		System.out.println("NULO Template: "+(null==template)+" "+template);
//		System.out.println("NULO Model: "+(null==model));
//		System.out.println("NULO VELOC: "+(velocityEngine==null));
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
                message.setTo(new InternetAddress(to));
                message.setFrom(new InternetAddress("requerimientos.urbicars@gmail.com")); // could be parameterized...
                message.setSubject(subject);
                if(text!=null && !text.equalsIgnoreCase(""))
                	message.setText(text);
                else if(template!=null && !template.equalsIgnoreCase("")){
                	String text = VelocityEngineUtils.mergeTemplateIntoString(
                			velocityEngine, "mail_template/"+template, "UTF-8", model);
//                	System.out.println("NULO TEXT: "+text);
                	message.setText(text, true);
                }
                else
                	message.setText("SIN CUERPO");
                
                if (attachments != null) {  
					for (File attachment : attachments) {  
						System.out.println("RUTA----------------:"+attachment.getAbsolutePath());
						FileSystemResource file = new FileSystemResource(attachment);  
						//message.addInline(attachment.getName(), file);
						message.addAttachment(attachment.getName(), file);
					}  
				}  
            }
        };
        this.mailSender.send(preparator);
	}
}