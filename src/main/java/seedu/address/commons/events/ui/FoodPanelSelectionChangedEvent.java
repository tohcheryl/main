package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.FoodCard;

/**
 * Represents a selection change in the Food List Panel
 */
public class FoodPanelSelectionChangedEvent extends BaseEvent {


    private final FoodCard newSelection;

    public FoodPanelSelectionChangedEvent(FoodCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public FoodCard getNewSelection() {
        return newSelection;
    }
}
