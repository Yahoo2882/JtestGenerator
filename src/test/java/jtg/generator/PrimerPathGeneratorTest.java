package jtg.generator;

import org.junit.jupiter.api.Test;

import java.io.File;

public class PrimerPathGeneratorTest {
    @Test
    public void classtest_1_test() throws Exception {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "ifElse";
        //String methodName = "simpleLoop";
        //String methodName = "sequence";
        PrimerPathGenerator sg = new PrimerPathGenerator(clspath, clsName, methodName);
        sg.generate();
    }
}
