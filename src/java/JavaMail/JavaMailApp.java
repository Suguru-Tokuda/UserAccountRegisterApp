/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaMail;

/**
 *
 * @author Sneh Vyas & Suguru Tokuda    
 */
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.Profile;

public class JavaMailApp {

    public boolean mail(Profile SignUpetails) {
        boolean sent = false;
        // Recipient's email ID needs to be mentioned.
        String to = SignUpetails.getEmail();

        // Sender's email ID needs to be mentioned
        String from = "svyas12@ilstu.edu";

        // Assuming you are sending email from this host
        String host = "m.outlook.com";

        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
        props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587"); // if needed
        props.put("mail.smtp.host", "m.outlook.com"); // if needed
        props.put("mail.smtp.auth", "true");
        // Get the default Session object.
        Session session = Session.getDefaultInstance(props);
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("svyas12@ilstu.edu",
                        "Sohanlal1!");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Your Account has been created");
            // Send the actual HTML message, as big as you like
            message.setContent("Hi " + SignUpetails.getFirstName() + "," + "<br/>" + "You have been Succesfully SignedUp." + "<br/>"
                    + "Your UserID :" + SignUpetails.getUserID() + "<br/>"
                    + "Your Password :" + SignUpetails.getPassword() + "<br/>"
                    + "Your Security Question :" + SignUpetails.getSecurityQuestion() + "<br/>"
                    + "<br/>Your Security Answer :" + SignUpetails.getSecurityAnswer() + "<br/>"
                    + "<br/>" + "Please keep in touch." + "<br/>" + "Regards," + "<br/>" + "Sneh Vyas"
                    + "<br/>" + "<img src=\"http://content.sportslogos.net/logos/32/707/thumbs/wgpjcd57fikjji1qy97f2gsqk.gif\">",
                    "text/html");

            // Send message
            Transport.send(message);
            sent = true;
            System.out.println("Sent message successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
            sent = false;
        }
        return sent;
    }
}