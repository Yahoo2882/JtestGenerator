package jtg.generator;

import org.junit.jupiter.api.Test;
import jtg.generator.ClassGenerator;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Int {

    public int num;

    public Int(int n) {
        num = n;
    }
}

class Char {

    public char ch;

    public Char(char c) { ch = c; }
}

class IntInClass {
    public Int num;

    public IntInClass(Int n) { num = n; }
}

class MultParam {

    public int num;
    public IntInClass[] nums;
    public double[] doubles;

    public MultParam(int n, IntInClass[] inNums, double[] ds) {
        num = n;
        nums = inNums;
        doubles = ds;
    }
}

class ClassGeneratorTest {

    @Test
    void testJavaAPI() {
        ClassGenerator<Int> intClassGenerator = new ClassGenerator<>(Int.class);
//        intClassGenerator.generate(null);
        System.out.println(Arrays.toString(Arrays.stream(Int.class.getConstructors()).toArray()));
    }

    @Test
    void testJavaAPI2() {
        Array.newInstance(double.class, 5);
    }

    @Test
    void testIntClass() {
        ClassGenerator<Int> intClassGenerator = new ClassGenerator<>(Int.class);
        Int int1 = intClassGenerator.generate(null);
        System.out.println(int1.num);
    }

    @Test
    void testCharClass() {
        ClassGenerator<Char> charClassGenerator = new ClassGenerator<>(Char.class);
        Char char1 = charClassGenerator.generate(null);
        System.out.println(char1.ch);
    }

    @Test
    void testClassInClass() {
        ClassGenerator<IntInClass> classGenerator = new ClassGenerator<>(IntInClass.class);
        IntInClass c = classGenerator.generate(null);
        System.out.println(c.num.num);
    }

    @Test
    void testMultiParams() {
        ClassGenerator<MultParam> classGenerator = new ClassGenerator<>(MultParam.class);
        MultParam c = classGenerator.generate(null);
        System.out.println(c.num);
        for (IntInClass intInClass : c.nums) {
            System.out.println(intInClass.num.num);
        }
        System.out.println(Arrays.toString(Arrays.stream(c.doubles).toArray()));
    }

    @Test
    void testObjectCastToPrimitive() {
        //int[] arr = new Integer[10];
    }
}