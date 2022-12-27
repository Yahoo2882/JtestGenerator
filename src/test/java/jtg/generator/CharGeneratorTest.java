package jtg.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharGeneratorTest {

    @Test
    void generate() {
//        System.out.print((char)65);
    }

    @Test
    void generateAlphaAndNumber() {
        CharGenerator charGenerator = new CharGenerator();
        int timesOfGeneration = 50;
        for (int i = 1; i <= timesOfGeneration; ++i) {
            char ch = charGenerator.generateAlphaAndNumber();
            System.out.print(ch + " ");
            if (i % 10 == 0) System.out.println();
        }
    }
}