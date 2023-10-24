package unicash.model.category;

import static java.util.Objects.requireNonNull;
import static unicash.commons.util.AppUtil.checkArgument;

/**
 * Represents a Category in UnICash.
 * Guarantees: immutable; categoryName is valid as declared in {@link #isValidCategory(String)}
 */
public class Category {

    public static final String MESSAGE_CONSTRAINTS =
            "Category names should be alphanumeric and up to 15 characters long.";
    // Category can only be up to 15 characters long
    public static final String VALIDATION_REGEX = "\\p{Alnum}{1,15}$";

    public final String category;

    /**
     * Constructs a {@code Category}.
     *
     * @param category A valid category name.
     */
    public Category(String category) {
        requireNonNull(category);
        checkArgument(isValidCategory(category), MESSAGE_CONSTRAINTS);
        this.category = category;
    }

    /**
     * Returns true if a given string is a valid category.
     */
    public static boolean isValidCategory(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Category)) {
            return false;
        }

        Category otherCategory = (Category) other;
        return category.equals(otherCategory.category);
    }

    @Override
    public int hashCode() {
        return category.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return category;
    }
}