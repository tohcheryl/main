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
    public static final String LOCAL_COUNTRY_CODE = "+65";
    public static final String TEMP_FROM_PHONE = "+16123245532";

    /**
     * Sets up variables to begin ordering {@code Food}
     */
    public static void order(Model model) throws URISyntaxException {
        UserProfile user = model.getAddressBook().getUserProfile();
        String localPhoneNumber = LOCAL_COUNTRY_CODE + user.getPhone();
        beginCall(localPhoneNumber);
    }

    /**
     * Uses Twilio API to begin call
     * @throws URISyntaxException
     */
    private static void beginCall(String to) throws URISyntaxException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String from = TEMP_FROM_PHONE;

        Call call = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI("http://demo.twilio.com/docs/voice.xml")).create();

        System.out.println(call.getSid());
    }
}
