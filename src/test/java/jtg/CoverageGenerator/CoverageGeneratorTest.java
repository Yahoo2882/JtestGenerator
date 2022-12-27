package jtg.CoverageGenerator;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CoverageGeneratorTest {

    private CoverageGenerator setUp(String mtdName) throws Exception {
        String clsPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.TestSet";
        return new CoverageGenerator(clsPath,clsName,mtdName);
    }
    @Test
    void primitiveInt() throws Exception {
        CoverageGenerator coverageGenerator=setUp("primitiveInt");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void primitiveBoolean() throws Exception {
        CoverageGenerator coverageGenerator=setUp("primitiveBoolean");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void paramClass() throws Exception {
        CoverageGenerator coverageGenerator=setUp("paramClass");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void paramEnum() throws Exception {
        CoverageGenerator coverageGenerator=setUp("paramEnum");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void paramString() throws Exception {
        CoverageGenerator coverageGenerator=setUp("paramString");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void loopFor() throws Exception {
        CoverageGenerator coverageGenerator=setUp("loopFor");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void loopWhile() throws Exception {
        CoverageGenerator coverageGenerator=setUp("loopWhile");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void multipleIf() throws Exception {
        CoverageGenerator coverageGenerator=setUp("multipleIf");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void plusEqual() throws Exception {
        CoverageGenerator coverageGenerator=setUp("plusEqual");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void unReachable() throws Exception {
        CoverageGenerator coverageGenerator=setUp("unReachable");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
    }
    @Test
    void lackCondition() throws Exception {
        CoverageGenerator coverageGenerator=setUp("lackCondition");
        System.out.println("===================================   PrimerPath   ===================================");
        coverageGenerator.pathConstrainGenerator();
        System.out.println("===================================   MCDCGenerator   ===================================");
        coverageGenerator.MCDCGenerator();
    }
}