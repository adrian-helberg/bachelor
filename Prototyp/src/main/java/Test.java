import java.util.Arrays;
import java.util.stream.Collectors;

public class Test<T> {
    T child;

    Test(T c) {
        child = c;
    }

    @Override
    public String toString() {
        if (child instanceof Integer[]) {
            return Arrays.stream(((Integer[]) child)).map(Object::toString).collect(Collectors.joining(", "));
        }
        return "Test{" + "child=" + child + '}';
    }

    public static void main(String[] args) {
        Integer[] test = new Integer[2];
        test[0] = 1;
        test[1] = 3;

        Test<Integer[]> a = new Test<>(test);

        System.out.println(a);
    }
}
