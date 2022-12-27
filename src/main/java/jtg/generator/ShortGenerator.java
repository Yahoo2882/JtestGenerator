package jtg.generator;

public class ShortGenerator extends BasicTypeGenerator {

    @Override
    public Short generate() {
        return (short)randomIntegerFromRange(Short.MIN_VALUE, Short.MAX_VALUE);
    }
}
