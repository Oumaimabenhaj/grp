package Util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUser {

    public static void sendEmailU(String recipientEmail, String subject, String messageBody) {
            Properties props = new Properties();
            //props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            String username = "oumaimabelhaj2020@gmail.com";
            String password = "ldzq guyy cmas fuxi";

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);
                message.setText(messageBody);

                Transport.send(message); // Corrected method name

                System.out.println("Message envoyé avec succès à : " + recipientEmail);
            } catch (MessagingException e) {
                throw new RuntimeException("Erreur lors de l'envoi du message : " + e.getMessage());
            }
        }



}
