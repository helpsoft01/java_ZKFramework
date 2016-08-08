package com.vietek.taxioperation.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;

public class EmailUtils {
	
	public static void sendMail(String toEmail, String title, String content) {
		final String username = ConfigUtil.getValueConfig("EMAIL_SUPPORT", CommonDefine.EMAIL_SUPPORT);
		final String password = ConfigUtil.getValueConfig("EMAIL_SUPPORT_PASS", CommonDefine.EMAIL_SUPPORT_PASS);
		final String serverHost = ConfigUtil.getValueConfig("EMAIL_SERVER_HOST", CommonDefine.EMAIL_SERVER_HOST);
		final String portMail = ConfigUtil.getValueConfig("EMAIL_SERVER_PORT", CommonDefine.EMAIL_SERVER_PORT);

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", serverHost);
		props.put("mail.smtp.port", portMail);

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toEmail));
			message.setSubject(title);
			message.setText(content);

			Transport.send(message);

		} catch (MessagingException e) {
			AppLogger.logDebug.error("", e);
		}
	}

	public static void main(String[] args) {
		sendMail("tuanba0412@gmail.com","Test mail","aaaaa");
	}
}