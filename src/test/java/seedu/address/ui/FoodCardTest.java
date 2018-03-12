package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.model.person.Food;
import seedu.address.testutil.PersonBuilder;

public class FoodCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Food foodWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(foodWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, foodWithNoTags, 1);

        // with tags
        Food foodWithTags = new PersonBuilder().build();
        personCard = new PersonCard(foodWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, foodWithTags, 2);
    }

    @Test
    public void equals() {
        Food food = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(food, 0);

        // same food, same index -> returns true
        PersonCard copy = new PersonCard(food, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different food, same index -> returns false
        Food differentFood = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentFood, 0)));

        // same food, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(food, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedFood} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Food expectedFood, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify food details are displayed correctly
        assertCardDisplaysPerson(expectedFood, personCardHandle);
    }
}
