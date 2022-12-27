package jtg.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorTest {

    @Test
    void randomFloatFromRange() {
        double low = -1e5;
        double high = 1e5;
        int length = 10000000;
        for (int i = 0; i < length; ++i) {
            double randomNumber = BasicTypeGenerator.randomFloatFromRange(low, high);
            assertTrue(low <= randomNumber && randomNumber <= high);
//            System.out.println(randomNumber);
        }
    }

    @Test
    void randomIntegerFromRange() {
        long low = -100000;
        long high = 100000;
        int length = 100000;
        for (int i = 0; i < length; ++i) {
            long randomNumber = BasicTypeGenerator.randomIntegerFromRange(low, high);
            assertTrue(low <= randomNumber && randomNumber <= high);
//            System.out.println(randomNumber);
        }
    }
}