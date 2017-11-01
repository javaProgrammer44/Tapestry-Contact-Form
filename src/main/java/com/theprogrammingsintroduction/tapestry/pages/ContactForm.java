package com.theprogrammingsintroduction.tapestry.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Yasir Shabbir on 10/30/2017
 */

public class ContactForm {


    @Property
    private String target; // target page name


    @Property
    @Validate("required, minlength=3, maxlength=50")
    private String subject;


    @Property
    private String message;


    @Property
    @Validate("required,email")
    private String email;


    @Property
    @Validate("required,email")
    private String toEmail;


    @Property
    private String password;



    @Component
    private Form contactForm;

    @Inject
    private Messages messages;




    @OnEvent(value = EventConstants.VALIDATE, component = "ContactForm")
    public void checkForm() {

        System.out.println();
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "ContactForm")
    public Object proceedContactEmail() {

        System.out.println(this.email);
        System.out.println(this.subject);
        System.out.println(this.message);
        System.out.println("SimpleEmail Start");

        sendEmail();
        return Index.class;
    }

    private void sendEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(toEmail, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.email));
            message.setSubject(this.subject);
            message.setText(this.message);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}


