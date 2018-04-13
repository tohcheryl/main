package seedu.address.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.tag.Tag;
import seedu.address.model.user.UserProfile;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Food[] getSampleFoods() {
        return new Food[] {
            new Food(new Name("Almond Biscuit"), new Phone("87438807"), new Email("almondhouse@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Price("$0.50"), new Rating("3"),
                    getTagSet("snack"), getAllergySet("peanut")),
            new Food(new Name("Banana Smoothie"), new Phone("99272758"), new Email("bananastand@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Price("$5"), new Rating("4"),
                    getTagSet("drink", "fruity"), getAllergySet("lactose")),
            new Food(new Name("Cheesecake"), new Phone("93210283"), new Email("cheesecakestore@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Price("$3"), new Rating("5"),
                    getTagSet("dessert"), getAllergySet("lactose")),
            new Food(new Name("Durian Milkshake"), new Phone("91031282"), new Email("durianmarket@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Price("$10"), new Rating("2"),
                    getTagSet("drink"), getAllergySet("lactose")),
            new Food(new Name("Ice cream"), new Phone("92492021"), new Email("icecreamshop@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new Price("$2.5"), new Rating("5"),
                    getTagSet("dessert"), getAllergySet("lactose")),
            new Food(new Name("Roti"), new Phone("92624417"), new Email("rotirestraunt@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Price("$0.8"), new Rating("3"),
                    getTagSet("snack"), getAllergySet("peanut")),
            new Food(new Name("Bak Kut Teh"), new Phone("67892304"), new Email("pepperysoup@pork.com"),
                    new Address("127 Kim Tian Road"), new Price("$6.8"), new Rating("3"),
                    getTagSet("meal"), getAllergySet()),
            new Food(new Name("Lontong"), new Phone("92332333"), new Email("nuruldelights@example.com"),
                    new Address("413 Bukit Batok West Ave 4"), new Price("$3.5"), new Rating("3"),
                    getTagSet("meal"), getAllergySet("lactose")),
            new Food(new Name("Thosai"), new Phone("93456023"), new Email("srivijaya@example.com"),
                    new Address("229 Selegie Road"), new Price("$0.8"), new Rating("5"),
                    getTagSet("meal"), getAllergySet()),
            new Food(new Name("Fishball Noodles"), new Phone("65342023"), new Email("meekia@tah.com"),
                    new Address("6 Jalan Bukit Merah, #01-121"), new Price("$3.5"), new Rating("3"),
                    getTagSet("meal"), getAllergySet()),
            new Food(new Name("Carrot cake"), new Phone("68349233"), new Email("chai@tow.kuay"),
                    new Address("#01-18, Bedok Interchange Food Center, 207 New Upper Changi Road"),
                    new Price("$3"), new Rating("3"),
                    getTagSet("oily"), getAllergySet("egg")),
            new Food(new Name("Nasi Briyani"), new Phone("63925247"), new Email("briyani@nasi.com"),
                    new Address("17 Beach Road, #01-4705"), new Price("$0.8"), new Rating("3"),
                    getTagSet("meal"), getAllergySet()),
            new Food(new Name("Beef Pho"), new Phone("60243423"), new Email("phoisdabez@beef.com"),
                    new Address("#01-02, 51 Telok Ayer Street"), new Price("$7"), new Rating("4"),
                    getTagSet("meal"), getAllergySet("peanut")),
            new Food(new Name("Seafood Paella"), new Phone("93493230"), new Email("seafood@paella.com"),
                    new Address("200 Turf Club Road"), new Price("$20"), new Rating("4"),
                    getTagSet("meal"), getAllergySet("seafood")),
            new Food(new Name("Beef Tacos"), new Phone("64756978"), new Email("beeftacos@example.com"),
                    new Address("11 Demsey Road"), new Price("$18"), new Rating("3"),
                    getTagSet("meal"), getAllergySet("gluten")),
            new Food(new Name("Chicken Wings"), new Phone("96670638"), new Email("twowings@example.com"),
                    new Address("1 Cantonment Road"), new Price("$3"), new Rating("3"),
                    getTagSet("snack"), getAllergySet()),
            new Food(new Name("Mapo Tofu"), new Phone("62213206"), new Email("chensmapotofu@example.com"),
                    new Address("6A Shenton Way #02-29, Downtown Gallery"), new Price("$8.0"), new Rating("3"),
                    getTagSet("Szechuan"), getAllergySet("soy")),
            new Food(new Name("Curry Puff"), new Phone("92624417"), new Email("tanglincurrypok@makan.com"),
                    new Address("531A Upper Cross Street, #02-36, Hong Lim Market & Food Centre"),
                    new Price("$1.5"), new Rating("3"),
                    getTagSet("snack"), getAllergySet("egg")),
            new Food(new Name("Salted Egg Chicken"), new Phone("67468884"), new Email("de@eatinghouse.com"),
                    new Address("469 Geylang Road"), new Price("$6"), new Rating("5"),
                    getTagSet("savoury"), getAllergySet("egg")),
            new Food(new Name("Portugese Egg Tart"), new Phone("62479363"), new Email("eggtart@madeleine.com"),
                    new Address("198 Tanjong Katong Rd"), new Price("$1.5"), new Rating("5"),
                    getTagSet("snack"), getAllergySet("egg"))

        };
    }

    public static UserProfile getSampleProfile() {
        return new UserProfile(new Name("Hacker"), new Phone("97002333"),
                new Address("Blk 71 One North MRT Station"), getAllergySet("lactose", "peanut", "cinnamon"));
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAb = new AddressBook();
            for (Food sampleFood : getSampleFoods()) {
                sampleAb.addFood(sampleFood);
            }
            return sampleAb;
        } catch (DuplicateFoodException e) {
            throw new AssertionError("sample data cannot contain duplicate foods", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

    /**
     * Returns an allergy set containing the list of strings given.
     */
    public static Set<Allergy> getAllergySet(String... strings) {
        HashSet<Allergy> allergies = new HashSet<>();
        for (String s : strings) {
            allergies.add(new Allergy(s));
        }

        return allergies;
    }
}
