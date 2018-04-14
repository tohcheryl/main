package seedu.address.logic.orderer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;

import seedu.address.model.food.Food;
import seedu.address.model.user.UserProfile;


//@@author samzx

/**
 * Orders food in HackEat.
 */
public class OrderManager {

    // Find your Account Sid, Token and phone number used at twilio.com/console
    public static final String ACCOUNT_SID = "AC08ed603e3a4de8c0055e27ed8f5e8a3e";
    public static final String AUTH_TOKEN = "97fbd0228fa8419cb931583626039e00";
    public static final String OUTGOING_PHONE = "+16123245532";

    // Country code specific to Singapore at the moment
    public static final String LOCAL_COUNTRY_CODE = "+65";
    public static final String CANNED_SPEECH_MESSAGE = "Hello, my name is %s. Could I order a %s to %s?";
    public static final String ORDER_PATH = "order/";
    public static final String CREATE_PATH = "create/";
    public static final String REMOTE_SERVER = "https://mysterious-temple-83678.herokuapp.com/";

    private String orderId;
    private UserProfile user;
    private Food toOrder;

    public OrderManager(UserProfile user, Food food) {
        this.user = user;
        this.toOrder = food;
        this.orderId = UUID.randomUUID().toString();
    }

    /**
     * Uses Twilio API to begin call and order {@code Food}
     */
    public void order() throws URISyntaxException, IOException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String to = LOCAL_COUNTRY_CODE + toOrder.getPhone();
        String from = OUTGOING_PHONE;

        createOrder(String.format(CANNED_SPEECH_MESSAGE, user.getName(), toOrder.getName(), user.getAddress()));

        Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI(REMOTE_SERVER + ORDER_PATH + orderId)).create();
    }

    /**
     *  Use TwiML to generate speech
     *  Say Hello. Wait for response. Say order. Wait for response. Say Thank you.
     */
    private void createOrder(String speech) throws IOException {

        Say say  = new Say.Builder(
                speech)
                .build();
        VoiceResponse voiceResponse = new VoiceResponse.Builder()
                .say(say)
                .build();

        sendOrder(voiceResponse.toXml());
    }

    /**
     * Sends order to REST API for TwiML to pick up
     */
    private void sendOrder(String body) throws IOException {
        URL url = new URL(REMOTE_SERVER + CREATE_PATH + orderId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(body.getBytes("UTF-8"));
        con.getInputStream();
        con.disconnect();
    }
}
