package seedu.address.logic.orderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.food.Food;
import seedu.address.model.food.allergy.Allergy;

/**
 * Orders food in HackEat.
 */
public class FoodSelector {

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private String logBuffer = "";
    /**
     * Selects a {@code Food} based on the HackEat Algorithm
     * @param model the current model of the program
     * @return the index of the selected food
     */
    public Index select(Model model) {
        ArrayList<FoodScore> foodScores = generateFoodList(model);
        FoodScore fs = pickFood(foodScores);
        logBuffer += String.format("Selected Food: %s at index %s", fs.food.getName(), fs.index.getOneBased());
        printLog();
        return fs.index;
    }

    /**
     * Selects a food weighted randomly from a list of food with scores {@code FoodScore}
     * @param foodScores an ArrayList of {@code FoodScore}
     * @return the selected {@code FoodScore}
     */
    private FoodScore pickFood(ArrayList<FoodScore> foodScores) {

        float runningScore = 0;
        for (FoodScore foodScore : foodScores) {
            runningScore += foodScore.score;
            foodScore.runningScore = runningScore;
            logBuffer += String.format("Food: %3s, score: %3s, running score: %3s \n",
                    foodScore.food.getName(), foodScore.score, foodScore.runningScore);
        }

        float decidingNumber = (new Random()).nextFloat() * runningScore;
        logBuffer += String.format("Selected number: %s\n", decidingNumber);
        // Already sorted
        for (FoodScore foodScore : foodScores) {
            if (decidingNumber < foodScore.runningScore) {
                return foodScore;
            }
        }

        return foodScores.get(foodScores.size() - 1);
    }

    /**
     * Generates a list of food based on the model provided
     * @param model to be provided
     * @return a list of food
     */
    private ArrayList<FoodScore> generateFoodList(Model model) {
        ArrayList<FoodScore> foodScores = new ArrayList<>();

        List<Food> lastShownList = model.getFilteredFoodList();

        for (int i = 0; i < lastShownList.size(); i++) {
            FoodScore fs = new FoodScore(lastShownList.get(i), Index.fromZeroBased(i));
            fs.score = calculateScore(fs.food, model.getUserProfile().getAllergies());
            foodScores.add(fs);
        }
        return foodScores;
    }

    /**
     * Calculates a score based on some metric provided by the {@code Food} class
     * @param food The food that requires a score to be derived from
     * @param userAllergies a set of allergies of the user
     * @return the score for that particular food
     */
    private float calculateScore(Food food, Set<Allergy> userAllergies) {
        float score = 0;
        for (Allergy allergy : food.getAllergies()) {
            if (userAllergies.contains(allergy)) {
                return 0;
            }
        }

        score += Integer.parseInt(food.getRating().value);
        score /= 1.0 + Float.parseFloat(food.getPrice().getValue());

        return score;
    }

    private void printLog() {
        logBuffer = "\n" + logBuffer;
        logger.info(logBuffer);
        logBuffer = "";
    }

    /**
     * A private class that holds data for Food
     */
    class FoodScore {
        private Food food;
        private Index index;
        private float score;
        private float runningScore;

        FoodScore (Food food, Index index) {
            this.food = food;
            this.index = index;
        }
    }
}
