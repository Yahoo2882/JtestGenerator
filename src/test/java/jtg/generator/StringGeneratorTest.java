package jtg.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringGeneratorTest {

    @Test
    void generate() {
        StringGenerator stringGenerator = new StringGenerator();
        int timesOfGeneration = 10;
        for (int i = 0; i < timesOfGeneration; ++i) {
            String randomString = stringGenerator.generate();
            System.out.println(randomString);
        }
    }

    @Test
    void generateReadableString() {
        StringGenerator stringGenerator = new StringGenerator();
        int timesOfGeneration = 10;
        for (int i = 0; i < timesOfGeneration; ++i) {
            String randomString = stringGenerator.generateReadableString(10);
            System.out.println(randomString);
        }
    }

}