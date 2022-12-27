package jtg.generator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

enum Size { SMALL, MEDIUM, LARGE, VeryLarge }

class EnumerationGeneratorTest {

    @Test
    void generate() {
        EnumerationGenerator<Size> sizeEnumerationGenerator = new EnumerationGenerator<>(Size.class);

        int length = Size.values().length;

        int[] count = new int[length];
        for (int i = 0; i < length; ++i) {
            count[i] = 0;
        }

        int timesOfGeneration = 100000;
        for (int i = 0; i < timesOfGeneration; ++i) {
            ++count[sizeEnumerationGenerator.generate().ordinal()];
        }

        long sum = 0;
        for (int i = 0; i < length; ++i) {
            sum += count[i];
        }
        for (int i = 0; i < length; ++i) {
            System.out.println((double)count[i] / sum);
        }
    }
}