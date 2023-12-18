package com.example.techverse.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	 
	 private String senderEmail="laxmipatil070295@gmail.com";;
	
 
     private String senderPassword= "thvluetwbpkkrfwg";;
	
	 
     private String host= "smtp.gmail.com";;
	
	 
     int port=587;
	
	public void sendEmail(String recipientEmail,int OTP)
	{
		Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create a mail session with the specified properties
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Techverse Email Verification ");
            message.setText("Welcome to the System and This is verfication OTP ."+OTP);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
	
	public void sendEmail(String recipientEmail,String emailSubject, String emailBody)
	{
		Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create a mail session with the specified properties
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(emailSubject);
            message.setContent(emailBody, "text/html");
            // message.setContent();

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
	 

}
