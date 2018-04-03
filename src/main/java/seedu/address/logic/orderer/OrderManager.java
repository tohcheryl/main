package seedu.address.logic.orderer;

import static spark.Spark.post;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;

import seedu.address.model.food.Food;
import seedu.address.model.user.UserProfile;


/**
 * Orders food in HackEat.
 */
public class OrderManager {

    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC08ed603e3a4de8c0055e27ed8f5e8a3e";
    public static final String AUTH_TOKEN = "97fbd0228fa8419cb931583626039e00";
    public static final String LOCAL_COUNTRY_CODE = "+65";
    public static final String TEMP_FROM_PHONE = "+16123245532";

    private UserProfile user;
    private Food toOrder;

    public OrderManager(UserProfile user, Food food) {
        this.user = user;
        this.toOrder = food;
    }

    /**
     * Sets up variables to begin ordering {@code Food}
     */
    public void order() throws URISyntaxException, UnknownHostException {
        String localPhoneNumber = LOCAL_COUNTRY_CODE + toOrder.getPhone();
        beginCall(localPhoneNumber);
    }

    /**
     * Uses Twilio API to begin call
     * @throws URISyntaxException
     */
    private void beginCall(String to) throws URISyntaxException, UnknownHostException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String from = TEMP_FROM_PHONE;

        updateSpeech(String.format("Hello. Could I order a %1$s to %2$s?", toOrder.getName(), user.getAddress()));

        Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI("http://33cc3524.ngrok.io/")).create();
    }

    /**
     *  Use TwiML to generate speech
     */
    private static void updateSpeech(String speech) {

        post("/", (request, response) -> {
            Say say  = new Say.Builder(
                    speech)
                    .build();
            VoiceResponse voiceResponse = new VoiceResponse.Builder()
                    .say(say)
                    .build();
            return voiceResponse.toXml();
        });
    }
}
