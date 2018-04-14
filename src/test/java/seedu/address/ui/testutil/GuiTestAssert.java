package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.ChatPanelHandle;
import guitests.guihandles.FoodCardHandle;
import guitests.guihandles.FoodListPanelHandle;
import guitests.guihandles.UserProfilePanelHandle;
import seedu.address.model.food.Food;
import seedu.address.model.user.UserProfile;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(FoodCardHandle expectedCard, FoodCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getEmail(), actualCard.getEmail());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedFood}.
     */
    public static void assertCardDisplaysFood(Food expectedFood, FoodCardHandle actualCard) {
        assertEquals(expectedFood.getName().fullName, actualCard.getName());
        assertEquals(expectedFood.getPhone().value, actualCard.getPhone());
        assertEquals(expectedFood.getEmail().value, actualCard.getEmail());
        assertEquals(expectedFood.getAddress().value, actualCard.getAddress());
        assertEquals(expectedFood.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that the list in {@code foodListPanelHandle} displays the details of {@code foods} correctly and
     * in the correct order.
     */
    public static void assertListMatching(FoodListPanelHandle foodListPanelHandle, Food... foods) {
        for (int i = 0; i < foods.length; i++) {
            assertCardDisplaysFood(foods[i], foodListPanelHandle.getFoodCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code foodListPanelHandle} displays the details of {@code foods} correctly and
     * in the correct order.
     */
    public static void assertListMatching(FoodListPanelHandle foodListPanelHandle, List<Food> foods) {
        assertListMatching(foodListPanelHandle, foods.toArray(new Food[0]));
    }

    /**
     * Asserts the size of the list in {@code foodListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(FoodListPanelHandle foodListPanelHandle, int size) {
        int numberOfPeople = foodListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code chatPanelHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ChatPanelHandle chatPanelHandle, String expected) {
        assertEquals(expected, chatPanelHandle.getText());
    }

    //@@author tohcheryl
    /**
     * Asserts that {@code actualPanel} displays the details of {@code expectedUser}.
     */
    public static void assertPanelDisplaysUser(UserProfile userProfile, UserProfilePanelHandle actualPanel) {
        assertEquals(userProfile.getName().fullName, actualPanel.getName());
        assertEquals(userProfile.getPhone().value, actualPanel.getPhone());
        assertEquals(userProfile.getAddress().value, actualPanel.getAddress());
        assertEquals(userProfile.getAllergies().stream().map(allergy -> allergy.allergyName)
                        .collect(Collectors.toList()),
                actualPanel.getAllergies());
    }
}
