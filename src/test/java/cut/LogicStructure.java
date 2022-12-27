package cut;

import java.lang.reflect.Array;

public class LogicStructure {

    public int sequence(int a, int b) {
        return a + b;
    }

    public boolean soloIf(Integer[] e) {
        if (e.length > 0)
            return true;
        return false;
    }

    public boolean ifElse(int op) {
        if (op > 18)
            return true;
        else
            return false;
    }

    public String multipleIf(int op) {
        if (op % 15 == 0)
            return "Tom";
        else if (op % 5 == 0)
            return "Buzz";
        else if (op % 3 == 0)
            return "Fuze";
        else
            return Integer.toString(op);
    }
}
