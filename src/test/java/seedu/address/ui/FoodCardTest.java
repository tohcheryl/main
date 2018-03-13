package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysFood;

import org.junit.Test;

import guitests.guihandles.FoodCardHandle;
import seedu.address.model.food.Food;
import seedu.address.testutil.FoodBuilder;

public class FoodCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Food foodWithNoTags = new FoodBuilder().withTags(new String[0]).build();
        FoodCard foodCard = new FoodCard(foodWithNoTags, 1);
        uiPartRule.setUiPart(foodCard);
        assertCardDisplay(foodCard, foodWithNoTags, 1);

        // with tags
        Food foodWithTags = new FoodBuilder().build();
        foodCard = new FoodCard(foodWithTags, 2);
        uiPartRule.setUiPart(foodCard);
        assertCardDisplay(foodCard, foodWithTags, 2);
    }

    @Test
    public void equals() {
        Food food = new FoodBuilder().build();
        FoodCard foodCard = new FoodCard(food, 0);

        // same food, same index -> returns true
        FoodCard copy = new FoodCard(food, 0);
        assertTrue(foodCard.equals(copy));

        // same object -> returns true
        assertTrue(foodCard.equals(foodCard));

        // null -> returns false
        assertFalse(foodCard.equals(null));

        // different types -> returns false
        assertFalse(foodCard.equals(0));

        // different food, same index -> returns false
        Food differentFood = new FoodBuilder().withName("differentName").build();
        assertFalse(foodCard.equals(new FoodCard(differentFood, 0)));

        // same food, different index -> returns false
        assertFalse(foodCard.equals(new FoodCard(food, 1)));
    }

    /**
     * Asserts that {@code foodCard} displays the details of {@code expectedFood} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(FoodCard foodCard, Food expectedFood, int expectedId) {
        guiRobot.pauseForHuman();

        FoodCardHandle foodCardHandle = new FoodCardHandle(foodCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", foodCardHandle.getId());

        // verify food details are displayed correctly
        assertCardDisplaysFood(expectedFood, foodCardHandle);
    }
}
