import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A collection of pairs (x, y), that contains no duplicate pairs
 *
 * @param <X> the type of the first half of the pair
 * @param <Y> the type of the second half of the pair
 *
 * @author  Matthew Smith
 */
public interface Relation<X, Y> {
    /**
     * Requirement 1
     * Returns true if the relation contains a pair matching both x and y
     *
     * @param x the first part of the pair to be matched
     * @param y the second part of the pair to be matched
     * @return true if a pair matching (x, y) is found, false otherwise
     */
    boolean contains(X x, Y y);

    /**
     * Requirement 2
     * Given x, returns a set containing all values y such that the relation contains (x, y)
     *
     * If no pairs matching x are found, returns an empty set
     *
     * @param x the element to search for in the first half of a pair
     * @return a set containing all values y such that the relation contains (x, y)
     */
    Set<Y> getAllMatchingX(X x);

    /**
     * Requirement 3
     * Given y, returns a set containing all values x such that the relation contains (x, y)
     *
     * If no pairs matching y are found, returns an empty set
     *
     * @param y the element to search for in the second half of a pair
     * @return a set containing all values x such that the relation contains (x, y)
     */
    Set<X> getAllMatchingY(Y y);

    /**
     * Requirement 4
     * Empties the relation
     */
    void clear();

    /**
     * Requirement 5
     * Adds a given pair (x, y) to the relation
     *
     * @param x the first half of the pair to be added
     * @param y the second half of the pair to be added
     */
    void put(X x, Y y);

    /**
     * Requirement 6
     * Removes a given pair (x, y) to the relation
     *
     * @param x the first half of the pair to be removed
     * @param y the second half of the pair to be removed
     * @throws NoSuchElementException when trying to remove an element that isn't in the relation
     */
    void remove(X x, Y y) throws NoSuchElementException;

    /**
     * Requirement 7
     * Given x, removes all pairs(x, y) from the relation
     *
     * @param x the first part of all pairs to be removed
     */
    void removeAllMatchingX(X x);

    /**
     * Requirement 8
     * Given y, removes all pairs(x, y) from the relation
     *
     * @param y the second part of all pairs to be removed
     */
    void removeAllMatchingY(Y y);

    /**
     * Requirement 9
     * Renders the relations contents as a string in a readable format
     * 
     * @return a string representation of the relation
     */
    String toString();

    /**
     * Returns the number of pairs stored in the relation
     *
     * @return the number of pairs stored in the relation
     */
    int size();
}
