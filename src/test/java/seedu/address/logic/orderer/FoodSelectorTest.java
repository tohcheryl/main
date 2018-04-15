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
    public void selectIndex_withModel_validIndex() throws Exception {
        FoodSelector fs = new FoodSelector();
        Index index = fs.selectIndex(model);
        assertNotNull(index);
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

        FoodSelector foodSelector = new FoodSelector();

        assertAvoidsAllergies(foodSelector, model, OrderCommand.MESSAGE_SELECT_FAIL);
    }

    @Test
    public void selectIndex_nonAllergy_indexOne() throws Exception {
        AddressBook allAllergicAddressBook = new AddressBook();
        allAllergicAddressBook.initUserProfile(validUser);

        Food foodIsAllergic = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_ALLERGY).build();
        Food foodIsNotAllergic = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_NOT_ALLERGIC).build();

        allAllergicAddressBook.addFood(foodIsAllergic);
        allAllergicAddressBook.addFood(foodIsNotAllergic);

        Model model = new ModelManager(allAllergicAddressBook, new UserPrefs());

        FoodSelector fs = new FoodSelector();

        final int expectedSelectedIndex = 1;

        assertEquals(expectedSelectedIndex, fs.selectIndex(model).getZeroBased());
    }

    /**
     * Executes selectIndex method for food selector. Given a model with all allergic, makes sure that an exception
     * is thrown is the desired message, to avoid ordering a food that the user is allergic to, and to notify them
     * @param foodSelector that will have selectIndex executed
     * @param model which must have all foods be allergic by the userProfile
     * @param expectedMessage when unable to order food
     * @throws Exception when an allergic food is ordered
     */
    private static void assertAvoidsAllergies(FoodSelector foodSelector, Model model, String expectedMessage)
            throws Exception {
        try {
            foodSelector.selectIndex(model);
            throw new AssertionError(MESSAGE_SHOULD_AVOID_ALLERGIC);
        } catch (AssertionError e) {
            throw new Exception(MESSAGE_SHOULD_AVOID_ALLERGIC);
        } catch (Exception e) {
            assertEquals(e.getMessage(), expectedMessage);
        }
    }
}
