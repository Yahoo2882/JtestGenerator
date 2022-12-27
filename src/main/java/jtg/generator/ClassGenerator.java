package jtg.generator;

import javax.net.ssl.SNIHostName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ClassGenerator<T> extends TypeUnknownGenerator<T> {

    private Class type;

    private Constructor<?>[] constructors;

    public ClassGenerator(Class clazz) {
        type = clazz;
        constructors = clazz.getConstructors();
    }

    @Override
    public T generate(Object... params) {
        Constructor constructor = constructors[getConstructorIndex()];
        Class[] paramsTypes = constructor.getParameterTypes();

        Object[] objects = new Object[paramsTypes.length];
        for (int i = 0; i < paramsTypes.length; ++i) {
            objects[i] = generateParam(paramsTypes[i]);
        }

        T result = null;

        final int paramsLength = paramsTypes.length;
        try {
            result = (T)constructor.newInstance(objects);
        } catch (InstantiationException e) {
            System.err.println(e.toString());
        } catch (IllegalAccessException e) {
            System.err.println(e.toString());
        } catch (InvocationTargetException e) {
            System.err.println(e.toString());
        }

        return result;
    }

    private <E> E generateParam(Class<E> paramClass) {
        // 数组
        if (paramClass.isArray()) {
            ArrayGenerator arrayGenerator = new ArrayGenerator(paramClass);
            return (E)arrayGenerator.generate(10);  // 默认生成长度为 10 的随机数组
        }

        RandomGenerator generator = null;
        // 基本类型 + String
        switch (paramClass.toString()) {
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

        if (Enum.class.isAssignableFrom(paramClass)) {
            generator = new EnumerationGenerator(paramClass);
        }

        // 自定义类型
        if (generator == null) {
            generator = new ClassGenerator(paramClass);
            return (E)generator.generate(null);
        }

        return (E)generator.generate();
    }

    private int getConstructorIndex() {
        return (int)randomIntegerFromRange(0, constructors.length - 1);
    }
}
