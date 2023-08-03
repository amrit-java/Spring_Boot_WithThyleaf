package com.smart.service;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailService {
	public boolean sendEmail(String subject,String message,String to) {
		
		//rest of code...
		boolean f= false;
		String from = "ambh0007@gmail.com";
		
		//variable for gmail
		String host = "smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES"+properties);
		
		//setting important infomation to properties object
		
		//host set
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", true);
				properties.put("mail.smtp.auth", true);
		
		//step 1: to get the session object..
		Session session = Session.getInstance(properties,new Authenticator() {
			
		@Override
			protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("ambh0007@gmail.com","xponltpdzjimburi"); 
			
			}
		
		});
		session.setDebug(true);
		

		MimeMessage mimeMessage = new MimeMessage(session);
		
		try {
			
		//from email
		mimeMessage.setFrom(from);
		
		//adding recipient
		
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//adding sub to msg
		
		mimeMessage.setSubject(subject);
		
		//adding text to message
		
		//mimeMessage.setText(message);
		mimeMessage.setContent(message, "text/html");
		//send
		
		
		//step 3. send the msg using transport class
		
		Transport.send(mimeMessage);
		
		System.out.println("##################Sent Successfully#################");
		
		f=true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
