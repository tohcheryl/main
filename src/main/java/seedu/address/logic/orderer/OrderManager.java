package seedu.address.logic.orderer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.mail.MessagingException;

import seedu.address.model.food.Food;
import seedu.address.model.user.UserProfile;

//@@author samzx

/**
 * Orders food in HackEat.
 */
public class OrderManager {

    public static final String CONTENT_SEPERATOR = "//";

    public static final String REMOTE_SERVER = "https://mysterious-temple-83678.herokuapp.com/";
    public static final String CREATE_PATH = "create/";
    public static final String ORDER_PATH = "order/";
    public static final String REQUEST_METHOD =  "POST";
    public static final String CHARSET_ENCODING = "UTF-8";

    public static final String CANNED_SPEECH_MESSAGE = "Hello, my name is %s. Could I order a %s to %s?";


    private String orderId;
    private UserProfile user;
    private Food toOrder;

    public OrderManager(UserProfile user, Food food) {
        this.user = user;
        this.toOrder = food;
        this.orderId = UUID.randomUUID().toString();
    }

    /**
     * Sends email summary and orders {@code Food} via phone
     */
    public void order() throws IOException, MessagingException {
        String message = createMessage();

        EmailManager emailManager = new EmailManager(user, toOrder, orderId, message);
        emailManager.email();

        sendOrder(toOrder.getPhone().toString(), message);
    }

    /**
     * Checks whether client can connect to server
     * @return whether client can connect to server
     */
    public static boolean netIsAvailable(String urlString) {
        try {
            final URL url = new URL(urlString);
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Creates the body based on a pre-defined message, the user and food values
     * @return the String format of the body
     */
    private String createMessage() {
        return String.format(CANNED_SPEECH_MESSAGE, user.getName(), toOrder.getName(), user.getAddress());
    }

    /**
      * Sends order to REST API for TwiML to pick up
      */
    private void sendOrder(String toPhone, String body) throws IOException {
        String data = toPhone + CONTENT_SEPERATOR +  body;
        URL url = new URL(REMOTE_SERVER + CREATE_PATH + orderId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(REQUEST_METHOD);
        con.setDoOutput(true);
        con.getOutputStream().write(data.getBytes(CHARSET_ENCODING));
        con.getInputStream();
        con.disconnect();
    }

    /**
     * Returns the orderId for this object
     */
    public String getOrderId() {
        return this.orderId;
    }
}
