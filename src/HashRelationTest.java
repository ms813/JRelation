import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class HashRelationTest {

    private Relation<String, String> r;
    private double desiredLoad = 0.75;
    private String[] a = {"j", "j", "c", "c", "c", "p", "n", "x", "x", "x"};
    private String[] b = {"java", "javascript", "c++", "c#", "c", "python", "node", "java", "node", "c"};

    @BeforeEach
    void setUp(){
        int n = a.length;
        int m = (int)Math.round(((double) n )/ desiredLoad);

        r = new HashRelation<>(m);
        
        for(int i = 0; i < a.length; i++){
            r.put(a[i], b[i]);
            r.put(Integer.toString(i), Integer.toString(i*i));
        }
    }

    @Test
    void contains() {
        assertTrue(r.contains("j", "java"), "Contains j, java");
        assertFalse(r.contains("r", "ruby"), "Does not contain r, ruby");
    }

    @Test
    void getX() {
        Set<String> s = new TreeSet<>();
        s.add("java");
        assertNotEquals(s, r.getX("j"), "getX set not equal too few");

        s.add("javascript");
        assertEquals(s, r.getX("j"), "getX set equal");

        s.add("junit");
        assertNotEquals(s, r.getX("j"), "getX set not equal too many");
    }

    @Test
    void getY() {
        Set<String> s = new TreeSet<>();
        s.add("j");
        assertNotEquals(s, r.getY("java"), "getY set not equal too few");

        s.add("x");
        assertEquals(s, r.getY("java"), "getY set equal");

        s.add("z");
        assertNotEquals(s, r.getY("java"), "getY set not equal too many");
    }

    @Test
    void clear() {
        r.clear();
        assertEquals(r.size(), 0, "Size empty");
        assertFalse(r.contains("j", "java"), "Clear doesn't contain anything");
    }

    @Test
    void remove() {
        assertTrue(r.contains("j", "java"), "Remove before");

        r.remove("j", "java");
        assertFalse(r.contains("j", "java"), "Remove after");

        assertThrows(NoSuchElementException.class, () -> r.remove("not in the relation", "?"));
    }

    @Test
    void removeAllWithX() {
        assertTrue(r.contains("j", "java"), "RemoveAllWithX before");
        assertTrue(r.contains("j", "javascript"), "RemoveAllWithX before 2");
        assertTrue(r.contains("c", "c"), "RemoveAllWithX before 3");

        r.removeAllWithX("j");
        assertFalse(r.contains("j", "java"), "RemoveAllWithX after");
        assertFalse(r.contains("j", "javascript"), "RemoveAllWithX after 2");
        assertTrue(r.contains("c", "c"), "RemoveAllWithX after 3");
    }

    @Test
    void removeAllWithY() {
        assertTrue(r.contains("j", "java"), "RemoveAllWithY before");
        assertTrue(r.contains("x", "java"), "RemoveAllWithY before 2");
        assertTrue(r.contains("c", "c"), "RemoveAllWithY before 3");

        r.removeAllWithY("java");
        assertFalse(r.contains("j", "java"), "RemoveAllWithY before");
        assertFalse(r.contains("x", "java"), "RemoveAllWithY before 2");
        assertTrue(r.contains("c", "c"), "RemoveAllWithY after 3");
    }

    @Test
    void size(){
        assertEquals(r.size(), a.length * 2, "Size");

        r.put("g", "groovy");
        assertEquals(r.size(), a.length * 2 + 1, "Size + 1");

        r.remove("g", "groovy");
        assertEquals(r.size(), a.length * 2, "Size - 1");
    }
}
