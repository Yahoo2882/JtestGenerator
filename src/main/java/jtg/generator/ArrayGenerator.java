package jtg.generator;

import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import heros.flowfunc.Gen;
import soot.G;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 生成指定长度数组，数组内元素随机生成
 * 使用方法:
 *      ArrayGenerator<数组类型> arrayGenerator = new ArrayGenerator<>(数组类型.class);
 *      arrayGenerator.generate(数组长度);
 * 示例：
 * --------------------------- >>> 生成 int[5] <<< ------------------------------------
 * ArrayGenerator<int[]> arrayGenerator = new ArrayGenerator<>(int[].class);
 * int[] array = arrayGenerator.generate(5);
 *
 * -------------------------- >>> 生成 Integer[5] <<< ---------------------------------
 * ArrayGenerator<Integer[]> arrayGenerator = new ArrayGenerator<>(Integer[].class);
 * Integer[] array = arrayGenerator.generate(5);
 *
 * -------------------------- >>> 生成 Integer[5][5] <<< ------------------------------
 * // 为了调用方便，仅支持各个维度长度相同的数组
 * ArrayGenerator<Integer[][]> arrayGenerator = new ArrayGenerator<>(Integer[][].class);
 * Integer[][] array = arrayGenerator.generate(5);
 *
 * @param <T> 数组的类型
 */
public class ArrayGenerator<T> extends TypeUnknownGenerator<T> {

    private Class<T> arrayType;

    /**
     *
     * @param clazz 数组的类型
     */
    public ArrayGenerator(Class<T> clazz) {
        arrayType = clazz;
    }

    /**
     * 生成指定长度的数组
     *
     * @param params [0] 数组的长度
     * @return
     */
    @Override
    public T generate(Object... params) {
        int length = (int)params[0];
        T array = (T)generateArray(arrayType, length);
        return array;
    }

    /**
     *
     * @param clazz 数组类型
     * @param length
     * @return
     */
    private Object generateArray(Class clazz, int length) {
        if (!clazz.isArray()) {
            return generateOneItemAccordingClass(clazz);
        } else {
            Object array = Array.newInstance(clazz.getComponentType(), length);
            for (int i = 0; i < length; ++i) {
                Array.set(array, i, generateArray(clazz.getComponentType(), length));
            }
            return array;
        }
    }

    private Object generateOneItemAccordingClass(Class clazz) {
        RandomGenerator generator = null;
        // 基本类型 + String
        switch (clazz.toString()) {
            case "int":
            case "class java.lang.Integer":
                generator = new IntGenerator();
                break;
            case "boolean":
            case "class java.lang.Boolean":
                generator = new BooleanGenerator();
                break;
            case "byte":
            case "class java.lang.Byte":
                generator = new ByteGenerator();
                break;
            case "char":
            case "class java.lang.Character":
                generator = new CharGenerator();
                break;
            case "double":
            case "class java.lang.Double":
                generator = new DoubleGenerator();
                break;
            case "float":
            case "class java.lang.Float":
                generator = new FloatGenerator();
                break;
            case "long":
            case "class java.lang.Long":
                generator = new LongGenerator();
                break;
            case "short":
            case "class java.lang.Short":
                generator = new ShortGenerator();
                break;
            case "class java.lang.String":
                generator = new StringGenerator();
                break;
            default:
                break;
        }

        if (Enum.class.isAssignableFrom(clazz)) {
            generator = new EnumerationGenerator(clazz);
        }

        // 自定义类型
        if (generator == null) {
            generator = new ClassGenerator(clazz);
            return generator.generate(null);
        }

        return generator.generate();
    }
}
