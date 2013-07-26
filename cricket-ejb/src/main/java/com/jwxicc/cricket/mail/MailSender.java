package com.jwxicc.cricket.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.Validate;

public class MailSender {

	public void sendEmail(String fromAddress, String recipientAddress, String subject,
			String mailText) throws MessagingException {
		Validate.notEmpty(recipientAddress);
		System.out.println("sendEmail request sent");

		final String mailHost = System.getenv("MAIL_HOST");
		final String mailPort = System.getenv("MAIL_PORT");
		final String username = System.getenv("MAIL_ADDRESS");
		final String password = System.getenv("MAIL_PASSWORD");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.port", mailPort);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Message message = new MimeMessage(session);
		try {
			if (fromAddress == null) {
				message.setFrom(new InternetAddress(username, "Johnnie Walker XI"));
			} else {
				message.setFrom(new InternetAddress(fromAddress));
			}

		} catch (UnsupportedEncodingException e) {
			throw new MessagingException("Unable to set FROM address", e);
		}
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
		message.setSubject(subject);
		message.setText(mailText);

		Transport.send(message);
	}

	public void sendEmail(String recipientAddress, String subject, String mailText)
			throws MessagingException {
		this.sendEmail(null, recipientAddress, subject, mailText);

	}
}
