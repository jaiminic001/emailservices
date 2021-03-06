package com.pixels.texchange.email.emailservices;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.plugin2.message.Message;
import sun.rmi.transport.Transport;
import javax.mail.internet.Addressexception;
import javax.mail.internet.InternetAddress;
import javax.mail.Session;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.Properties;


@SpringBootApplication
@RestController
public class EmailservicesApplication {

	@Value("${username}")
	private String username;
	@Value("${password}")
	private String password;
	public static void main(String[] args) {
		SpringApplication.run(EmailservicesApplication.class, args);
	}


	@RequestMapping(value="/send",method=RequestMethod.POST)
	public String sendEmail(@RequestBody EmailMessage emailmessage) throws AddressException, MessagingException, IOException {
		sendmail(emailmessage);
		return "Email sent successfully";
	}

	private void sendmail(EmailMessage emailmessage) throws AddressException, MessagingException,IOException{

		Properties props = new Properties();
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.starttis.enable","true");
		props.put("mail.smtp.host","smtp.gmail.com");
		props.put("mail.smtp.port","587");

		Session session = Session.getInstance(props,new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(username,password);
			}
		});

		Message msg = new MIMEMessage(session);
		msg.setFrom(new InternetAddress(username,false));
		msg.setRecipients(Message.RecipientsType.TO,InternetAddress.parse(emailmessage.getTo_address()));
		msg.setSubject(emailmessage.getSubject());
		msg.setContent(emailmessage.getBody(),"text/html");
		msg.setSentDate(new Data());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailmessage.getBody(),"text/html");

		MultiPart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		MimeBodyPart attachPart = new MimeBodyPart();
		attachPart.attachFile("");
		multipart.addBodyPart(attachPart);
		msg.setContent(multipart);
		//send the email
		Transport.send(msg);



	}

}
