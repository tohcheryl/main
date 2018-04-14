package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalFoods.ALMOND;
import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.food.Food;
import seedu.address.model.tag.Tag;
import seedu.address.model.user.UserProfile;
import seedu.address.model.util.SampleDataUtil;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getFoodList());
        assertEquals(Collections.emptyList(), addressBook.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateFoods_throwsAssertionError() {
        // Repeat ALMOND twice
        List<Food> newFoods = Arrays.asList(ALMOND, ALMOND);
        List<Tag> newTags = new ArrayList<>(ALMOND.getTags());
        AddressBookStub newData = new AddressBookStub(newFoods, newTags);

        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }

    @Test
    public void getFoodList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getFoodList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getTagList().remove(0);
    }

    /**
     * A stub ReadOnlyAddressBook whose foods and tags lists can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Food> foods = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();
        private UserProfile profile;

        AddressBookStub(Collection<Food> foods, Collection<? extends Tag> tags) {
            this.profile = SampleDataUtil.getSampleProfile();
            this.foods.setAll(foods);
            this.tags.setAll(tags);
        }

        @Override
        public ObservableList<Food> getFoodList() {
            return foods;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }

        //@@author tohcheryl
        @Override
        public UserProfile getUserProfile() {
            return profile;
        }
    }

}
