package jtg.generator;

public class IntGenerator extends BasicTypeGenerator {

    @Override
    public Integer generate() {
        return (int)randomIntegerFromRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

}
