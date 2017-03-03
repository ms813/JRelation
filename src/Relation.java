import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public interface Relation<X, Y> {
    boolean contains(X x, Y y);
    Set getX(X x);
    Set getY(Y y);
    void clear();
    void put(X x, Y y);
    void remove(X x, Y y);
    void removeAllWithX(X x);
    void removeAllWithY(Y y);
    String toString();
    void setHasherX(ToIntFunction<X> h);
    void setHasherY(ToIntFunction<Y> h);
    int size();
}
