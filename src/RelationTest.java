
public class RelationTest {
    public static void main(String[] args) {

        String[] country = new String[]{"FR", "DE", "IT", "FR", "BE", "BE", "NL", "UK", "IE", "IE", "DE"};
        String[] lang = new String[]{"French", "German", "Italy", "French", "French", "Flemish", "Dutch", "English", "English", "Irish", "English" };

        double desiredLoad = 0.75;
        int n = country.length;

        int m = (int)Math.round(((double) n )/ desiredLoad);

        System.out.println("Bucket count: " + m);
        Relation<String, String> r = new HashRelation<>(m);

        r.setHasherX(String::length);
        r.setHasherY(y -> (int)y.charAt(0));

        for(int i = 0; i < country.length; i++){
            r.put(country[i], lang[i]);
        }
    }
}

