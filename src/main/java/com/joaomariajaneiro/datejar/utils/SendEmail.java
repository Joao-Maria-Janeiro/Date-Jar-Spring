package com.joaomariajaneiro.datejar.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    // Recipient's email ID needs to be mentioned.
    String to;

    // Assuming you are sending email from localhost
    String host;

    public SendEmail(String to, String host) {
        this.to = to;
        this.host = host;
    }

    public void sendMail(String subject, String emailBody) {
        // Get system properties
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS


        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EmailConstants.getUsername(),
                                EmailConstants.getPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConstants.getUsername()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(emailBody);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public String getTo() {
        return to;
    }

    public SendEmail setTo(String to) {
        this.to = to;
        return this;
    }


    public String getHost() {
        return host;
    }

    public SendEmail setHost(String host) {
        this.host = host;
        return this;
    }
}
