package com.example.workflow;


import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class Email implements JavaDelegate{

    public void execute (DelegateExecution execution) throws Exception{

        String email= (String) execution.getVariable("email");
        System.out.println("Email preparation to "+email);

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        String myAccountEmail="gmail@gmail.com";
        String password="password";


        Session session=Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(myAccountEmail,password);
            }
        });

        Message message= prepareMessage (session, myAccountEmail, email);


        try {

            System.out.println("sending email");
            Transport.send(message);
            System.out.println("Message sent successfully");

        } catch (MessagingException e) {

            e.printStackTrace();
            System.out.println("Message was not send");
        }


    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient) {
        try {
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress (recipient));
            message.setSubject("Confirmation");
            message.setText("Welcome to Camunda");
            return message;

        } catch (Exception ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE,null, ex);
        }
        return null;
    }
}
