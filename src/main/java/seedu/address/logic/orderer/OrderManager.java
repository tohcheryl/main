package seedu.address.logic.orderer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private static final String REMOTE_SERVER = "https://mysterious-temple-83678.herokuapp.com/";
    private static final String CREATE_PATH = "create/";

    private static final String PROPERTY_AUTH_HEADER = "mail.smtp.auth";
    private static final String PROPERTY_AUTH = "true";
    private static final String PROPERTY_TLS_HEADER = "mail.smtp.starttls.enable";
    private static final String PROPERTY_TLS = "true";
    private static final String PROPERTY_HOST_HEADER = "mail.smtp.host";
    private static final String PROPERTY_HOST = "smtp.gmail.com";
    private static final String PROPERTY_PORT_HEADER = "mail.smtp.port";
    private static final String PROPERTY_PORT = "587";

    private static final String CANNED_SPEECH_MESSAGE = "Hello, my name is %s. Could I order a %s to %s?";
    private static final String SUBJECT_LINE = "Order from HackEat. Reference code: %s";

    private final String username = "hackeatapp@gmail.com";
    private final String password = "hackeater";
    private final String from = username;

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
        String message = createMessage();

        generateEmailSession();
        sendEmail(message);

        sendOrder(toOrder.getPhone().toString(), message);
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
    private String createMessage() {
        return String.format(CANNED_SPEECH_MESSAGE, user.getName(), toOrder.getName(), user.getAddress());
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

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    /**
      * Sends order to REST API for TwiML to pick up
      */
    private void sendOrder(String toPhone, String body) {
        String data = toPhone + "//" +  body;
        try {
            URL url = new URL(REMOTE_SERVER + CREATE_PATH + orderId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes("UTF-8"));
            con.getInputStream();
            con.disconnect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
