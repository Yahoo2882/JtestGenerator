package jtg.generator;

public class FloatGenerator extends BasicTypeGenerator {

    @Override
    public Float generate() {
        return (float)randomFloatFromRange(Float.MIN_VALUE, Float.MAX_VALUE);
    }
}
