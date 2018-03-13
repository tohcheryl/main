package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_FOOD;
import static seedu.address.testutil.TypicalFoods.getTypicalFoods;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysFood;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.FoodCardHandle;
import guitests.guihandles.FoodListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.model.food.Food;

public class FoodListPanelTest extends GuiUnitTest {
    private static final ObservableList<Food> TYPICAL_FOODS =
            FXCollections.observableList(getTypicalFoods());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_FOOD);

    private FoodListPanelHandle personListPanelHandle;

    @Before
    public void setUp() {
        FoodListPanel personListPanel = new FoodListPanel(TYPICAL_FOODS);
        uiPartRule.setUiPart(personListPanel);

        personListPanelHandle = new FoodListPanelHandle(getChildNode(personListPanel.getRoot(),
                FoodListPanelHandle.FOOD_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_FOODS.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_FOODS.get(i));
            Food expectedFood = TYPICAL_FOODS.get(i);
            FoodCardHandle actualCard = personListPanelHandle.getFoodCardHandle(i);

            assertCardDisplaysFood(expectedFood, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        FoodCardHandle expectedCard = personListPanelHandle.getFoodCardHandle(INDEX_SECOND_FOOD.getZeroBased());
        FoodCardHandle selectedCard = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedCard, selectedCard);
    }
}
