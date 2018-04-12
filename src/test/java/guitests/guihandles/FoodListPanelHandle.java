package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;
import seedu.address.model.food.Food;
import seedu.address.ui.FoodCard;

/**
 * Provides a handle for {@code FoodListPanel} containing the list of {@code FoodCard}.
 */
public class FoodListPanelHandle extends NodeHandle<ListView<FoodCard>> {
    public static final String FOOD_LIST_VIEW_ID = "#foodListView";

    private Optional<FoodCard> lastRememberedSelectedFoodCard;

    public FoodListPanelHandle(ListView<FoodCard> personListPanelNode) {
        super(personListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code FoodCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public FoodCardHandle getHandleToSelectedCard() {
        List<FoodCard> personList = getRootNode().getSelectionModel().getSelectedItems();

        if (personList.size() != 1) {
            throw new AssertionError("Food list size expected 1.");
        }

        return new FoodCardHandle(personList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<FoodCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and selectIndex the food.
     */
    public void navigateToCard(Food food) {
        List<FoodCard> cards = getRootNode().getItems();
        Optional<FoodCard> matchingCard = cards.stream().filter(card -> card.food.equals(food)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("Food does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the food card handle of a food associated with the {@code index} in the list.
     */
    public FoodCardHandle getFoodCardHandle(int index) {
        return getFoodCardHandle(getRootNode().getItems().get(index).food);
    }

    /**
     * Returns the {@code FoodCardHandle} of the specified {@code food} in the list.
     */
    public FoodCardHandle getFoodCardHandle(Food food) {
        Optional<FoodCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.food.equals(food))
                .map(card -> new FoodCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Food does not exist."));
    }

    /**
     * Selects the {@code FoodCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code FoodCard} in the list.
     */
    public void rememberSelectedFoodCard() {
        List<FoodCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedFoodCard = Optional.empty();
        } else {
            lastRememberedSelectedFoodCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code FoodCard} is different from the value remembered by the most recent
     * {@code rememberSelectedFoodCard()} call.
     */
    public boolean isSelectedFoodCardChanged() {
        List<FoodCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedFoodCard.isPresent();
        } else {
            return !lastRememberedSelectedFoodCard.isPresent()
                    || !lastRememberedSelectedFoodCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
