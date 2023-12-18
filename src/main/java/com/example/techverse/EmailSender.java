package com.example.techverse;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.techverse.Repository.UserRepository;

public class EmailSender {
	
	public void emailSent(String recipientEmail,int OTP)
	{
		 
		
		 // Sender's email and password
        String senderEmail = "laxmipatil070295@gmail.com";
        String senderPassword = "thvluetwbpkkrfwg";

        // Recipient's email
       
        // SMTP server settings
        String host = "smtp.gmail.com";
        int port = 587;

        // Create properties for the mail session
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
	 

   
}

