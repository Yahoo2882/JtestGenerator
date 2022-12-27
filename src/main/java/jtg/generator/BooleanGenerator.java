package jtg.generator;

public class BooleanGenerator extends BasicTypeGenerator {

    @Override
    public Boolean generate() {
        return randomIntegerFromRange(0, 1) == 0;
    }
}
