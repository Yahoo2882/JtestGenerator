package cut;

public class CallClass {
    private int num;

    public CallClass() {
        num = 10;
    }

    int num() {
        return num;
    }

    int add(int n) {
        num += n;
        return num;
    }
}
