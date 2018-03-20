package seedu.address.storage;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.food.allergy.Allergy;

/**
 * JAXB-friendly adapted version of the Allergy.
 */
public class XmlAdaptedAllergy {

    @XmlValue
    private String allergyName;

    /**
     * Constructs an XmlAdaptedAllergy.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAllergy() {}

    /**
     * Constructs a {@code XmlAdaptedAllergy} with the given {@code allergyName}.
     */
    public XmlAdaptedAllergy(String allergyName) {
        this.allergyName = allergyName;
    }

    /**
     * Converts a given Allergy into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedAllergy(Allergy source) {
        allergyName = source.allergyName;
    }

    /**
     * Converts this jaxb-friendly adapted allergy object into the model's Allergy object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted user profile
     */
    public Allergy toModelType() throws IllegalValueException {
        if (!Allergy.isValidAllergyName(allergyName)) {
            throw new IllegalValueException(Allergy.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Allergy(allergyName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAllergy)) {
            return false;
        }

        return allergyName.equals(((XmlAdaptedAllergy) other).allergyName);
    }
}
