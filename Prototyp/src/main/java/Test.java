import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("F", 4);
        String w = "F";
        if (map.containsKey('F')) {
            System.out.println("Any char matches a key");
        } else {
            System.out.println("No char matches a key");
        }
    }
}
