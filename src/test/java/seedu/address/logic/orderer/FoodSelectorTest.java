package seedu.address.logic.orderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATING_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIED;

import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.food.Address;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.user.UserProfile;
import seedu.address.testutil.FoodBuilder;


//@@author samzx

public class FoodSelectorTest {
    private static final String USER_NAME = "Alice";
    private static final String USER_PHONE = "12345678";
    private static final String USER_ALLERGY = "lactose";
    private static final String USER_NOT_ALLERGIC = "peanut";
    private static final String MESSAGE_SHOULD_AVOID_ALLERGIC =
            "Food selector should have avoided a food the user is allergic to!";
    private static final String MESSAGE_SHOULD_SELECT_NOT_ALLERGIC =
            "Food selector should have selected a food the user is not allergic to!";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private UserProfile validUser = new UserProfile(
            new Name(USER_NAME),
            new Phone(USER_PHONE),
            new Address(Address.DEFAULT_ADDRESS),
            new HashSet<>(
                    Arrays.asList(new Allergy(USER_ALLERGY))
            )
    );

    @Test
    public void constructor_withoutArguments_success() {
        FoodSelector foodSelector = new FoodSelector();
        assertNotNull(foodSelector);
    }

    @Test
    public void selectIndex_withModel_validIndex() {
        FoodSelector fs = new FoodSelector();
        try {
            Index index = fs.selectIndex(model);
            assertNotNull(index);
        } catch (CommandException ce) {
            assertEquals(ce, OrderCommand.MESSAGE_SELECT_FAIL);
        }
    }

    @Test
    public void selectIndex_withAllAllergy_invalidIndex() throws Exception {
        AddressBook allAllergicAddressBook = new AddressBook();
        allAllergicAddressBook.initUserProfile(validUser);

        Food allergicFood = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_ALLERGY).build();

        allAllergicAddressBook.addFood(allergicFood);
        Model model = new ModelManager(allAllergicAddressBook, new UserPrefs());

        FoodSelector fs = new FoodSelector();
        try {
            fs.selectIndex(model);
            throw new AssertionError(MESSAGE_SHOULD_AVOID_ALLERGIC);
        } catch (AssertionError e) {
            throw new Exception(MESSAGE_SHOULD_AVOID_ALLERGIC);
        } catch (Exception e) {
            assertEquals(e.getMessage(), OrderCommand.MESSAGE_SELECT_FAIL);
        }
    }

    @Test
    public void selectIndex_nonAllergy_indexOne() throws Exception {
        AddressBook allAllergicAddressBook = new AddressBook();
        allAllergicAddressBook.initUserProfile(validUser);

        // Added to be index 0
        Food foodIsAllergic = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_ALLERGY).build();
        // Added to be index 1
        Food foodIsNotAllergic = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_NOT_ALLERGIC).build();

        allAllergicAddressBook.addFood(foodIsAllergic);
        allAllergicAddressBook.addFood(foodIsNotAllergic);

        Model model = new ModelManager(allAllergicAddressBook, new UserPrefs());

        FoodSelector fs = new FoodSelector();

        final int expectedSelectedIndex = 1;

        try {
            assertEquals(expectedSelectedIndex, fs.selectIndex(model).getZeroBased());
        } catch (Exception e) {
            throw new Exception(MESSAGE_SHOULD_SELECT_NOT_ALLERGIC);
        }
    }
}
