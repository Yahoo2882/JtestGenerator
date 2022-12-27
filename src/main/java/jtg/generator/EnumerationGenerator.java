package jtg.generator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumerationGenerator<T extends Enum> extends TypeKnownGenerator {

    private T[] enumValues = null;

    public EnumerationGenerator(Class enumType) {
        try {
            Method getEnumValues = enumType.getMethod("values");
            enumValues = (T[])getEnumValues.invoke(null);
        } catch (NoSuchMethodException exception) {
            System.err.println(exception.toString());
        } catch (InvocationTargetException e) {
            System.err.println(e.toString());
        } catch (IllegalAccessException e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public T generate() {
        T ret = enumValues[(int)randomIntegerFromRange(0, enumValues.length - 1)];
        return ret;
    }
}
