package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.food.UniqueFoodList;

public class UniqueFoodListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueFoodList uniqueFoodList = new UniqueFoodList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueFoodList.asObservableList().remove(0);
    }
}
