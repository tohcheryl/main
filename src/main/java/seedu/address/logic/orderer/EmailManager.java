package seedu.address.logic.orderer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import seedu.address.model.food.Food;
import seedu.address.model.food.Price;
import seedu.address.model.user.UserProfile;

//@@author samzx

/**
 * Orders food in HackEat.
 */
public class EmailManager {

    public static final String EMAIL_CONTENT_MODE = "text/html";

    public static final String SUBJECT_LINE = "Order from HackEat. Reference code: %s";

    private static final String PROPERTY_AUTH_HEADER = "mail.smtp.auth";
    private static final String PROPERTY_AUTH = "true";
    private static final String PROPERTY_TLS_HEADER = "mail.smtp.starttls.enable";
    private static final String PROPERTY_TLS = "true";
    private static final String PROPERTY_HOST_HEADER = "mail.smtp.host";
    private static final String PROPERTY_HOST = "smtp.gmail.com";
    private static final String PROPERTY_PORT_HEADER = "mail.smtp.port";
    private static final String PROPERTY_PORT = "587";

    private static final String USERNAME = "hackeatapp@gmail.com";
    private static final String PASSWORD = "hackeater";
    private static final String FROM = USERNAME;

    private Session session;

    private String orderId;
    private UserProfile user;
    private Food toOrder;
    private String to;
    private String message;

    public EmailManager(UserProfile user, Food food, String orderId, String message) {
        this.user = user;
        this.toOrder = food;
        this.orderId = orderId;
        this.message = message;
    }

    /**
     * Creates an email session, fills in email contents and sends.
     * @throws MessagingException when able to utilise email session
     */
    public void email() throws MessagingException {
        generateEmailSession();
        sendEmail();
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
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }

    /**
     * Sends an email to a food's email address
     */
    private void sendEmail() throws MessagingException {

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(String.format(SUBJECT_LINE, orderId));
        message.setContent(buildContent(), EMAIL_CONTENT_MODE);
        Transport.send(message);
    }

    /**
     * Creates the body of the email message for ease of reading
     * @return the raw HTML string of the email content
     */
    private String buildContent() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(buildHeading());
        stringBuilder.append(buildExcerpt());
        stringBuilder.append(buildSummary());
        stringBuilder.append(buildFooter());

        return stringBuilder.toString();
    }

    /**
     * Builds the heading for the content of the email
     * @return the built string
     */
    private String buildHeading() {
        return wrapH1(String.format("Order for %s.", user.getName()));
    }

    /**
     * Builds the excerpt message for the content of the email
     * @return the built string
     */
    private String buildExcerpt() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(wrapH2(String.format("%s has sent the following message:", user.getName())));
        stringBuilder.append(wrapPre(message));

        return stringBuilder.toString();
    }

    /**
     * Builds the order summary for the content of the email
     * @return the built string
     */
    private String buildSummary() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(wrapH2("Order summary:"));
        stringBuilder.append(wrapUl(String.format("Food: %s", toOrder.getName())));
        stringBuilder.append(wrapUl(String.format("Price: %s", Price.displayString(toOrder.getPrice().getValue()))));
        stringBuilder.append(wrapUl(String.format("Address: %s", user.getAddress())));
        stringBuilder.append(wrapUl(
                String.format("Time: %s",
                        new Date(Clock.fixed(Instant.now(), ZoneId.systemDefault()).millis()).toString())
        ));

        return stringBuilder.toString();
    }

    /**
     * Builds the footer for the content of the email
     * @return the built string
     */
    private String buildFooter() {
        return wrapI("Thank you, from the HackEat team.");
    }

    private String wrapH1(String content) {
        return "<h1>" + content + "</h1>";
    }

    private String wrapH2(String content) {
        return "<h2>" + content + "</h2>";
    }

    private String wrapUl(String content) {
        return "<ul>" + content + "</ul>";
    }

    private String wrapPre(String content) {
        return "<pre>" + content + "</pre>";
    }

    private String wrapI(String content) {
        return "<i>" + content + "</i>";
    }
}
