package seedu.address.logic.orderer;

import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import seedu.address.model.food.Food;
import seedu.address.model.user.UserProfile;

//@@author {samzx}

/**
 * Orders food in HackEat.
 */
public class OrderManager {

    static final String PROPERTY_AUTH_HEADER = "mail.smtp.auth";
    static final String PROPERTY_AUTH = "true";
    static final String PROPERTY_TLS_HEADER = "mail.smtp.starttls.enable";
    static final String PROPERTY_TLS = "true";
    static final String PROPERTY_HOST_HEADER = "mail.smtp.host";
    static final String PROPERTY_HOST = "smtp.gmail.com";
    static final String PROPERTY_PORT_HEADER = "mail.smtp.port";
    static final String PROPERTY_PORT = "587";

    static final String CANNED_SPEECH_MESSAGE = "Hello, my name is %s. Could I order a %s to %s?";
    static final String SUBJECT_LINE = "Order from HackEat. Reference code: %s";

    final String username = "hackeatapp@gmail.com";
    final String password = "hackeater";
    final String from = username;

    private Session session;

    private String orderId;
    private UserProfile user;
    private Food toOrder;
    private String to;

    public OrderManager(UserProfile user, Food food) {
        this.user = user;
        this.toOrder = food;
        this.orderId = UUID.randomUUID().toString();
    }

    /**
     * Uses TLS email protocol to begin call and order {@code Food}
     */
    public void order() {
        generateEmailSession();
        sendEmail(createBody());
    }

    /**
     * Generates a new email session with pre-defined seetings
     */
    private void generateEmailSession() {
        to = toOrder.getEmail().toString();

        Properties props = new Properties();
        props.put(PROPERTY_AUTH_HEADER, PROPERTY_AUTH);
        props.put(PROPERTY_TLS_HEADER, PROPERTY_TLS);
        props.put(PROPERTY_HOST_HEADER, PROPERTY_HOST);
        props.put(PROPERTY_PORT_HEADER, PROPERTY_PORT);

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    /**
     * Creates the body based on a pre-defined message, the user and food values
     * @return the String format of the body
     */
    private String createBody() {
        return String.format(CANNED_SPEECH_MESSAGE, user.getName(), toOrder.getName(), toOrder.getAddress());
    }

    /**
     * Sends an email to a food's email address
     * @param body of the email sent
     */
    private void sendEmail(String body) {

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(String.format(SUBJECT_LINE, orderId));

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
