package jtg.CoverageGenerator;

import jtg.generator.RadomValueGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class controllerTest {

    @Test
    void main() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "multipleIf";
        //CoverageGenerator coverageGenerator=new CoverageGenerator(clspath,clsName,methodName);
        // coverageGenerator.pathConstrainGenerator();
        //coverageGenerator.MCDCGenerator();
        RadomValueGenerator radomValueGenerator=new RadomValueGenerator(clspath,clsName,methodName);
        radomValueGenerator.Generator();
    }
}