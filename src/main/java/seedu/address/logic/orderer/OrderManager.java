package seedu.address.logic.orderer;

import java.net.URI;
import java.net.URISyntaxException;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import seedu.address.model.Model;
import seedu.address.model.user.UserProfile;

/**
 * Orders food in HackEat.
 */
public class OrderManager {

    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC08ed603e3a4de8c0055e27ed8f5e8a3e";
    public static final String AUTH_TOKEN = "97fbd0228fa8419cb931583626039e00";


    /**
     * Begins phone call to order {@code Food}
     */
    public static void beginOrder(Model model) throws URISyntaxException {
        UserProfile user = model.getAddressBook().getUserProfile();
        beginCall();
    }

    /**
     * @throws URISyntaxException
     */
    private static void beginCall() throws URISyntaxException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String from = "+16123245532";
        String to = "+6581321850";

        Call call = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI("http://demo.twilio.com/docs/voice.xml")).create();

        System.out.println(call.getSid());
    }
}
