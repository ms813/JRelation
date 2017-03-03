import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * A hash table representation of a Relation
 * <p>
 * This implementation uses two hash tables to store pairs twice, as (x, y) and (y, x),
 * allowing for fast lookup of either x or y.
 * This comes with the overhead of increased memory requirements and increased complexity
 * when adding and removing elements from both tables.
 * <p>
 * Each bucket in the hash tables is stored as a linked list
 *
 * @param <X> Type of the first item in a pair
 * @param <Y> Type of the second item in a pair
 * @author Matthew Smith
 */
public class HashRelation<X, Y> implements Relation<X, Y> {

    /**
     * Hash table X: Array containing (x, y) pairs, using x as the key
     * This array is used as the primary lookup table when adding or removing elements
     */
    private Pair<X, Y>[] bucketsX;


    /**
     * Hash Table Y: Array containing (x, y) pairs, using y as the key
     */
    private Pair<Y, X>[] bucketsY;


    /**
     * Number of buckets in each of the x and y hash tables
     */
    private int bucketCount;


    /**
     * Number of items currently stored in one of the hash tables
     * <p>
     * Note that this number should always be the same for the x and the y tables
     */
    private int size;


    /**
     * Constructs a HashRelation with a specified number of buckets in its X and Y hash tables
     *
     * @param bucketCount number of buckets in the X and Y hash tables
     */
    public HashRelation(int bucketCount) {
        this.bucketCount = bucketCount;

        //instantiate new buckets
        clear();
    }


    /**
     * Requirement 1
     * Returns true if the relation contains the specified pair (x, y)
     * Hashes x, looks up the bucket hash(x) in the hash table,
     * then traverses the linked list in the bucket
     * <p>
     * Complexity:
     * Best case: O(1) (1 item in bucket)
     * Worst case: O(n) (all items in same bucket, specified item is at the end of the linked list)
     *
     * @param x the first part of the pair to be matched
     * @param y the second part of the pair to be matched
     * @return true if a pair matching (x, y) is found, false otherwise
     */
    @Override
    public boolean contains(X x, Y y) {
        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx];

        //traverse the linked list until the item is found or the end of the list is reached
        while (curr != null) {
            if (x.equals(curr.k) && y.equals(curr.v)) {
                //pair is found
                return true;
            }
            curr = curr.next;
        }

        //pair is not found
        return false;
    }


    /**
     * Requirement 2
     * Given x, returns a set containing all values y such that the relation contains (x, y)
     * <p>
     * Hashes x, looks up the bucket hash(x) in the X hash table,
     * then traverses the linked list in the bucket, adding matching y elements to a set
     * <p>
     * Complexity:
     * Best case: O(1) (one item matches x)
     * Worst case: O(n) (all items match x, hence are in same bucket)
     *
     * @param x the element to search for in the first half of a pair
     * @return a set containing all values y such that the relation contains (x, y)
     */
    @Override
    public Set<Y> getAllMatchingX(X x) {
        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx];

        //create an empty set to store matched Y elements
        Set<Y> set = new TreeSet<>();

        //traverse the bucket, adding all matching elements to the set
        while (curr != null) {
            if (x.equals(curr.k)) {
                set.add(curr.v);
            }
            curr = curr.next;
        }

        return set;
    }


    /**
     * Requirement 3
     * Given y, returns a set containing all values x such that the relation contains (x, y)
     * <p>
     * Hashes y, looks up the bucket hash(y) in the Y hash table,
     * then traverses the linked list in the bucket, adding matching x elements to a set
     * <p>
     * Complexity:
     * Best case: O(1) (one item matches y)
     * Worst case: O(n) (all items match y, hence are in same bucket)
     *
     * @param y the element to search for in the second half of a pair
     * @return a set containing all values k such that the relation contains (x, y)
     */
    @Override
    public Set<X> getAllMatchingY(Y y) {
        int hy = hashY(y);
        Pair<Y, X> curr = bucketsY[hy];

        //create an empty set to store matched Y elements
        Set<X> set = new TreeSet<>();

        //traverse the bucket, adding all matching elements to the set
        while (curr != null) {
            if (y.equals(curr.k)) {
                set.add(curr.v);
            }
            curr = curr.next;
        }

        return set;
    }

    /**
     * Requirement 4
     * Empties the relation by creating new instances of the X and Y pair arrays
     * <p>
     * Complexity:
     * O(1)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        bucketsX = (Pair<X, Y>[]) new Pair<?, ?>[bucketCount];
        bucketsY = (Pair<Y, X>[]) new Pair<?, ?>[bucketCount];
    }


    /**
     * Requirement 5
     * Adds a given pair (x, y) to the relation
     * <p>
     * Only need to check if element exists in the X table
     * If it exists in the X table, no need to check the Y table
     * If it does not exist in the X table, go ahead and add it to both tables without
     * checking if it exists in the Y table
     * <p>
     * Best case: O(1) (nothing in target bucket)
     * Worst case: O(n) (all items in same bucket, have to traverse full tree to check if pair exists)
     *
     * @param x the first half of the pair to be added
     * @param y the second half of the pair to be added
     */
    @Override
    public void put(X x, Y y) {

        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx];

        //traverse the bucket, adding all matching elements to the set
        while (curr != null) {
            /*
             Pair is already in the relation, so return immediately without doing anything

             Check both x and y here in case there is a collision
             hash(x) == hash(x') for pairs P1<x, y> and P2<x', y>,
             ensuring P2<x', y> does not overwrite P1<x, y>
             */
            if (x.equals(curr.k) && y.equals(curr.v)) {
                return;
            }

            //traverse the linked list in bucket hash(k)
            curr = curr.next;
        }

        // if the pair wasn't found in the table for X, add it to both X and Y tables
        int hy = hashY(y);

        //add the new pair to the start of the linked list in the appropriate bucket in the X and Y tables
        bucketsX[hx] = new Pair<>(x, y, bucketsX[hx]);
        bucketsY[hy] = new Pair<>(y, x, bucketsY[hy]);
        size++;
    }


    /**
     * Get the number of pairs in the relation
     * <p>
     * Complexity:
     * O(1)
     *
     * @return the number of items in the X table (size of X and Y tables are the same)
     */
    @Override
    public int size() {
        return size;
    }


    /**
     * Requirement 6
     * Removes a given pair (x, y) to the relation
     * <p>
     * Check the X table first. If the element is found remove it from both tables
     * If the element is not found in the X table, no need to check the Y table
     * <p>
     * Complexity:
     * Best case: O(1) (only one item in bucket)
     * Worst case: O(n) (every item in same bucket)
     *
     * @param x the first half of the pair to be removed
     * @param y the second half of the pair to be removed
     * @throws NoSuchElementException when trying to remove an element that isn't in the relation
     */
    @Override
    public void remove(X x, Y y) throws NoSuchElementException {

        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx], prev = null;

        //flag indicating if pair to be removed has been found
        boolean found = false;

        //traverse the linked list in the appropriate bucket of the X table for the pair
        while (curr != null) {
            if (x.equals(curr.k) && y.equals(curr.v)) {
                //item was found, so delete it
                if (prev == null) {
                    //item to delete is first item in linked list
                    bucketsX[hx] = curr.next;
                } else {
                    //item is not the first in the list
                    prev.next = curr.next;
                }
                size--;
                found = true;

                //stop searching once specified element is deleted
                break;
            }
            prev = curr;
            curr = curr.next;
        }

        if (!found) {
            //if pair is not in the relation, throw an exception
            //no need to check the Y table
            throw new NoSuchElementException();
        } else {
            //pair was found and removed from the X table, so remove from Y table as well
            int hy = hashY(y);
            Pair<Y, X> currY = bucketsY[hy], prevY = null;

            //traverse the linked list in the appropriate bucket of the Y table for the pair
            while (currY != null) {
                if (x.equals(currY.v) && y.equals(currY.k)) {
                    if (prevY == null) {
                        //item to delete is first item in linked list
                        bucketsY[hy] = currY.next;
                    } else {
                        //item is not the first in the list
                        prevY.next = currY.next;
                    }
                }
                prevY = currY;
                currY = currY.next;
            }
        }
    }

    /**
     * Requirement 7
     * Given x, removes all pairs(x, y) from the relation
     * <p>
     * Find the bucket containing pairs with x, and removes all of them matching x
     * <p>
     * Complexity:
     * Best case: O(1) (one item in bucket)
     * Worst case: O(n) (all items in same bucket)
     *
     * @param x the first part of all pairs to be removed
     */
    @Override
    public void removeAllMatchingX(X x) {

        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx];

        while (curr != null) {
            //check x matches, so as to not delete items with x' hashed to same bucket
            if (x.equals(curr.k)) {
                remove(curr.k, curr.v);
            }
            curr = curr.next;
        }
    }


    /**
     * Requirement 8
     * Given y, removes all pairs(x, y) from the relation
     * <p>
     * Find the bucket containing pairs with y, and removes all of them matching y
     * <p>
     * Complexity:
     * Best case: O(1) (one item in bucket)
     * Worst case: O(n) (all items in same bucket)
     *
     * @param y the second part of all pairs to be removed
     */
    @Override
    public void removeAllMatchingY(Y y) {
        int hy = hashY(y);

        Pair<Y, X> curr = bucketsY[hy];
        while (curr != null) {
            //check y matches, so as to not delete items with y' hashed to same bucket
            if (y.equals(curr.k)) {
                remove(curr.v, curr.k);
            }
            curr = curr.next;
        }
    }

    /**
     * Requirement 9
     * Renders the relations contents as a string in a readable format
     * <p>
     * Prints each bucket in the hash table (even if empty), and each linked list in each bucket
     * <p>
     * Complexity:
     * O(n) (has to traverse every item in the relation)
     *
     * @return a string representation of the relation
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("\n-- X table --\n");
        for (int i = 0; i < bucketCount; i++) {
            Pair<X, Y> p = bucketsX[i];

            sb.append("Bucket ");
            sb.append(i);
            sb.append(": [");

            while (p != null) {
                sb.append(p.toString());
                sb.append(", ");
                p = p.next;
            }

            sb.append("]\n");
        }

        sb.append("\n-- Y table --\n");
        for (int i = 0; i < bucketCount; i++) {
            Pair<Y, X> p = bucketsY[i];

            sb.append("Bucket ");
            sb.append(i);
            sb.append(": [");

            while (p != null) {
                sb.append(p.toString());
                sb.append(", ");
                p = p.next;
            }

            sb.append("]\n");
        }

        return sb.toString();
    }

    /**
     * The hash function for x
     *
     * @param x the element to be hashed
     * @return the hashed representation of x
     */
    private int hashX(X x) {
        return Math.abs(x.hashCode() % bucketCount);
    }

    /**
     * The hash function for y
     *
     * @param y the element to be hashed
     * @return the hashed representation of y
     */
    private int hashY(Y y) {
        return Math.abs(y.hashCode() % bucketCount);
    }

    /**
     * Static nested class representing the (x, y) pairs
     *
     * @param <X> Type of the first item in the pair
     * @param <Y> Type of the second item in the pair
     */
    private static class Pair<X, Y> {
        /**
         * The first item in the pair
         */
        X k;

        /**
         * The second item in the pair
         */
        Y v;

        /**
         * The next item in the linked list
         */
        Pair<X, Y> next;

        /**
         * Constructs a pair with the specified elements
         *
         * @param k    the first item in the pair
         * @param v    the second item in the pair
         * @param next the next item in the linked list
         */
        private Pair(X k, Y v, Pair<X, Y> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

        /**
         * Returns a string representation of the pair
         *
         * @return a string representation of the pair
         */
        public String toString() {
            return "(" + k.toString() + ", " + v.toString() + ")";
        }
    }
}
