import java.util.NoSuchElementException;

/**
 * Contains the main method and some test data for HashRelation
 *
 * @see HashRelation
 */
public class RelationTest {
    public static void main(String[] args) {

        //test data
        String[] country = new String[]{"FR", "DE", "IT", "FR", "BE", "BE", "NL", "UK", "IE", "IE", "DE"};
        String[] lang = new String[]{"French", "German", "Italy", "French", "French", "Flemish", "Dutch", "English", "English", "Irish", "English" };

        //desired load factor
        double desiredLoad = 0.75;

        //number of buckets to achieve desired load factor
        int m = (int)Math.round(((double) country.length )/ desiredLoad);

        Relation<String, String> r = new HashRelation<>(m);

        //enter the data into the relation
        for(int i = 0; i < country.length; i++){
            r.put(country[i], lang[i]);
        }

        //test cases
        System.out.println("----- Initial State -----");
        System.out.println(r);
        System.out.println("\n");

        System.out.println("----- 1. contains -----");
        System.out.println("Does the relation contain ('FR', 'French')?");
        System.out.println(r.contains("FR", "French"));
        System.out.println();
        System.out.println("Does the relation contain ('XX', 'The Moon')?");
        System.out.println(r.contains("XX", "The Moon"));
        System.out.println("\n");

        System.out.println("----- 2. getAllMatchingX -----");
        System.out.println("What matches are returned for X = 'BE'?");
        System.out.println(r.getAllMatchingX("BE"));
        System.out.println("What matches are returned for X = 'XX'?");
        System.out.println(r.getAllMatchingX("XX"));
        System.out.println("\n");

        System.out.println("----- 3. getAllMatchingY -----");
        System.out.println("What matches are returned for Y = 'French'?");
        System.out.println(r.getAllMatchingY("French"));
        System.out.println("What matches are returned for X = 'Swahili'?");
        System.out.println(r.getAllMatchingY("Swahili"));
        System.out.println("\n");

        System.out.println("----- 4. clear -----");
        System.out.println("What does the relation look like after calling clear()?");
        r.clear();
        System.out.println(r);
        System.out.println("\n");

        System.out.println("----- 5. put -----");
        System.out.println("Adding ('US', 'English') to the relation");
        r.put("US", "English");
        System.out.println(r);
        System.out.println();
        System.out.println("Adding ('US', 'English') to the relation (again!)");
        r.put("US", "English");
        System.out.println(r);
        System.out.println("Adding the rest of the test data back in");
        for(int i = 0; i < country.length; i++){
            r.put(country[i], lang[i]);
        }
        System.out.println(r);
        System.out.println("\n");

        System.out.println("----- 6. remove -----");
        System.out.println("Removing ('US', 'English') from the relation");
        r.remove("US", "English");
        System.out.println(r);
        System.out.println();
        System.out.println("Removing ('XX', 'The Moon') from the relation");
        try{
            r.remove("XX", "The Moon");
        } catch(NoSuchElementException e){
            System.out.println(e);
        }
        System.out.println("\n");

        System.out.println("----- 7. removeAllMatchingX -----");
        System.out.println("Removing anything with x = 'BE'");
        r.removeAllMatchingX("BE");
        System.out.println(r);
        System.out.println("Removing anything with x = 'XX' (nothing matches. nothing happens)");
        r.removeAllMatchingX("XX");
        System.out.println(r);

        System.out.println("----- 8. removeAllMatchingX -----");
        System.out.println("Removing anything with y = 'English'");
        r.removeAllMatchingY("English");
        System.out.println(r);
        System.out.println("Removing anything with y = 'Swahili' (nothing matches. nothing happens)");
        r.removeAllMatchingY("Swahili");
        System.out.println(r);
    }
}

