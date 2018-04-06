package seedu.address.logic.orderer;

import java.util.List;
import java.util.Random;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.food.Food;

/**
 * Orders food in HackEat.
 */
public class FoodSelector {

    /**
     * Selects a {@code Food} based on the HackEat Algorithm
     */
    public static Index select(Model model) {
        List<Food> lastShownList = model.getFilteredFoodList();
        int listSize = lastShownList.size();
        int randomIndex = (new Random()).nextInt(listSize);
        return Index.fromZeroBased(randomIndex);
    }
}
