package jtg.generator;

import org.junit.jupiter.api.Test;

import java.io.File;

class RadomValueGeneratorTest {

    private RadomValueGenerator setUp(String mtdName){
        String clsPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.TestSet";
        return new RadomValueGenerator(clsPath,clsName,mtdName);
    }
    @Test
    void primitiveInt() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("primitiveInt");
        radomValueGenerator.Generator();
    }
    @Test
    void primitiveBoolean() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("primitiveBoolean");
        radomValueGenerator.Generator();
    }
    @Test
    void paramClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("paramClass");
        radomValueGenerator.Generator();
    }
    @Test
    void paramEnum() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("paramEnum");
        radomValueGenerator.Generator();
    }
    @Test
    void paramString() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("paramString");
        radomValueGenerator.Generator();
    }
    @Test
    void paramArray() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("paramArray");
        radomValueGenerator.Generator();
    }
    @Test
    void loopFor() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("loopFor");
        radomValueGenerator.Generator();
    }
    @Test
    void loopWhile() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("loopWhile");
        radomValueGenerator.Generator();
    }
    @Test
    void plusEqual() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RadomValueGenerator radomValueGenerator=setUp("plusEqual");
        radomValueGenerator.Generator();
    }
}