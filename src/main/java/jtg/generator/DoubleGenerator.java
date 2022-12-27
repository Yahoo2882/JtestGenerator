package jtg.generator;

public class DoubleGenerator extends BasicTypeGenerator {

    @Override
    public Double generate() {
        return randomFloatFromRange(Double.MIN_VALUE, Double.MAX_VALUE);
    }
}
