package seedu.address.logic.orderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.food.Food;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.food.allergy.Allergy;

//@@author samzx

/**
 * Selects food in HackEat if the user has not specified a specific food to order.
 */
public class FoodSelector {
    private static final float SCORE_BUFFER = 0.001f;
    /**
     * Selects an {@code Index} from a model based on the HackEat Algorithm
     * @param model of the program
     * @return the index of the selected food
     */
    public Index selectIndex(Model model) throws CommandException {
        ArrayList<FoodDescriptor> foodDescriptors = buildFoodDescriptorList(model);
        FoodDescriptor foodDescriptor = pickFood(foodDescriptors);
        return foodDescriptor.index;
    }

    /**
     * Selects a food randomly with weighting from a list of food with scores {@code FoodDescriptor}
     * @param foodDescriptors an ArrayList of {@code FoodDescriptor}
     * @return the selected {@code FoodDescriptor}
     */
    private FoodDescriptor pickFood(ArrayList<FoodDescriptor> foodDescriptors) throws CommandException {

        float runningScore = 0;
        for (FoodDescriptor foodDescriptor : foodDescriptors) {
            runningScore += foodDescriptor.score;
            foodDescriptor.runningScore = runningScore;
        }

        float decidingNumber = (new Random()).nextFloat() * runningScore;

        for (FoodDescriptor foodDescriptor : foodDescriptors) {
            if (decidingNumber < foodDescriptor.runningScore) {
                return foodDescriptor;
            }
        }

        throw new CommandException(OrderCommand.MESSAGE_SELECT_FAIL);
    }

    /**
     * Generates a list of food based on the model provided
     * @param model to be provided
     * @return a list of food
     */
    private ArrayList<FoodDescriptor> buildFoodDescriptorList(Model model) {
        ArrayList<FoodDescriptor> foodDescriptors = new ArrayList<>();

        List<Food> lastShownList = model.getFilteredFoodList();

        for (int i = 0; i < lastShownList.size(); i++) {
            FoodDescriptor foodDescriptor = new FoodDescriptor(lastShownList.get(i), Index.fromZeroBased(i));
            foodDescriptor.score = calculateScore(foodDescriptor.food, model.getUserProfile().getAllergies());
            foodDescriptors.add(foodDescriptor);
        }
        return foodDescriptors;
    }

    /**
     * Calculates a score based on some metric provided by the {@code Food} class
     * @param food The food that requires a score to be derived from
     * @param userAllergies a set of allergies of the user
     * @return the score for that particular food
     */
    private float calculateScore(Food food, Set<Allergy> userAllergies) {
        float score;

        for (Allergy allergy : food.getAllergies()) {
            if (userAllergies.contains(allergy)) {
                return 0;
            }
        }

        score = 1;
        score *= scoreFromRating(food.getRating()) + SCORE_BUFFER;
        score *= scoreFromPrice(food.getPrice()) + SCORE_BUFFER;

        assert(score > 0);

        return score;
    }

    /**
     * Outputs a score based on the value of the price
     * For dampener variable:
     * -    dampener = 1, Roughly twice more likely to order a food of $5, than of value $10
     * -    dampener = 1.5, Roughly 50% more likely to order a food of $5, than of value $10
     * -    dampener = 2, Roughly 30% more likely to order a food of $5, than of value $10
     * @param price to have score derived from
     * @return score determined by algorithm
     */
    private float scoreFromPrice(Price price) {
        final float dampener = 1;

        float value = Float.parseFloat(price.getValue());

        return (float) Math.pow(1 / (value + 1), 1 / dampener);
    }

    /**
     * Outputs a score based on the value of the rating
     * For weighting variable:
     * -    weighting = 1, 5x more likely to order a food of rating 5, than of 1
     * -    weighting = 1.5, ~10x more likely to order a food of rating 5, than of 1
     * -    weighting = 2, 25x more likely to order a food of rating 5, than of 1
     * @param rating to have score derived from
     * @return score determined by algorithm
     */
    private float scoreFromRating(Rating rating) {
        final float weighting = 1;

        float value = Float.parseFloat(rating.value);

        return (float) Math.pow(value, weighting);
    }

    /**
     * Holds descriptions of the food for calculation purposes
     */
    class FoodDescriptor {
        private Food food;
        private Index index;
        private float score;
        private float runningScore;

        FoodDescriptor(Food food, Index index) {
            this.food = food;
            this.index = index;
        }
    }
}
