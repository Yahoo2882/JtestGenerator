package jtg.generator;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class Numbers {
    public int num1;
    public int num2;
    public Numbers(int n1, int n2) {
        num1 = n1;
        num2 = n2;
    }
}

class ArrayGeneratorTest {

    enum Size { SMALL, MIDDLE, LARGE };

    @Test
    void testGenerate() {
        ArrayGenerator<Integer[]> intGenerator = new ArrayGenerator<>(Integer[].class);
        Integer[] arrInt = intGenerator.generate(10);
        System.out.println(Arrays.stream(arrInt).toArray());

        ArrayGenerator<int[]> pintGenerator = new ArrayGenerator<>(int[].class);
        int[] arrpInt = pintGenerator.generate(5);
        for (int num : arrpInt) {
            System.out.println(num);
        }

        ArrayGenerator<Boolean[]> booleanGenerator = new ArrayGenerator<>(Boolean[].class);
        Boolean[] arrBoolean = booleanGenerator.generate(10);
        for (Boolean b : arrBoolean) {
            System.out.println(b);
        }

        ArrayGenerator<Byte[]> byteArrayGenerator = new ArrayGenerator<>(Byte[].class);
        Byte[] arrByte = byteArrayGenerator.generate(10);
        for (Byte b : arrByte) {
            System.out.println(b);
        }

        ArrayGenerator<Character[]> charGenerator = new ArrayGenerator<>(Character[].class);
        Character[] arrChar = charGenerator.generate(10);
        for (Character ch : arrChar) {
            System.out.println(ch);
        }

        ArrayGenerator<Double[]> doubleGenerator = new ArrayGenerator<>(Double[].class);
        Double[] arrDouble = doubleGenerator.generate(10);
        for (Double d : arrDouble) {
            System.out.println(d);
        }

        ArrayGenerator<Float[]> floatArrayGenerator = new ArrayGenerator<>(Float[].class);
        Float[] arrFloat = floatArrayGenerator.generate(10);
        for (Float f : arrFloat) {
            System.out.println(f);
        }

        ArrayGenerator<Long[]> longArrayGenerator = new ArrayGenerator<>(Long[].class);
        Long[] arrLong = longArrayGenerator.generate(10);
        for (Long l : arrLong) {
            System.out.println(l);
        }

        ArrayGenerator<Short[]> shortArrayGenerator = new ArrayGenerator<>(Short[].class);
        Short[] arrShort = shortArrayGenerator.generate(10);
        for (Short s : arrShort) {
            System.out.println(s);
        }

        ArrayGenerator<String[]> stringArrayGenerator = new ArrayGenerator<>(String[].class);
        String[] arrString = stringArrayGenerator.generate(10);
        for (String s : arrString) {
            System.out.println(s);
        }

        ArrayGenerator<Size[]> sizeArrayGenerator = new ArrayGenerator<>(Size[].class);
        Size[] arrSize = sizeArrayGenerator.generate(10);
        for (Size e : arrSize) {
            System.out.println(e.toString());
        }

        // 二维数组
        ArrayGenerator<Integer[][]> multiDimension = new ArrayGenerator<>(Integer[][].class);
        Integer[][] arrMulti = multiDimension.generate(10);
        for (Integer[] arr : arrMulti) {
            for (Integer num : arr) {
                System.out.print(num.toString() + " ");
            }
            System.out.println();
        }

    }

    @Test
    void testClass() {
        ArrayGenerator<Numbers[]> generator = new ArrayGenerator<>(Numbers[].class);
        Numbers[] array = generator.generate(10);
        for (Numbers numbers : array) {
            System.out.println(Integer.toString(numbers.num1) + " " + Integer.toString(numbers.num2));
        }
    }

}