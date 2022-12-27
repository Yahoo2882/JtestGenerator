package jtg.generator;

public class ByteGenerator extends BasicTypeGenerator {

    @Override
    public Byte generate() {
        return (byte)randomIntegerFromRange(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }
}
