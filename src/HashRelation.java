import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.ToIntFunction;

public class HashRelation<X, Y> implements Relation<X, Y> {

    private Pair<X, Y>[] bucketsX;
    private Pair<Y, X>[] bucketsY;

    private ToIntFunction<X> hasherX;
    private ToIntFunction<Y> hasherY;

    private int bucketCount;

    private int size;

    public HashRelation(int bucketCount) {
        this.bucketCount = bucketCount;

        //instantiate new buckets
        clear();

        //set the default X and Y hash functions
        this.hasherX = X::hashCode;
        this.hasherY = Y::hashCode;
    }

    @Override
    public boolean contains(X x, Y y) {
        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx];

        while (curr != null) {
            if (x.equals(curr.x) && y.equals(curr.y)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public Set getX(X x) {
        int hx = hashX(x);
        Set<Y> set = new TreeSet<>();

        Pair<X, Y> curr = bucketsX[hx];

        while (curr != null) {
            if (x.equals(curr.x)) {
                set.add(curr.y);
            }
            curr = curr.next;
        }

        return set;
    }

    @Override
    public Set getY(Y y) {
        int hy = hashY(y);
        Set<X> set = new TreeSet<>();
        Pair<Y, X> curr = bucketsY[hy];

        while (curr != null) {
            if (y.equals(curr.x)) {
                set.add(curr.y);
            }
            curr = curr.next;
        }

        return set;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        bucketsX = (Pair<X, Y>[]) new Pair<?, ?>[bucketCount];
        bucketsY = (Pair<Y, X>[]) new Pair<?, ?>[bucketCount];
    }

    @Override
    public void put(X x, Y y) {

        int hx = hashX(x);

        //get the first pair in the bucket for hash(x)
        Pair<X, Y> curr = bucketsX[hx];

        while (curr != null) {

            /*
             Pair is already in the relation, so return immediately

             Check both x and y here in case there is a collision
             hash(x) == hash(x') for pairs P1<x, y> and P2<x', y>,
             ensuring P2<x', y> does not overwrite P1<x, y>
             */
            if (x.equals(curr.x) && y.equals(curr.y)) {
                return;
            }

            //traverse the linked list in bucket hash(x)
            curr = curr.next;
        }

        // if the pair wasn't found in the table for X, add it to both X and Y tables
        int hy = hashY(y);

        //add the new pair to the start of the linked list in buckets hash(x) and hash(y)
        bucketsX[hx] = new Pair<>(x, y, bucketsX[hx]);
        bucketsY[hy] = new Pair<>(y, x, bucketsY[hy]);
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void remove(X x, Y y) throws NoSuchElementException {

        int hx = hashX(x);
        Pair<X, Y> curr = bucketsX[hx], prev = null;

        boolean found = false;
        //search the X table for the pair
        while (curr != null) {
            if (x.equals(curr.x) && y.equals(curr.y)) {
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
            }
            prev = curr;
            curr = curr.next;
        }

        if (!found) {
            //if pair is not in the relation
            throw new NoSuchElementException();
        } else {
            //pair is in the relation, so remove from Y table
            int hy = hashY(y);
            Pair<Y, X> currY = bucketsY[hy], prevY = null;
            while (currY != null) {
                if (x.equals(currY.y) && y.equals(currY.x)) {
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

    @Override
    public void removeAllWithX(X x) {
        int hx = hashX(x);

        Pair<X, Y> curr = bucketsX[hx];
        while (curr != null) {
            if(x.equals(curr.x)){
                remove(curr.x, curr.y);
            }
            curr = curr.next;
        }
    }

    @Override
    public void removeAllWithY(Y y) {
        int hy = hashY(y);

        Pair<Y, X> curr = bucketsY[hy];
        while(curr != null){
            if(y.equals(curr.x)){
                remove(curr.y, curr.x);
            }
            curr = curr.next;
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("##### X #####\n");
        for (int i = 0; i < bucketCount; i++) {
            Pair<X, Y> p = bucketsX[i];
            while (p != null) {
                sb.append("Bucket ");
                sb.append(i);
                sb.append(", ");
                sb.append(p.toString());
                p = p.next;
            }
        }

        sb.append("\n##### Y #####\n");
        for (int i = 0; i < bucketCount; i++) {
            Pair<Y, X> p = bucketsY[i];
            while (p != null) {
                sb.append("Bucket ");
                sb.append(i);
                sb.append(", ");
                sb.append(p.toString());
                p = p.next;
            }
        }

        return sb.toString();
    }

    @Override
    public void setHasherX(ToIntFunction<X> h) {
        this.hasherX = h;
    }

    @Override
    public void setHasherY(ToIntFunction<Y> h) {
        this.hasherY = h;
    }

    private int hashX(X x) {
        return Math.abs(this.hasherX.applyAsInt(x) % bucketCount);
    }

    private int hashY(Y y) {
        return Math.abs(this.hasherY.applyAsInt(y) % bucketCount);
    }

    private static class Pair<X, Y> {
        X x;
        Y y;

        Pair<X, Y> next;

        private Pair(X x, Y y, Pair<X, Y> next) {
            this.x = x;
            this.y = y;
            this.next = next;
        }

        public String toString() {
            return "x: " + x.toString() + ", y: " + y.toString() + "\n";
        }
    }
}
