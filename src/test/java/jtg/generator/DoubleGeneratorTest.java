package jtg.generator;

import org.junit.jupiter.api.Test;
import polyglot.ast.Do;

import static org.junit.jupiter.api.Assertions.*;

class DoubleGeneratorTest {

    @Test
    void generate() {
        DoubleGenerator generator = new DoubleGenerator();
        for (int i = 0; i < 1000; ++i) {
            double num = generator.generate();
            if (num > 100) {
                System.out.println(num);
            }
        }
    }
}