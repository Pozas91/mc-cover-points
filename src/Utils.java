import java.util.Collection;
import java.util.Random;

public class Utils {

    public static Integer hammingDistance(final String a, final String b) {

        if(a == null || b == null) {
            throw new IllegalArgumentException("String must not be null");
        }

        if(a.length() != b.length()) {
            throw new IllegalArgumentException("String must have the same length");
        }

        int distance = 0;

        for(int i = 0; i < a.length(); i++) {
            if(a.charAt(i) != b.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }

    public static Object getRandomElementFromCollection(Collection from) {
        Random rnd = new Random();
        int i = rnd.nextInt(from.size());
        return from.toArray()[i];
    }

    public static Integer numberOfChars(String string, Character character) {

        Integer count = 0;

        for(int i = 0; i < string.length(); i++) {

            if(string.charAt(i) == character) {
                count++;
            }
        }

        return count;
    }
}
