# samzx
###### /java/seedu/address/logic/orderer/OrderManager.java
``` java

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
}
```
###### /java/seedu/address/logic/orderer/FoodSelector.java
``` java

/**
 * Selects food in HackEat.
 */
public class FoodSelector {
    /**
     * Selects a {@code Food} based on the HackEat Algorithm
     * @param model the current model of the program
     * @return the index of the selected food
     */
    public Index select(Model model) {
        ArrayList<FoodScore> foodScores = generateFoodList(model);
        FoodScore fs = pickFood(foodScores);
        return fs.index;
    }

    /**
     * Selects a food randomly with weighting from a list of food with scores {@code FoodScore}
     * @param foodScores an ArrayList of {@code FoodScore}
     * @return the selected {@code FoodScore}
     */
    private FoodScore pickFood(ArrayList<FoodScore> foodScores) {

        float runningScore = 0;
        for (FoodScore foodScore : foodScores) {
            runningScore += foodScore.score;
            foodScore.runningScore = runningScore;
        }

        float decidingNumber = (new Random()).nextFloat() * runningScore;

        for (FoodScore foodScore : foodScores) {
            if (decidingNumber < foodScore.runningScore) {
                return foodScore;
            }
        }

        return null;
    }

    /**
     * Generates a list of food based on the model provided
     * @param model to be provided
     * @return a list of food
     */
    private ArrayList<FoodScore> generateFoodList(Model model) {
        ArrayList<FoodScore> foodScores = new ArrayList<>();

        List<Food> lastShownList = model.getFilteredFoodList();

        for (int i = 0; i < lastShownList.size(); i++) {
            FoodScore fs = new FoodScore(lastShownList.get(i), Index.fromZeroBased(i));
            fs.score = calculateScore(fs.food, model.getUserProfile().getAllergies());
            foodScores.add(fs);
        }
        return foodScores;
    }

    /**
     * Calculates a score based on some metric provided by the {@code Food} class
     * @param food The food that requires a score to be derived from
     * @param userAllergies a set of allergies of the user
     * @return the score for that particular food
     */
    private float calculateScore(Food food, Set<Allergy> userAllergies) {
        float score = 0;
        for (Allergy allergy : food.getAllergies()) {
            if (userAllergies.contains(allergy)) {
                return 0;
            }
        }

        score += 1.0 + Integer.parseInt(food.getRating().value);
        score /= 1.0 + Float.parseFloat(food.getPrice().getValue());

        return score;
    }

    /**
     * A private class that holds data for Food
     */
    class FoodScore {
        private Food food;
        private Index index;
        private float score;
        private float runningScore;

        FoodScore (Food food, Index index) {
            this.food = food;
            this.index = index;
        }
    }
}
```
###### /java/seedu/address/logic/parser/OrderCommandParser.java
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
     * Given a {@code String} of arguments in the context of the OrderCommand
     * and returns an OrderCommand object for execution.
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
###### /java/seedu/address/logic/commands/OrderCommand.java
``` java

/**
 * Orders food in HackEat.
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Orders a food "
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_SUCCESS = "Successfully emailed restaurant for %1$s";
    public static final String MESSAGE_SELECT_FAIL = "Unable to select food at random";
    public static final String MESSAGE_EMAIL_FAIL_FOOD = "Order failure for: %s";

    private Food toOrder;
    private Index index;

    /**
     * Creates an Order command to the specified index of {@code Food}
     */
    public OrderCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            OrderManager manager = new OrderManager(model.getAddressBook().getUserProfile(), toOrder);
            manager.order();

            return new CommandResult(String.format(MESSAGE_SUCCESS, toOrder.getName()));
        } catch (Exception e) {
            throw new CommandException(String.format(MESSAGE_EMAIL_FAIL_FOOD, toOrder.getName()));
        }
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        try {
            List<Food> lastShownList = model.getFilteredFoodList();

            if (this.index == null) {
                FoodSelector fs = new FoodSelector();
                this.index = fs.select(model);
            }

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
            }

            toOrder = lastShownList.get(index.getZeroBased());
        } catch (Exception e) {
            throw new CommandException(MESSAGE_SELECT_FAIL);
        }

    }

    @Override
    public boolean equals(Object other) {
        try {
            return other == this // short circuit if same object
                    || (other instanceof OrderCommand // instanceof handles nulls
                    && index.equals(((OrderCommand) other).index));

        } catch (NullPointerException npe) {
            return other == this // short circuit if same object
                    || (other instanceof OrderCommand // instanceof handles nulls
                    && index == (((OrderCommand) other).index));
        }

    }

}
```
###### /java/seedu/address/model/food/Price.java
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
###### /java/seedu/address/model/food/Rating.java
``` java

/**
 * Represents a Food's rating in HackEat.
 * Guarantees: immutable; is valid as declared in {@link #isValidRating(String)}
 */
public class Rating {

    public static final String DEFAULT_RATING = "0";
    public static final int MAX_RATING = 5;
    public static final String MESSAGE_RATING_CONSTRAINTS =
            "Please enter a number between 0 to " + MAX_RATING;

    /*
     * User must enter only a single digit.
     */
    public static final String RATING_VALIDATION_REGEX = "\\b\\d\\b";

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
     * Returns true if a given string is a valid food email.
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
     * @return a string of colored or uncolore stars
     */
    public static String displayString(String value) {
        final int rating = Integer.parseInt(value);
        int count = rating;
        String stars = "";
        for (int i = 0; i < MAX_RATING; i++) {
            if (count > 0) {
                stars += "★";
            } else {
                stars += "☆";
            }
            count--;
        }
        return stars;
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
