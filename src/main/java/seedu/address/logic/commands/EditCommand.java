package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOODS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.food.exceptions.FoodNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing food in HackEat.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the food identified "
            + "by the index number used in the last food listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_FOOD_SUCCESS = "Edited Food: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_FOOD = "This food already exists in HackEat.";

    private final Index index;
    private final EditFoodDescriptor editFoodDescriptor;

    private Food foodToEdit;
    private Food editedFood;

    /**
     * @param index of the food in the filtered food list to edit
     * @param editFoodDescriptor details to edit the food with
     */
    public EditCommand(Index index, EditFoodDescriptor editFoodDescriptor) {
        requireNonNull(index);
        requireNonNull(editFoodDescriptor);

        this.index = index;
        this.editFoodDescriptor = new EditFoodDescriptor(editFoodDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateFood(foodToEdit, editedFood);
        } catch (DuplicateFoodException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_FOOD);
        } catch (FoodNotFoundException pnfe) {
            throw new AssertionError("The target food cannot be missing");
        }
        model.updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
        return new CommandResult(String.format(MESSAGE_EDIT_FOOD_SUCCESS, editedFood));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Food> lastShownList = model.getFilteredFoodList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        foodToEdit = lastShownList.get(index.getZeroBased());
        editedFood = createEditedFood(foodToEdit, editFoodDescriptor);
    }

    /**
     * Creates and returns a {@code Food} with the details of {@code foodToEdit}
     * edited with {@code editFoodDescriptor}.
     */
    private static Food createEditedFood(Food foodToEdit, EditFoodDescriptor editFoodDescriptor) {
        assert foodToEdit != null;

        Name updatedName = editFoodDescriptor.getName().orElse(foodToEdit.getName());
        Phone updatedPhone = editFoodDescriptor.getPhone().orElse(foodToEdit.getPhone());
        Email updatedEmail = editFoodDescriptor.getEmail().orElse(foodToEdit.getEmail());
        Address updatedAddress = editFoodDescriptor.getAddress().orElse(foodToEdit.getAddress());
        Set<Tag> updatedTags = editFoodDescriptor.getTags().orElse(foodToEdit.getTags());

        return new Food(updatedName, updatedPhone, updatedEmail, updatedAddress, , updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editFoodDescriptor.equals(e.editFoodDescriptor)
                && Objects.equals(foodToEdit, e.foodToEdit);
    }

    /**
     * Stores the details to edit the food with. Each non-empty field value will replace the
     * corresponding field value of the food.
     */
    public static class EditFoodDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditFoodDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditFoodDescriptor(EditFoodDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.phone, this.email, this.address, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditFoodDescriptor)) {
                return false;
            }

            // state check
            EditFoodDescriptor e = (EditFoodDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}
