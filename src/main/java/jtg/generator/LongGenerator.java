package jtg.generator;

public class LongGenerator extends BasicTypeGenerator {

    @Override
    public Long generate() {
        return randomIntegerFromRange(Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
