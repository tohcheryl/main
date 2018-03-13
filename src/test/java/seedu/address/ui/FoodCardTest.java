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
        FoodCard personCard = new FoodCard(foodWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, foodWithNoTags, 1);

        // with tags
        Food foodWithTags = new FoodBuilder().build();
        personCard = new FoodCard(foodWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, foodWithTags, 2);
    }

    @Test
    public void equals() {
        Food food = new FoodBuilder().build();
        FoodCard personCard = new FoodCard(food, 0);

        // same food, same index -> returns true
        FoodCard copy = new FoodCard(food, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different food, same index -> returns false
        Food differentFood = new FoodBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new FoodCard(differentFood, 0)));

        // same food, different index -> returns false
        assertFalse(personCard.equals(new FoodCard(food, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedFood} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(FoodCard personCard, Food expectedFood, int expectedId) {
        guiRobot.pauseForHuman();

        FoodCardHandle personCardHandle = new FoodCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify food details are displayed correctly
        assertCardDisplaysFood(expectedFood, personCardHandle);
    }
}
