# samzx
###### \java\seedu\address\logic\commands\OrderCommand.java
``` java

/**
 * Orders command which starts the selection and ordering food process in HackEat.
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Orders a food "
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_SUCCESS = "%1$s has been requested to be ordered.";
    public static final String MESSAGE_SELECT_FAIL = "You seem to be allergic to all the foods listed here.";
    public static final String MESSAGE_SELECT_INDEX_FAIL = "Sorry, can't order that, you seem to be allergic to %s";
    public static final String MESSAGE_FAIL_FOOD = "Something went wrong, we could not order %s";
    public static final String MESSAGE_CHECK_INTERNET_CONNECTION = "Failed to contact our servers. "
            + "Please check your internet connection.";
    public static final String MESSAGE_EMAIL_FAIL_FOOD = "%1$s has failed to be ordered via email. "
            + MESSAGE_CHECK_INTERNET_CONNECTION;
    public static final String MESSAGE_DIAL_FAIL_FOOD = "%1$s has failed to be ordered via phone. "
            + MESSAGE_CHECK_INTERNET_CONNECTION;

    private Food toOrder;
    private Index index;

    /**
     * Creates an Order command to the specified index of {@code Food}
     */
    public OrderCommand(Index index) {
        this.index = index;
    }

    /**
     * Selects a index based on {@code FoodSelector} algorithm if not selected yet
     * @throws CommandException if unable to selectIndex food
     */
    private void getIndexIfNull() throws CommandException {
        if (this.index == null) {
            FoodSelector fs = new FoodSelector();
            this.index = fs.selectIndex(model);
        }
    }

    /**
     * Verifies that the index is smaller than the size of a list
     * @param list which the index can not exceed the size
     * @throws CommandException if index exceeds list size
     */
    private void verifyIndex(Index index, List list) throws CommandException {
        if (index.getZeroBased() >= list.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }
    }

    /**
     * Checks a food for allergies
     * @param food to check for allergy
     * @throws CommandException is thrown if food contains an allergy same as user
     */
    private void checkForAllergy(Food food) throws CommandException {
        for (Allergy allergy : food.getAllergies()) {
            if (model.getUserProfile().getAllergies().contains(allergy)) {
                throw new CommandException(String.format(MESSAGE_SELECT_INDEX_FAIL, food.getName()));
            }
        }
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            if (!OrderManager.netIsAvailable(OrderManager.REMOTE_SERVER)) {
                throw new CommandException(String.format(MESSAGE_CHECK_INTERNET_CONNECTION));
            } else {
                OrderManager manager = new OrderManager(model.getAddressBook().getUserProfile(), toOrder);
                manager.order();
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS, toOrder.getName()));
        } catch (CommandException e) {
            throw e;
        } catch (MessagingException e) {
            throw new CommandException(String.format(MESSAGE_EMAIL_FAIL_FOOD, toOrder.getName()));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_DIAL_FAIL_FOOD, toOrder.getName()));
        } catch (Exception e) {
            throw new CommandException(String.format(MESSAGE_FAIL_FOOD, toOrder.getName()));
        }
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Food> lastShownList = model.getFilteredFoodList();

        getIndexIfNull();
        verifyIndex(this.index, lastShownList);

        Food aboutToOrder = lastShownList.get(index.getZeroBased());
        checkForAllergy(aboutToOrder);

        toOrder = aboutToOrder;
    }

    @Override
    public boolean equals(Object other) {
        try {
            return other == this
                    || (other instanceof OrderCommand
                    && index.equals(((OrderCommand) other).index));

        } catch (NullPointerException npe) {
            return other == this
                    || (other instanceof OrderCommand
                    && index == (((OrderCommand) other).index));
        }

    }

}
```
###### \java\seedu\address\logic\orderer\EmailManager.java
``` java

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
```
###### \java\seedu\address\logic\orderer\FoodSelector.java
``` java

/**
 * Selects food in HackEat if the user has not specified a specific food to order.
 */
public class FoodSelector {
    private static final float SCORE_BUFFER = 0.001f;
    /**
     * Selects an {@code Index} from a model based on the HackEat Algorithm
     * @param model of the program
     * @return the index of the selected food
     */
    public Index selectIndex(Model model) throws CommandException {
        ArrayList<FoodDescriptor> foodDescriptors = buildFoodDescriptorList(model);
        FoodDescriptor foodDescriptor = pickFood(foodDescriptors);
        return foodDescriptor.index;
    }

    /**
     * Selects a food randomly with weighting from a list of food with scores {@code FoodDescriptor}
     * @param foodDescriptors an ArrayList of {@code FoodDescriptor}
     * @return the selected {@code FoodDescriptor}
     */
    private FoodDescriptor pickFood(ArrayList<FoodDescriptor> foodDescriptors) throws CommandException {

        float runningScore = 0;
        for (FoodDescriptor foodDescriptor : foodDescriptors) {
            runningScore += foodDescriptor.score;
            foodDescriptor.runningScore = runningScore;
        }

        float decidingNumber = (new Random()).nextFloat() * runningScore;

        for (FoodDescriptor foodDescriptor : foodDescriptors) {
            if (decidingNumber < foodDescriptor.runningScore) {
                return foodDescriptor;
            }
        }

        throw new CommandException(OrderCommand.MESSAGE_SELECT_FAIL);
    }

    /**
     * Generates a list of food based on the model provided
     * @param model to be provided
     * @return a list of food
     */
    private ArrayList<FoodDescriptor> buildFoodDescriptorList(Model model) {
        ArrayList<FoodDescriptor> foodDescriptors = new ArrayList<>();

        List<Food> lastShownList = model.getFilteredFoodList();

        for (int i = 0; i < lastShownList.size(); i++) {
            FoodDescriptor foodDescriptor = new FoodDescriptor(lastShownList.get(i), Index.fromZeroBased(i));
            foodDescriptor.score = calculateScore(foodDescriptor.food, model.getUserProfile().getAllergies());
            foodDescriptors.add(foodDescriptor);
        }
        return foodDescriptors;
    }

    /**
     * Calculates a score based on some metric provided by the {@code Food} class
     * @param food The food that requires a score to be derived from
     * @param userAllergies a set of allergies of the user
     * @return the score for that particular food
     */
    private float calculateScore(Food food, Set<Allergy> userAllergies) {
        float score;

        for (Allergy allergy : food.getAllergies()) {
            if (userAllergies.contains(allergy)) {
                return 0;
            }
        }

        score = 1;
        score *= scoreFromRating(food.getRating()) + SCORE_BUFFER;
        score *= scoreFromPrice(food.getPrice()) + SCORE_BUFFER;

        assert(score > 0);

        return score;
    }

    /**
     * Outputs a score based on the value of the price
     * For dampener variable:
     * -    dampener = 1, Roughly twice more likely to order a food of $5, than of value $10
     * -    dampener = 1.5, Roughly 50% more likely to order a food of $5, than of value $10
     * -    dampener = 2, Roughly 30% more likely to order a food of $5, than of value $10
     * @param price to have score derived from
     * @return score determined by algorithm
     */
    private float scoreFromPrice(Price price) {
        final float dampener = 1;

        float value = Float.parseFloat(price.getValue());

        return (float) Math.pow(1 / (value + 1), 1 / dampener);
    }

    /**
     * Outputs a score based on the value of the rating
     * For weighting variable:
     * -    weighting = 1, 5x more likely to order a food of rating 5, than of 1
     * -    weighting = 1.5, ~10x more likely to order a food of rating 5, than of 1
     * -    weighting = 2, 25x more likely to order a food of rating 5, than of 1
     * @param rating to have score derived from
     * @return score determined by algorithm
     */
    private float scoreFromRating(Rating rating) {
        final float weighting = 1;

        float value = Float.parseFloat(rating.value);

        return (float) Math.pow(value, weighting);
    }

    /**
     * Holds descriptions of the food for calculation purposes
     */
    class FoodDescriptor {
        private Food food;
        private Index index;
        private float score;
        private float runningScore;

        FoodDescriptor(Food food, Index index) {
            this.food = food;
            this.index = index;
        }
    }
}
```
###### \java\seedu\address\logic\orderer\OrderManager.java
``` java

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
```
###### \java\seedu\address\logic\parser\OrderCommandParser.java
``` java

/**
 * Parses input arguments and creates a new Order Command object
 */
public class OrderCommandParser implements Parser<OrderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the OrderCommand
     * and returns an OrderCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public OrderCommand parse(String args) throws ParseException {

        if (withIndexArgument(args)) {
            return orderCommandWithIndex(args);
        } else {
            return orderCommandWithoutIndex();
        }

    }

    /**
     * Returns a default order command without a specific index to order
     */
    private OrderCommand orderCommandWithoutIndex() {
        return new OrderCommand(null);
    }

    /**
     * Returns an OrderCommand object for execution when given a {@code String} of
     * arguments in the context of the OrderCommand.
     * @throws ParseException if the user input does not conform the expected format
     */
    private OrderCommand orderCommandWithIndex(String args) throws ParseException {
        Index index;
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args);
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderCommand.MESSAGE_USAGE));
        }
        return new OrderCommand(index);
    }

    private boolean withIndexArgument(String args) {
        return !args.equals("");
    }
}


```
###### \java\seedu\address\model\food\Price.java
``` java

    /**
     * Parses price value into more recognisable price format with $.
     * @param value of price
     * @return price value with $ prefixed
     */
    public static String displayString(String value) {
        return "$" + value;
    }

```
###### \java\seedu\address\model\food\Rating.java
``` java

/**
 * Represents a Food's rating in HackEat.
 */
public class Rating {

    public static final String DEFAULT_RATING = "0";
    public static final int MAX_RATING = 5;
    public static final String MESSAGE_RATING_CONSTRAINTS =
            "Please enter a number between 0 to " + MAX_RATING;

    /**
     * Users must enter only a single digit.
     */
    public static final String RATING_VALIDATION_REGEX = "\\b\\d\\b";
    public static final String CLASS_NAME = "Rating";

    private static final String UNFILLED_RATING_SYMBOL = "☆";
    private static final String FILLED_RATING_SYMBOL = "★";

    public final String value;

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A valid rating.
     */
    public Rating(String rating) {
        requireNonNull(rating);
        checkArgument(isValidRating(rating), MESSAGE_RATING_CONSTRAINTS);
        this.value = rating;
    }

    /**
     * Returns true if a given string is a valid food rating.
     */
    public static boolean isValidRating(String test) {
        if (test.matches(RATING_VALIDATION_REGEX)) {
            int rating = Integer.parseInt(test);
            if (rating >= 0 && rating <= MAX_RATING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to display ratings as stars instead of a number
     * @return a string of colored or uncolored stars
     */
    public static String displayString(String value) {
        int count = Integer.parseInt(value);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < MAX_RATING; i++) {
            if (count > 0) {
                stringBuilder.append(FILLED_RATING_SYMBOL);
            } else {
                stringBuilder.append(UNFILLED_RATING_SYMBOL);
            }
            count--;
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rating // instanceof handles nulls
                && this.value.equals(((Rating) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
