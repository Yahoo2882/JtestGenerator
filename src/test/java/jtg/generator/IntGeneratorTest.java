package jtg.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntGeneratorTest {

    @Test
    void generate() {
        IntGenerator intGenerator = new IntGenerator();
        int length = 100000;
        for (int i = 0; i < length; ++i) {
            Integer number = intGenerator.generate();
            assertTrue(Integer.MIN_VALUE <= number && number <= Integer.MAX_VALUE);
//            System.out.println(number);
        }
    }
}