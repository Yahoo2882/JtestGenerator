package jtg.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanGeneratorTest {

    @Test
    void generate() {
        BooleanGenerator booleanGenerator = new BooleanGenerator();
        double timesOfTrue = 0;
        double timesOfFalse = 0;
        int length = 100000;
        for (int i = 0; i < length; ++i) {
            boolean b = booleanGenerator.generate();
            if (b) {
                ++timesOfTrue;
            } else {
                ++timesOfFalse;
            }
        }
        System.out.println("true: " + (timesOfTrue / length) + "\nfalse: " + (timesOfFalse / length));
    }
}